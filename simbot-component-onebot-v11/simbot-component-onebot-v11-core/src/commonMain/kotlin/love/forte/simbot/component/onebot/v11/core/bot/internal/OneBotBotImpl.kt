/*
 * Copyright (c) 2024. ForteScarlet.
 *
 * This file is part of simbot-component-onebot.
 *
 * simbot-component-onebot is free software: you can redistribute it and/or modify it under the terms
 * of the GNU Lesser General Public License as published by the Free Software Foundation,
 * either version 3 of the License, or (at your option) any later version.
 *
 * simbot-component-onebot is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with simbot-component-onebot.
 * If not, see <https://www.gnu.org/licenses/>.
 */

package love.forte.simbot.component.onebot.v11.core.bot.internal

import io.ktor.client.*
import io.ktor.client.plugins.websocket.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.websocket.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.overwriteWith
import love.forte.simbot.bot.JobBasedBot
import love.forte.simbot.common.collectable.Collectable
import love.forte.simbot.common.collectable.flowCollectable
import love.forte.simbot.common.coroutines.IOOrDefault
import love.forte.simbot.common.function.invokeWith
import love.forte.simbot.common.id.ID
import love.forte.simbot.common.id.StringID.Companion.ID
import love.forte.simbot.component.onebot.v11.core.actor.OneBotFriend
import love.forte.simbot.component.onebot.v11.core.actor.OneBotGroup
import love.forte.simbot.component.onebot.v11.core.actor.OneBotMember
import love.forte.simbot.component.onebot.v11.core.actor.OneBotStranger
import love.forte.simbot.component.onebot.v11.core.actor.internal.toFriend
import love.forte.simbot.component.onebot.v11.core.actor.internal.toGroup
import love.forte.simbot.component.onebot.v11.core.actor.internal.toMember
import love.forte.simbot.component.onebot.v11.core.actor.internal.toStranger
import love.forte.simbot.component.onebot.v11.core.api.*
import love.forte.simbot.component.onebot.v11.core.bot.OneBotBot
import love.forte.simbot.component.onebot.v11.core.bot.OneBotBotConfiguration
import love.forte.simbot.component.onebot.v11.core.bot.OneBotBotFriendRelation
import love.forte.simbot.component.onebot.v11.core.bot.OneBotBotGroupRelation
import love.forte.simbot.component.onebot.v11.core.component.OneBot11Component
import love.forte.simbot.component.onebot.v11.core.dialect.ExperimentalOneBotDialect
import love.forte.simbot.component.onebot.v11.core.event.internal.stage.OneBotBotStartedEventImpl
import love.forte.simbot.component.onebot.v11.core.internal.message.OneBotMessageContentImpl
import love.forte.simbot.component.onebot.v11.core.utils.onEachErrorLog
import love.forte.simbot.component.onebot.v11.message.OneBotMessageContent
import love.forte.simbot.event.Event
import love.forte.simbot.event.EventProcessor
import love.forte.simbot.event.EventResult
import love.forte.simbot.logger.LoggerFactory
import kotlin.concurrent.Volatile
import kotlin.coroutines.CoroutineContext
import kotlin.math.max
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds
import love.forte.simbot.component.onebot.v11.event.RawEvent as OBRawEvent


/**
 * [OneBotBot] 的实现
 * @author ForteScarlet
 */
@OptIn(ExperimentalOneBotDialect::class)
internal class OneBotBotImpl(
    private val uniqueId: String,
    override val coroutineContext: CoroutineContext,
    override val job: Job,
    override val configuration: OneBotBotConfiguration,
    override val component: OneBot11Component,
    private val eventProcessor: EventProcessor,
    baseDecoderJson: Json,
) : OneBotBot, JobBasedBot() {
    companion object {
        private const val BASE_LOGGER_NAME =
            "love.forte.simbot.component.onebot.v11.core.bot.OneBotBot"
    }

    override val subContext = coroutineContext.minusKey(Job)
    override val decoderJson: Json = Json(baseDecoderJson) {
        configuration.serializersModule?.also { confMd ->
            serializersModule = serializersModule overwriteWith confMd
        }
    }

    override val logger = LoggerFactory.getLogger("$BASE_LOGGER_NAME.$uniqueId")

    override val dialect = configuration.dialectFactory.createDialect(this)

    private val eventServerHost = configuration.eventServerHost
    private val connectMaxRetryTimes = configuration.wsConnectMaxRetryTimes
    private val connectRetryDelay = max(configuration.wsConnectRetryDelayMillis, 0L).milliseconds

    override val apiClient: HttpClient
    private val wsClient: HttpClient?


    init {
        apiClient = resolveHttpClient()
        wsClient = if (eventServerHost != null) {
            resolveWsClient()
        } else {
            null
        }

        job.invokeOnCompletion {
            apiClient.close()
            wsClient?.close()
        }
    }

    private val wsEnabled: Boolean
        get() = wsClient != null

    private inline fun resolveHttpClient(crossinline block: HttpClientConfig<*>.() -> Unit = {}): HttpClient {
        val apiClientConfigurer = configuration.apiClientConfigurer

        val engine = configuration.apiClientEngine
        val engineFactory = configuration.apiClientEngineFactory
        // 不能二者都有
        require(!(engine != null && engineFactory != null)) {
            "`apiClientEngine` and `apiClientEngineFactory` can only have one that is not null."
        }

        return when {
            engine == null && engineFactory == null -> HttpClient {
                apiClientConfigurer?.invokeWith(this)
                block()
            }

            engine != null -> HttpClient(engine) {
                apiClientConfigurer?.invokeWith(this)
                block()
            }

            engineFactory != null -> HttpClient(engineFactory) {
                apiClientConfigurer?.invokeWith(this)
                block()
            }

            else ->
                throw IllegalArgumentException("`engine` and `engineFactory` only need one.")
        }
    }

    private inline fun resolveWsClient(
        crossinline wsConfig: WebSockets.Config.() -> Unit = {},
        crossinline block: HttpClientConfig<*>.() -> Unit = {}
    ): HttpClient {
        val engine = configuration.wsClientEngine
        val engineFactory = configuration.wsClientEngineFactory
        // 不能二者都有
        require(!(engine != null && engineFactory != null)) {
            "`wsClientEngine` and `wsClientEngineFactory` can only have one that is not null."
        }

        return when {
            engine == null && engineFactory == null -> HttpClient {
                WebSockets {
                    wsConfig()
                }
                block()
            }

            engine != null -> HttpClient(engine) {
                WebSockets {
                    wsConfig()
                }
                block()
            }

            engineFactory != null -> HttpClient(engineFactory) {
                WebSockets {
                    wsConfig()
                }
                block()
            }

            else ->
                throw IllegalArgumentException("`engine` and `engineFactory` only need one.")
        }
    }

    override val apiHost: Url = configuration.apiServerHost

    override val apiAccessToken: String? = configuration.apiAccessToken
    override val eventAccessToken: String? = configuration.eventAccessToken

    override val id: ID = uniqueId.ID

    @Volatile
    private var _loginInfoResult: GetLoginInfoResult? = null

    override suspend fun queryLoginInfo(): GetLoginInfoResult {
        return dialect.queryLoginInfo().also {
            _loginInfoResult = it
        }
    }

    private val loginInfoResult: GetLoginInfoResult
        // LoginInfo尚未初始化
        get() = checkNotNull(_loginInfoResult) {
            "Login info has not been initialised"
        }

    override val userId: ID
        get() = loginInfoResult.userId

    override val name: String
        get() = loginInfoResult.nickname

    override fun isMe(id: ID): Boolean {
        return dialect.checkIsMe(id, _loginInfoResult)
    }

    /**
     * 当前的 ws session
     *
     * 需要在 [startLock] 中进行修改
     */
    @Volatile
    private var wsSession: WsEventSession? = null
    private val startLock = Mutex()


    override suspend fun start(): Unit = startLock.withLock {
        job.ensureActive()

        // 更新个人信息
        val info = queryLoginInfo()
        logger.debug("Update bot login info: {}", info)

        if (wsEnabled) {
            val wsHost = eventServerHost!!
            val client = wsClient!!
            logger.debug("WebSocket connection is enabled to {} via client {}", wsHost, client)
            val wsSession = createEventSession(client, wsHost).also { s ->
                // init it first
                val initialSession = s.createSessionWithRetry()
                launch { s.launch(initialSession) }
            }
            logger.debug("WebSocket session connected: {}", wsSession)
            this.wsSession = wsSession
        } else {
            logger.debug("WebSocket connection is disabled because of the `eventServerHost` is null")
        }

        if (!isStarted) {
            isStarted = true
            launch {
                eventProcessor
                    .push(OneBotBotStartedEventImpl(this@OneBotBotImpl))
                    .onEachErrorLog(logger)
                    .collect()
            }
        }
    }

    private fun createEventSession(wsClient: HttpClient, host: Url): WsEventSession {
        // Cancel current session if exists
        val currentSession = wsSession
        wsSession = null
        currentSession?.cancel()

        // OB11 似乎没有什么心跳之类乱七八糟的，似乎可以直接省略状态机
        // 直接连接、断线重连
        return WsEventSession(wsClient, host)
    }


    private inner class WsEventSession(
        val wsClient: HttpClient,
        val wsHost: Url
    ) {
        private val sessionJob = Job(this@OneBotBotImpl.job)
        private var session: DefaultWebSocketSession? = null

        suspend fun createSession(): DefaultWebSocketSession {
            return wsClient.webSocketSession {
                url {
                    takeFrom(wsHost)
                    eventAccessToken?.also { bearerAuth(it) }
                }
            }
        }

        suspend fun createSessionWithRetry(): DefaultWebSocketSession? {
            var session: DefaultWebSocketSession? = null
            var retryTimes = 0

            while (session == null && retryTimes <= connectMaxRetryTimes) {
                try {
                    logger.debug("Connect to ws server {}", wsHost)
                    session = createSession()
                } catch (e: Exception) {
                    retryTimes++

                    @Suppress("ConvertTwoComparisonsToRangeCheck")
                    if (connectMaxRetryTimes > 0 && retryTimes > connectMaxRetryTimes) {
                        "Connect to ws server $wsHost failed in $retryTimes times.".also { msg ->
                            val ex = IllegalStateException(msg)
                            sessionJob.completeExceptionally(ex)

                            throw ex
                        }
                    }

                    if (logger.isWarnEnabled()) {
                        logger.warn(
                            "Connect to ws server {} failed: {}, retry in {}...",
                            wsHost,
                            e.message,
                            connectRetryDelay.toString(),
                            e,
                        )
                    }

                    delay(connectRetryDelay)
                    continue
                }
            }

            if (session == null || retryTimes >= connectMaxRetryTimes) {
                sessionJob.completeExceptionally(
                    IllegalStateException("Connect to ws server failed in $retryTimes times.")
                )

                return null
            }

            return session
        }

        /**
         * 开始创建连接并持续地接收事件。
         * 在接收过程中，只要 [sessionJob] 未被关闭，
         * 如果会话被断开（通常是被远端服务器断开）
         * 则会尝试重连（并顺便修改 [session] 的值）。
         *
         * 如果因为重试失败而被终结，
         * 也会连带着 [OneBotBotImpl.job] 一起结束。
         *
         */
        @OptIn(DelicateCoroutinesApi::class)
        suspend fun launch(initialSession: DefaultWebSocketSession? = null) {
            var session: DefaultWebSocketSession? = initialSession
            while (sessionJob.isActive) {
                if (session?.isActive != true) {
                    session = null
                }

                val currentSession = session ?: createSessionWithRetry().also {
                    session = it
                }

                if (currentSession == null) return

                logger.debug("Connected to ws server {}, session: {}", wsHost, currentSession)

                this@WsEventSession.session = currentSession

                val completionHandle = sessionJob.invokeOnCompletion {
                    if (currentSession.isActive) {
                        currentSession.cancel("Job is completed: ${it?.message}", it)
                    }
                }

                kotlin.runCatching {
                    receiveEvent(currentSession)
                }.onFailure { ex ->
                    logger.error("Event reception is interrupted: {}", ex.message, ex)
                }

                // The Session is done or dead,
                // or the job is done.

                // 如果会话仍然处于活跃状态，
                // 尝试关闭它，并首先尝试在异步中通过发送 close 数据包的形式进行关闭
                // 如果 5s 内无法完成此行为，则直接使用 cancel
                if (currentSession.isActive) {
                    GlobalScope.launch(Dispatchers.IOOrDefault) {
                        try {
                            withTimeout(5.seconds) {
                                currentSession.close()
                            }
                        } catch (timeout: TimeoutCancellationException) {
                            currentSession.cancel("Session close timeout: $timeout", timeout)
                        }
                    }
                } else {
                    // 否则，取消回调即可
                    // 不活跃的会话可能是被回调关闭的，也可能不是，但无所谓
                    completionHandle.dispose()
                }

                // 等待关闭完成
                val reason = kotlin.runCatching {
                    currentSession.closeReason.await()
                }.getOrElse { e ->
                    logger.debug("Failed to get close reason for session: {}", e.message, e)
                    null
                }
                logger.debug("Session {} done. The reason: {}", currentSession, reason)
            }

            logger.debug(
                "The EventSession is done.",
                isActive,
                sessionJob.isActive
            )
        }

        @Suppress("LoopWithTooManyJumpStatements")
        private suspend fun receiveEvent(session: DefaultWebSocketSession) {
            with(session) {
                while (isActive && sessionJob.isActive) {
                    val frameResult = incoming.receiveCatching()

                    if (!frameResult.isSuccess) {
                        if (frameResult.isClosed) {
                            logger.debug("Session received Close frame result: {}", frameResult)
                            break
                        }
                        if (frameResult.isFailure) {
                            val ex = frameResult.exceptionOrNull()
                            logger.debug(
                                "Session received Failure frame result: {}, exception: {}",
                                frameResult,
                                ex,
                                ex
                            )

                            when (ex) {
                                is CancellationException -> break
                                else -> {
                                    continue
                                }
                            }
                        }

                        continue
                    }

                    val eventRaw = when (val frame = frameResult.getOrNull()) {
                        is Frame.Text -> frame.readText()
                        is Frame.Binary -> frame.readBytes().decodeToString()
                        else -> {
                            logger.debug(
                                "Received frame {}, but is not Text or Binary, skip resolve."
                            )

                            null
                        }
                    } ?: continue

                    logger.debug("Received raw event: {}", eventRaw)

                    val event = kotlin.runCatching {
                        resolveRawEvent(eventRaw)
                    }.getOrElse { e ->
                        val exMsg = "Failed to resolve raw event $eventRaw, " +
                            "session and bot will be closed exceptionally"

                        val ex = IllegalStateException(
                            exMsg,
                            e
                        )
                        // 接收的事件解析出现错误，
                        // 这应该是预期外的情况，
                        // 直接终止 session, 但是不终止 Bot，
                        // 只有当重连次数用尽才考虑终止 Bot。
                        session.closeExceptionally(ex)

                        throw ex
                    }

                    pushEvent(resolveRawEventToEvent(eventRaw, event))
                        .launchIn(this@OneBotBotImpl)
                }
            }
        }

        fun cancel() {
            sessionJob.cancel()
        }
    }

    override val contactRelation: OneBotBotFriendRelation = FriendRelationImpl()

    private inner class FriendRelationImpl : OneBotBotFriendRelation {
        override val contacts: Collectable<OneBotFriend>
            get() = flowCollectable {
                val flow = dialect.getFriends()
                emitAll(flow.map { it.toFriend(this@OneBotBotImpl) })
            }

        override suspend fun contact(id: ID): OneBotFriend? {
            return dialect.getFriend(id)?.toFriend(this@OneBotBotImpl)
        }

        override suspend fun contactCount(): Int {
            return dialect.getFriendCount()
        }

        override suspend fun stranger(id: ID): OneBotStranger =
            dialect.getStrangerInfo(id).toStranger(this@OneBotBotImpl)
    }

    // 与群聊相关的操作
    override val groupRelation: OneBotBotGroupRelation = GroupRelationImpl()

    private inner class GroupRelationImpl : OneBotBotGroupRelation {
        override val groups: Collectable<OneBotGroup>
            get() = flowCollectable {
                val flow = dialect.getGroups()
                emitAll(flow.map { it.toGroup(this@OneBotBotImpl) })
            }

        override suspend fun group(id: ID): OneBotGroup? {
            val info = dialect.getGroup(id)

            return info?.toGroup(
                this@OneBotBotImpl,
                // TODO owner?
            )
        }

        override suspend fun groupCount(): Int {
            return dialect.getGroupCount()
        }

        override suspend fun member(groupId: ID, memberId: ID): OneBotMember {
            return dialect.getGroupMember(groupId, memberId)
                .toMember(this@OneBotBotImpl)
        }
    }

    override suspend fun getCookies(domain: String?): GetCookiesResult =
        this.executeData(GetCookiesApi.create(domain))

    override suspend fun getCredentials(domain: String?): GetCredentialsResult =
        executeData(GetCredentialsApi.create(domain))

    override suspend fun getCsrfToken(): GetCsrfTokenResult =
        executeData(GetCsrfTokenApi.create())

    override suspend fun getMessageContent(messageId: ID): OneBotMessageContent {
        val result = this.executeData(GetMsgApi.create(messageId))
        return OneBotMessageContentImpl(
            result.messageId,
            result.message,
            this
        )
    }

    override suspend fun execute(api: OneBotApi<*>): HttpResponse =
        api.request(
            client = this.apiClient,
            host = this.apiHost,
            accessToken = this.apiAccessToken
        )

    override suspend fun executeRaw(api: OneBotApi<*>): String =
        api.requestRaw(
            client = this.apiClient,
            host = this.apiHost,
            accessToken = this.apiAccessToken
        )

    override suspend fun <T : Any> executeResult(api: OneBotApi<T>): OneBotApiResult<T> =
        api.requestResult(
            client = this.apiClient,
            host = this.apiHost,
            accessToken = this.apiAccessToken,
            decoder = this.decoderJson
        )

    override suspend fun <T : Any> executeData(api: OneBotApi<T>): T =
        api.requestData(
            client = this.apiClient,
            host = this.apiHost,
            accessToken = this.apiAccessToken,
            decoder = this.decoderJson
        )

    override fun push(rawEvent: String): Flow<EventResult> {
        val event = kotlin.runCatching {
            resolveRawEvent(rawEvent)
        }.getOrElse { e ->
            val exMsg = "Failed to resolve raw event $rawEvent, " +
                "session and bot will be closed exceptionally"

            throw IllegalArgumentException(exMsg, e)
        }

        return pushEvent(resolveRawEventToEvent(rawEvent, event))
    }

    private fun pushEvent(event: Event): Flow<EventResult> {
        return eventProcessor
            .push(event)
            .onEachErrorLog(logger)
    }

    override fun toString(): String =
        "OneBotBot(uniqueId='$uniqueId', isStarted=$isStarted, isActive=$isActive)"
}


/**
 * 解析数据包字符串为 [Event]。
 */
@OptIn(ExperimentalOneBotDialect::class)
internal fun OneBotBot.resolveRawEvent(raw: String): OBRawEvent {
    return dialect.resolveRawEvent(raw)
}

@OptIn(ExperimentalOneBotDialect::class)
internal fun OneBotBot.resolveRawEventToEvent(raw: String, event: OBRawEvent): Event {
    return dialect.resolveRawEventToEvent(raw, event)
}
