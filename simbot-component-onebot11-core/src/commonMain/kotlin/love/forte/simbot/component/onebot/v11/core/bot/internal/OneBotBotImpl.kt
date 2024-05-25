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
import io.ktor.http.*
import kotlinx.coroutines.Job
import love.forte.simbot.bot.ContactRelation
import love.forte.simbot.bot.GroupRelation
import love.forte.simbot.bot.GuildRelation
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
    override val coroutineContext: CoroutineContext,
    override val job: Job,
    override val configuration: OneBotBotConfiguration,
    override val component: OneBot11Component,
    private val eventProcessor: EventProcessor,
) : OneBotBot, JobBasedBot() {
    override val apiClient: HttpClient

    init {
        apiClient = resolveHttpClient()
        job.invokeOnCompletion { apiClient.close() }
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

    override val apiHost: Url = requireNotNull(configuration.apiServerHost) {
        "Required config property 'apiServerHost' is null"
    }

    override val accessToken: String? = configuration.accessToken

    override val id: ID =
        requireNotNull(configuration.botUniqueId) {
            "Required config property 'botUniqueId' is null"
        }.ID

    @Volatile
    private var _loginInfoResult: GetLoginInfoResult? = null

    override suspend fun getLoginInfo(): GetLoginInfoResult {
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


    override suspend fun start() {
        TODO("Not yet implemented")
    }

    override val contactRelation: ContactRelation?
        get() = TODO("Not yet implemented")

    override val groupRelation: GroupRelation?
        get() = TODO("Not yet implemented")

    override val guildRelation: GuildRelation?
        get() = TODO("Not yet implemented")
}
