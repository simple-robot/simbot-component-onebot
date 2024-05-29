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
import io.ktor.http.*
import io.ktor.websocket.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import love.forte.simbot.bot.ContactRelation
import love.forte.simbot.bot.GroupRelation
import love.forte.simbot.bot.JobBasedBot
import love.forte.simbot.common.function.invokeWith
import love.forte.simbot.common.id.ID
import love.forte.simbot.common.id.StringID.Companion.ID
import love.forte.simbot.component.onebot.v11.core.OneBot11Component
import love.forte.simbot.component.onebot.v11.core.api.GetLoginInfoApi
import love.forte.simbot.component.onebot.v11.core.api.GetLoginInfoResult
import love.forte.simbot.component.onebot.v11.core.bot.OneBotBot
import love.forte.simbot.component.onebot.v11.core.bot.OneBotBotConfiguration
import love.forte.simbot.component.onebot.v11.core.bot.requestDataBy
import love.forte.simbot.event.EventProcessor
import kotlin.concurrent.Volatile
import kotlin.coroutines.CoroutineContext


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
        wsClient = resolveWsClient(
            wsConfig = {
                pingInterval = 6000L
            }
        )
        job.invokeOnCompletion { apiClient.close() }
    }

    private val eventServerHost =
        requireNotNull(configuration.eventServerHost) {
            "Required property `eventServerHost` is null"
        }

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

    override val apiHost: Url = requireNotNull(configuration.apiServerHost) {
        "Required config property 'apiServerHost' is null"
    }

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

    @Volatile
    private var wsSession: WsEventSession? = null
    private val startLock = Mutex()


    override suspend fun start() = startLock.withLock {
        // TODO cancel current session if exists

        // OB11 似乎没有什么心跳之类乱七八糟的，似乎可以直接省略状态机
        // 直接连接、断线重连
        val host = configuration.eventServerHost
        val session = wsClient.webSocketSession(eventServerHost.toString()) {
            TODO("")
        }

        wsSession = WsEventSession(session).also { it.launch(this) }

        isStarted = true
        TODO("Not yet implemented")
    }

    // 联系人相关操作，OB里即为好友
    override val contactRelation: ContactRelation
        get() = TODO("Not yet implemented")

    // 与群聊相关的操作
    override val groupRelation: GroupRelation
        get() = TODO("Not yet implemented")


    override fun toString(): String =
        "OneBotBotImpl(uniqueId='$uniqueId', isStarted=$isStarted, isActive=$isActive)"
}


private class WsEventSession(
    initialSession: DefaultWebSocketSession,
) {
    private var session: DefaultWebSocketSession = initialSession

    fun launch(scope: CoroutineScope): Job {
        return scope.launch {
            while (scope.isActive) {
                with(session) {
                    while (session.isActive) {
                        val receive = incoming.receive()
                        // TODO incoming.tryReceive() ?

                    }
                }
            }
        }
    }

    fun cancel() {
        // TODO ?
    }
}
