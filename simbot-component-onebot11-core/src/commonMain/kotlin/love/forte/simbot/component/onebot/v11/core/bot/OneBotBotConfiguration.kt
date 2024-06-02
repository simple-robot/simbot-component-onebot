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

package love.forte.simbot.component.onebot.v11.core.bot

import io.ktor.client.*
import io.ktor.client.engine.*
import io.ktor.client.plugins.*
import io.ktor.http.*
import love.forte.simbot.common.function.ConfigurerFunction
import love.forte.simbot.common.function.invokeBy
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext


/**
 * 用于 [OneBotBot] 的配置类。
 *
 * @author ForteScarlet
 */
public class OneBotBotConfiguration {
    /**
     * 用于 Bot 的协程上下文实例。
     *
     * 如果其中包含 [Job][kotlinx.coroutines.Job]，
     * 则会在与 [OneBotBotManager]
     * 中（也可能存在的）[Job][kotlinx.coroutines.Job] 合并后，
     * 作为一个 parent Job 使用。
     */
    public var coroutineContext: CoroutineContext = EmptyCoroutineContext

    /**
     * 必填属性，在OneBot组件中用于区分不同Bot的唯一ID，
     * 建议可以直接使用QQ号。
     * 更多说明参考 [OneBotBot.id]。
     */
    public var botUniqueId: String? = null

    /**
     * 参考 [鉴权](https://github.com/botuniverse/onebot-11/blob/master/communication/authorization.md)
     *
     */
    public var accessToken: String? = null

    /**
     * 必填属性，HTTP API的目标服务器地址。
     */
    public var apiServerHost: Url = Url("http://localhost:3001")

    /**
     * 必填属性，订阅事件的目标服务器地址。应当是ws或wss协议。
     */
    public var eventServerHost: Url = Url("ws://localhost:3001")


    /**
     * 用于API请求的 [HttpClient] 所使用的[引擎](https://ktor.io/docs/http-client-engines.html)。
     *
     * 如果 [apiClientEngine] 和 [apiClientEngineFactory] 都为null，则会尝试使用一个默认引擎。
     *
     */
    public var apiClientEngine: HttpClientEngine? = null

    /**
     * 用于API请求的 [HttpClient] 所使用的[引擎](https://ktor.io/docs/http-client-engines.html) 工厂。
     *
     * 如果 [apiClientEngine] 和 [apiClientEngineFactory] 都为null，则会尝试使用一个默认引擎。
     *
     */
    public var apiClientEngineFactory: HttpClientEngineFactory<*>? = null

    /**
     * API请求中的超时请求配置。
     *
     * 如果 [apiHttpRequestTimeoutMillis]、[apiHttpRequestTimeoutMillis]、[apiHttpRequestTimeoutMillis] 都为null，则不会配置timeout。
     *
     * @see HttpTimeout.HttpTimeoutCapabilityConfiguration.requestTimeoutMillis
     */
    public var apiHttpRequestTimeoutMillis: Long? = null


    /**
     * API请求中的超时请求配置。
     *
     * 如果 [apiHttpConnectTimeoutMillis]、[apiHttpConnectTimeoutMillis]、[apiHttpConnectTimeoutMillis] 都为null，则不会配置timeout。
     *
     * @see HttpTimeout.HttpTimeoutCapabilityConfiguration.connectTimeoutMillis
     */
    public var apiHttpConnectTimeoutMillis: Long? = null

    /**
     * API请求中的超时请求配置。
     *
     * 如果 [apiHttpSocketTimeoutMillis]、[apiHttpSocketTimeoutMillis]、[apiHttpSocketTimeoutMillis] 都为null，则不会配置timeout。
     *
     * @see HttpTimeout.HttpTimeoutCapabilityConfiguration.socketTimeoutMillis
     */
    public var apiHttpSocketTimeoutMillis: Long? = null

    /**
     * A configurer function for API requests [HttpClient].
     */
    public var apiClientConfigurer: ConfigurerFunction<HttpClientConfig<*>>? = null

    /**
     * config with [apiClientConfigurer].
     */
    public fun apiClientConfigurer(
        configurer: ConfigurerFunction<HttpClientConfig<*>>
    ): OneBotBotConfiguration =
        apply {
            apiClientConfigurer = apiClientConfigurer?.let { old ->
                ConfigurerFunction {
                    invokeBy(old)
                    invokeBy(configurer)
                }
            } ?: configurer
        }

    /**
     * 用于ws连接的 [HttpClient] 所使用的[引擎](https://ktor.io/docs/http-client-engines.html)。
     *
     * 如果 [wsClientEngine] 和 [wsClientEngineFactory] 都为null，则会尝试使用一个默认引擎。
     *
     */
    public var wsClientEngine: HttpClientEngine? = null

    /**
     * 用于ws连接的 [HttpClient] 所使用的[引擎](https://ktor.io/docs/http-client-engines.html) 工厂。
     *
     * 如果 [apiClientEngine] 和 [apiClientEngineFactory] 都为null，则会尝试使用一个默认引擎。
     *
     */
    public var wsClientEngineFactory: HttpClientEngineFactory<*>? = null

    /**
     * 每次尝试连接到 ws 服务时的最大重试次数。
     * 如果小于等于0则代表无限。
     * 默认为 `2147483647`。
     */
    public var wsConnectMaxRetryTimes: Int = Int.MAX_VALUE

    /**
     * 每次尝试连接到 ws 服务时，如果需要重新尝试，则每次尝试之间的等待时长，
     * 单位为毫秒。
     * 如果小于等于 `0` 则代表不等待。
     * 默认为 `3500`。
     *
     */
    public var wsConnectRetryDelayMillis: Long = 3500L

}
