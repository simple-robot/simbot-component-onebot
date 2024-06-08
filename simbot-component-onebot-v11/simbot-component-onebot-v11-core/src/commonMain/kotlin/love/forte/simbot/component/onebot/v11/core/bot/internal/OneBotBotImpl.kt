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

import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.plugins.websocket.WebSockets
import io.ktor.client.plugins.websocket.webSocketSession
import io.ktor.client.request.bearerAuth
import io.ktor.http.Url
import io.ktor.http.takeFrom
import io.ktor.websocket.DefaultWebSocketSession
import io.ktor.websocket.Frame
import io.ktor.websocket.close
import io.ktor.websocket.closeExceptionally
import io.ktor.websocket.readBytes
import io.ktor.websocket.readText
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withTimeout
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonPrimitive
import kotlinx.serialization.json.long
import love.forte.simbot.annotations.FragileSimbotAPI
import love.forte.simbot.bot.JobBasedBot
import love.forte.simbot.common.collectable.Collectable
import love.forte.simbot.common.collectable.flowCollectable
import love.forte.simbot.common.coroutines.IOOrDefault
import love.forte.simbot.common.function.invokeWith
import love.forte.simbot.common.id.ID
import love.forte.simbot.common.id.LongID.Companion.ID
import love.forte.simbot.common.id.StringID.Companion.ID
import love.forte.simbot.component.onebot.v11.core.OneBot11
import love.forte.simbot.component.onebot.v11.core.actor.OneBotFriend
import love.forte.simbot.component.onebot.v11.core.actor.OneBotGroup
import love.forte.simbot.component.onebot.v11.core.actor.internal.toFriend
import love.forte.simbot.component.onebot.v11.core.actor.internal.toGroup
import love.forte.simbot.component.onebot.v11.core.api.GetFriendListApi
import love.forte.simbot.component.onebot.v11.core.api.GetGroupInfoApi
import love.forte.simbot.component.onebot.v11.core.api.GetGroupListApi
import love.forte.simbot.component.onebot.v11.core.api.GetLoginInfoApi
import love.forte.simbot.component.onebot.v11.core.api.GetLoginInfoResult
import love.forte.simbot.component.onebot.v11.core.bot.OneBotBot
import love.forte.simbot.component.onebot.v11.core.bot.OneBotBotConfiguration
import love.forte.simbot.component.onebot.v11.core.bot.OneBotBotFriendRelation
import love.forte.simbot.component.onebot.v11.core.bot.OneBotBotGroupRelation
import love.forte.simbot.component.onebot.v11.core.bot.requestDataBy
import love.forte.simbot.component.onebot.v11.core.bot.requestResultBy
import love.forte.simbot.component.onebot.v11.core.component.OneBot11Component
import love.forte.simbot.component.onebot.v11.core.event.OneBotUnknownEvent
import love.forte.simbot.component.onebot.v11.core.event.OneBotUnsupportedEvent
import love.forte.simbot.component.onebot.v11.core.event.internal.message.OneBotAnonymousGroupMessageEventImpl
import love.forte.simbot.component.onebot.v11.core.event.internal.message.OneBotDefaultGroupMessageEventImpl
import love.forte.simbot.component.onebot.v11.core.event.internal.message.OneBotDefaultPrivateMessageEventImpl
import love.forte.simbot.component.onebot.v11.core.event.internal.message.OneBotFriendMessageEventImpl
import love.forte.simbot.component.onebot.v11.core.event.internal.message.OneBotGroupPrivateMessageEventImpl
import love.forte.simbot.component.onebot.v11.core.event.internal.message.OneBotNormalGroupMessageEventImpl
import love.forte.simbot.component.onebot.v11.core.event.internal.message.OneBotNoticeGroupMessageEventImpl
import love.forte.simbot.component.onebot.v11.core.event.internal.meta.OneBotHeartbeatEventImpl
import love.forte.simbot.component.onebot.v11.core.event.internal.meta.OneBotLifecycleEventImpl
import love.forte.simbot.component.onebot.v11.core.event.internal.notice.OneBotBotSelfPokeEventImpl
import love.forte.simbot.component.onebot.v11.core.event.internal.notice.OneBotFriendAddEventImpl
import love.forte.simbot.component.onebot.v11.core.event.internal.notice.OneBotFriendRecallEventImpl
import love.forte.simbot.component.onebot.v11.core.event.internal.notice.OneBotGroupAdminEventImpl
import love.forte.simbot.component.onebot.v11.core.event.internal.notice.OneBotGroupBanEventImpl
import love.forte.simbot.component.onebot.v11.core.event.internal.notice.OneBotGroupMemberDecreaseEventImpl
import love.forte.simbot.component.onebot.v11.core.event.internal.notice.OneBotGroupMemberIncreaseEventImpl
import love.forte.simbot.component.onebot.v11.core.event.internal.notice.OneBotGroupRecallEventImpl
import love.forte.simbot.component.onebot.v11.core.event.internal.notice.OneBotGroupUploadEventImpl
import love.forte.simbot.component.onebot.v11.core.event.internal.notice.OneBotHonorEventImpl
import love.forte.simbot.component.onebot.v11.core.event.internal.notice.OneBotLuckyKingEventImpl
import love.forte.simbot.component.onebot.v11.core.event.internal.notice.OneBotMemberPokeEventImpl
import love.forte.simbot.component.onebot.v11.core.event.internal.request.OneBotFriendRequestEventImpl
import love.forte.simbot.component.onebot.v11.core.event.internal.request.OneBotGroupRequestEventImpl
import love.forte.simbot.component.onebot.v11.core.event.internal.stage.OneBotBotStartedEventImpl
import love.forte.simbot.component.onebot.v11.core.utils.onEachErrorLog
import love.forte.simbot.component.onebot.v11.event.UnknownEvent
import love.forte.simbot.component.onebot.v11.event.message.GroupMessageEvent
import love.forte.simbot.component.onebot.v11.event.message.PrivateMessageEvent
import love.forte.simbot.component.onebot.v11.event.meta.HeartbeatEvent
import love.forte.simbot.component.onebot.v11.event.meta.LifecycleEvent
import love.forte.simbot.component.onebot.v11.event.notice.FriendAddEvent
import love.forte.simbot.component.onebot.v11.event.notice.FriendRecallEvent
import love.forte.simbot.component.onebot.v11.event.notice.GroupAdminEvent
import love.forte.simbot.component.onebot.v11.event.notice.GroupBanEvent
import love.forte.simbot.component.onebot.v11.event.notice.GroupDecreaseEvent
import love.forte.simbot.component.onebot.v11.event.notice.GroupIncreaseEvent
import love.forte.simbot.component.onebot.v11.event.notice.GroupRecallEvent
import love.forte.simbot.component.onebot.v11.event.notice.GroupUploadEvent
import love.forte.simbot.component.onebot.v11.event.notice.NotifyEvent
import love.forte.simbot.component.onebot.v11.event.request.FriendRequestEvent
import love.forte.simbot.component.onebot.v11.event.request.GroupRequestEvent
import love.forte.simbot.component.onebot.v11.event.resolveEventSerializer
import love.forte.simbot.component.onebot.v11.event.resolveEventSubTypeFieldName
import love.forte.simbot.event.Event
import love.forte.simbot.event.EventProcessor
import love.forte.simbot.logger.LoggerFactory
import kotlin.concurrent.Volatile
import kotlin.coroutines.CoroutineContext
import kotlin.math.max
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds
import love.forte.simbot.component.onebot.v11.event.Event as OBRawEvent


/**
 * [OneBotBot] 的实现
 * @author ForteScarlet
 */
internal class OneBotBotImpl(
    private val uniqueId: String,
    override val coroutineContext: CoroutineContext,
    override val job: Job,
    override val configuration: OneBotBotConfiguration,
    override val component: OneBot11Component,
    private val eventProcessor: EventProcessor,
) : OneBotBot, JobBasedBot() {
    override val apiClient: HttpClient
    private val wsClient: HttpClient

    init {
        apiClient = resolveHttpClient()
        wsClient = resolveWsClient()
        job.invokeOnCompletion { apiClient.close() }
    }

    override val subContext = coroutineContext.minusKey(Job)

    private val logger = LoggerFactory
        .getLogger(
            "love.forte.simbot.component.onebot.v11.core.bot.OneBotBot.$uniqueId"
        )

    private val eventServerHost = configuration.eventServerHost

    private val connectMaxRetryTimes = configuration.wsConnectMaxRetryTimes

    private val connectRetryDelay = max(configuration.wsConnectRetryDelayMillis, 0L).milliseconds

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

    override val accessToken: String? = configuration.accessToken

    override val id: ID = uniqueId.ID

    @Volatile
    private var _loginInfoResult: GetLoginInfoResult? = null

    override suspend fun queryLoginInfo(): GetLoginInfoResult {
        val result = GetLoginInfoApi.create().requestDataBy(this)
        _loginInfoResult = result
        return result
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
        if (id == this.id) return true
        return _loginInfoResult?.let { id == it.userId }
            ?: true
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

        wsSession = createEventSession().also { s ->
            launch { s.launch() }
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

    private fun createEventSession(): WsEventSession {
        // Cancel current session if exists
        val currentSession = wsSession
        wsSession = null
        currentSession?.cancel()

        // OB11 似乎没有什么心跳之类乱七八糟的，似乎可以直接省略状态机
        // 直接连接、断线重连
        return WsEventSession()
    }


    private inner class WsEventSession {
        private val sessionJob = Job(this@OneBotBotImpl.job)
        private var session: DefaultWebSocketSession? = null

        private suspend fun createSession(): DefaultWebSocketSession {
            return wsClient.webSocketSession {
                url {
                    takeFrom(eventServerHost)
                    accessToken?.also { bearerAuth(it) }
                }
            }
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
        suspend fun launch() {
            while (sessionJob.isActive) {
                var retryTimes = 0
                var session: DefaultWebSocketSession? = null
                do {
                    try {
                        logger.debug("Connect to ws server {}", eventServerHost)
                        session = createSession()
                    } catch (e: Exception) {
                        retryTimes++

                        @Suppress("ConvertTwoComparisonsToRangeCheck")
                        if (connectMaxRetryTimes > 0 && retryTimes > connectMaxRetryTimes) {
                            "Connect to ws server $eventServerHost failed in $retryTimes times.".also { msg ->
                                val ex = IllegalStateException(msg)
                                sessionJob.completeExceptionally(ex)

                                throw ex
                            }
                        }

                        if (logger.isWarnEnabled()) {
                            logger.warn(
                                "Connect to ws server {} failed: {}, retry in {}...",
                                eventServerHost,
                                e.message,
                                connectRetryDelay.toString(),
                                e,
                            )
                        }

                        delay(connectRetryDelay)
                        continue
                    }
                } while (session == null && retryTimes <= connectMaxRetryTimes)

                if (session == null || retryTimes >= connectMaxRetryTimes) {
                    sessionJob.completeExceptionally(
                        IllegalStateException("Connect to ws server failed in $retryTimes times.")
                    )

                    // "Connect to ws server failed in $retryTimes times.".also { msg ->
                    //     sessionJob.cancel(
                    //         msg,
                    //         IllegalStateException(msg)
                    //     )
                    // }

                    return
                }

                logger.debug("Connected to ws server {}: {}", eventServerHost, session)

                this@WsEventSession.session = session

                // val session =
                val completionHandle = sessionJob.invokeOnCompletion {
                    if (session.isActive) {
                        session.cancel("Job is completed: ${it?.message}", it)
                    }
                }

                receiveEvent(session)

                // The Session is done or dead,
                // or the job is done.

                // 如果会话仍然处于活跃状态，
                // 尝试关闭它，并首先尝试在异步中通过发送 close 数据包的形式进行关闭
                // 如果 5s 内无法完成此行为，则直接使用 cancel
                if (session.isActive) {
                    GlobalScope.launch(Dispatchers.IOOrDefault) {
                        try {
                            withTimeout(5.seconds) {
                                session.close()
                            }
                        } catch (timeout: TimeoutCancellationException) {
                            session.cancel("Session close timeout: $timeout", timeout)
                        }
                    }
                } else {
                    // 否则，取消回调即可
                    // 不活跃的会话可能是被回调关闭的，也可能不是，但无所谓
                    completionHandle.dispose()
                }

                // 等待关闭完成
                val reason = session.closeReason.await()
                logger.debug("Session {} done. The reason: {}", session, reason)
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
                        // 直接终止 session 和 Bot
                        session.closeExceptionally(ex)
                        job.cancel(exMsg, ex)

                        throw ex
                    }

                    pushEvent(resolveRawEventToEvent(eventRaw, event))
                }
            }
        }

        /**
         * 解析数据包字符串为 [Event]。
         */
        @OptIn(FragileSimbotAPI::class)
        private fun resolveRawEvent(text: String): OBRawEvent {
            val obj = OneBot11.DefaultJson.decodeFromString(
                JsonObject.serializer(),
                text
            )

            val postType = obj["post_type"]!!.jsonPrimitive.content
            val subTypeFieldName = resolveEventSubTypeFieldName(postType) ?: "${postType}_type"
            val subType = obj[subTypeFieldName]!!.jsonPrimitive.content
            resolveEventSerializer(postType, subType)?.let {
                return OneBot11.DefaultJson.decodeFromJsonElement(it, obj)
            } ?: run {
                val time = obj["time"]?.jsonPrimitive?.long ?: -1L
                val selfId = obj["self_id"]?.jsonPrimitive?.long?.ID ?: 0L.ID
                return UnknownEvent(time, selfId, postType, text)
            }
        }


        private fun pushEvent(event: Event): Job {
            return eventProcessor
                .push(event)
                .onEachErrorLog(logger)
                .launchIn(this@OneBotBotImpl)
        }

        fun cancel() {
            sessionJob.cancel()
        }
    }

    override val contactRelation: OneBotBotFriendRelation = FriendRelationImpl()

    private inner class FriendRelationImpl : OneBotBotFriendRelation {
        override val contacts: Collectable<OneBotFriend>
            get() = flowCollectable {
                val resultList = GetFriendListApi.create()
                    .requestDataBy(this@OneBotBotImpl)

                for (result in resultList) {
                    emit(result.toFriend(this@OneBotBotImpl))
                }
            }
    }

    // 与群聊相关的操作
    override val groupRelation: OneBotBotGroupRelation = GroupRelationImpl()

    private inner class GroupRelationImpl : OneBotBotGroupRelation {
        override val groups: Collectable<OneBotGroup>
            get() = flowCollectable {
                val resultList = GetGroupListApi.create()
                    .requestDataBy(this@OneBotBotImpl)

                for (groupInfoResult in resultList) {
                    emit(
                        groupInfoResult.toGroup(
                            this@OneBotBotImpl,
                            // TODO owner?
                        )
                    )
                }

            }

        override suspend fun group(id: ID): OneBotGroup {
            val result = GetGroupInfoApi.create(id)
                .requestResultBy(this@OneBotBotImpl)

            // TODO 如何检测不存在？

            return result.dataOrThrow.toGroup(
                this@OneBotBotImpl,
                // TODO owner?
            )
        }
    }

    override fun toString(): String =
        "OneBotBot(uniqueId='$uniqueId', isStarted=$isStarted, isActive=$isActive)"
}


@OptIn(FragileSimbotAPI::class)
internal fun OneBotBotImpl.resolveRawEventToEvent(raw: String, event: OBRawEvent): Event {
    val bot = this

    fun unsupported(): OneBotUnsupportedEvent =
        OneBotUnsupportedEvent(raw, event)

    return when (event) {
        //region 消息事件
        // 群消息、匿名消息、系统消息
        is GroupMessageEvent -> when (event.subType) {
            GroupMessageEvent.SUB_TYPE_NORMAL ->
                OneBotNormalGroupMessageEventImpl(raw, event, bot)

            GroupMessageEvent.SUB_TYPE_ANONYMOUS ->
                OneBotAnonymousGroupMessageEventImpl(raw, event, bot)

            GroupMessageEvent.SUB_TYPE_NOTICE ->
                OneBotNoticeGroupMessageEventImpl(raw, event, bot)

            else -> OneBotDefaultGroupMessageEventImpl(raw, event, bot)
        }

        // 好友私聊消息、成员临时会话
        is PrivateMessageEvent -> when (event.subType) {
            PrivateMessageEvent.SUB_TYPE_FRIEND ->
                OneBotFriendMessageEventImpl(raw, event, bot)

            PrivateMessageEvent.SUB_TYPE_GROUP ->
                OneBotGroupPrivateMessageEventImpl(raw, event, bot)

            else -> OneBotDefaultPrivateMessageEventImpl(raw, event, bot)
        }
        //endregion

        //region 元事件
        is LifecycleEvent -> OneBotLifecycleEventImpl(raw, event, bot,)
        is HeartbeatEvent -> OneBotHeartbeatEventImpl(raw, event, bot,)
        //endregion

        //region 申请事件
        is FriendRequestEvent -> OneBotFriendRequestEventImpl(raw, event, bot)
        is GroupRequestEvent -> OneBotGroupRequestEventImpl(raw, event, bot)
        //endregion

        //region notice events
        is FriendAddEvent -> OneBotFriendAddEventImpl(raw, event, bot)
        is FriendRecallEvent -> OneBotFriendRecallEventImpl(raw, event, bot)
        is GroupAdminEvent -> OneBotGroupAdminEventImpl(raw, event, bot)
        is GroupBanEvent -> OneBotGroupBanEventImpl(raw, event, bot)
        is GroupIncreaseEvent -> OneBotGroupMemberIncreaseEventImpl(raw, event, bot)
        is GroupDecreaseEvent -> OneBotGroupMemberDecreaseEventImpl(raw, event, bot)
        is GroupRecallEvent -> OneBotGroupRecallEventImpl(raw, event, bot)
        is GroupUploadEvent -> OneBotGroupUploadEventImpl(raw, event, bot)
        is NotifyEvent -> when (event.subType) {
            NotifyEvent.SUB_TYPE_HONOR -> OneBotHonorEventImpl(raw, event, bot)
            NotifyEvent.SUB_TYPE_LUCKY_KING -> OneBotLuckyKingEventImpl(raw, event, bot)
            NotifyEvent.SUB_TYPE_POKE -> when {
                event.selfId.value == event.targetId?.value ->
                    OneBotBotSelfPokeEventImpl(raw, event, bot)

                else -> OneBotMemberPokeEventImpl(raw, event, bot)
            }

            // Unsupported
            else -> unsupported()
        }

        //endregion
        is UnknownEvent -> OneBotUnknownEvent(raw, event)
        else -> unsupported()
    }
}
