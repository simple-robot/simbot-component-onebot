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

package love.forte.simbot.component.onebot.v11.core.api

import io.ktor.client.statement.*
import io.ktor.http.*
import love.forte.simbot.suspendrunner.ST
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract
import kotlin.jvm.JvmInline


/**
 * 可以用于执行 [OneBotApi] 的执行器接口描述。
 *
 * _应仅由内部实现，第三方实现不保证稳定。_
 *
 * @since 1.1.0
 *
 * @author ForteScarlet
 */
public interface OneBotApiExecutable {
    /**
     * 使用当前 [OneBotApiExecutable] 执行 [api] 并得到原始的 [HttpResponse] 结果。
     *
     * 更多描述参考 [OneBotApi.request]
     *
     * @since 1.1.0
     *
     * @see OneBotApi.request
     */
    @ST
    public suspend fun execute(api: OneBotApi<*>): HttpResponse

    /**
     * 使用当前 [OneBotApiExecutable] 执行 [api] 并得到原始的 [String] 结果。
     *
     * 更多描述参考 [OneBotApi.requestRaw]
     *
     * @since 1.1.0
     *
     * @throws OneBotApiResponseNotSuccessException 如果响应状态码不是 2xx (参考 [HttpStatusCode.isSuccess])
     * @see OneBotApi.requestRaw
     */
    @ST
    public suspend fun executeRaw(api: OneBotApi<*>): String

    /**
     * 使用当前 [OneBotApiExecutable] 执行 [api] 并得到 [OneBotApiResult] 结果。
     *
     * 更多描述参考 [OneBotApi.requestResult]
     *
     * @since 1.1.0
     *
     * @throws OneBotApiResponseNotSuccessException 如果响应状态码不是 2xx (参考 [HttpStatusCode.isSuccess])
     * @see OneBotApi.requestResult
     */
    @ST
    public suspend fun <T : Any> executeResult(api: OneBotApi<T>): OneBotApiResult<T>

    /**
     * 使用当前 [OneBotApiExecutable] 执行 [api] 并得到 [T] 结果。
     *
     * 更多描述参考 [OneBotApi.requestData]
     *
     * @since 1.1.0
     *
     * @throws OneBotApiResponseNotSuccessException 如果响应状态码不是 2xx (参考 [HttpStatusCode.isSuccess])
     * @throws IllegalStateException 如果响应结果体的状态 [OneBotApiResult.retcode]
     * 不是成功或 [OneBotApiResult.data] 为 `null`
     * @see OneBotApi.requestData
     */
    @ST
    public suspend fun <T : Any> executeData(api: OneBotApi<T>): T
}

/**
 * 在 [OneBotApiExecutable] 的基础上提供更多作用域API，
 * 允许在 Kotlin 中使用DSL的风格请求API。
 *
 * @since 1.1.0
 *
 * @see OneBotApiExecutable
 * @see withExecutableScope
 * @see inExecutableScope
 */
@JvmInline
public value class OneBotApiExecutableScope(private val executable: OneBotApiExecutable) {
    /**
     * 使用当前 [OneBotApiExecutable] 执行 [OneBotApi] 并得到原始的 [HttpResponse] 结果。
     *
     * 更多描述参考 [OneBotApi.request]
     *
     * @since 1.1.0
     *
     * @throws OneBotApiResponseNotSuccessException 如果响应状态码不是 2xx (参考 [HttpStatusCode.isSuccess])
     * @see OneBotApi.request
     */
    public suspend fun OneBotApi<*>.execute(): HttpResponse =
        executable.execute(this)

    /**
     * 使用当前 [OneBotApiExecutable] 执行 [OneBotApi] 并得到原始的 [String] 结果。
     *
     * 更多描述参考 [OneBotApi.requestRaw]
     *
     * @since 1.1.0
     *
     * @throws OneBotApiResponseNotSuccessException 如果响应状态码不是 2xx (参考 [HttpStatusCode.isSuccess])
     * @see OneBotApi.requestRaw
     */
    public suspend fun OneBotApi<*>.executeRaw(): String =
        executable.executeRaw(this)

    /**
     * 使用当前 [OneBotApiExecutable] 执行 [OneBotApi] 并得到 [OneBotApiResult] 结果。
     *
     * 更多描述参考 [OneBotApi.requestResult]
     *
     * @since 1.1.0
     *
     * @throws OneBotApiResponseNotSuccessException 如果响应状态码不是 2xx (参考 [HttpStatusCode.isSuccess])
     * @see OneBotApi.requestResult
     */
    public suspend fun <T : Any> OneBotApi<T>.executeResult(): OneBotApiResult<T> =
        executable.executeResult(this)

    /**
     * 使用当前 [OneBotApiExecutable] 执行 [OneBotApi] 并得到 [T] 结果。
     *
     * 更多描述参考 [OneBotApi.requestData]
     *
     * @since 1.1.0
     *
     * @throws OneBotApiResponseNotSuccessException 如果响应状态码不是 2xx (参考 [HttpStatusCode.isSuccess])
     * @throws IllegalStateException 如果响应结果体的状态 [OneBotApiResult.retcode]
     * 不是成功或 [OneBotApiResult.data] 为 `null`
     * @see OneBotApi.requestData
     */
    public suspend fun <T : Any> OneBotApi<T>.executeData(): T =
        executable.executeData(this)
}

/**
 * 在 [OneBotApiExecutableScope] 的作用域下执行 [action]。
 *
 * ```kotlin
 * withExecutableScope(executable) {
 *     api.execute()
 * }
 * ```
 *
 * @since 1.1.0
 */
@OptIn(ExperimentalContracts::class)
public inline fun <T> withExecutableScope(
    executable: OneBotApiExecutable,
    action: OneBotApiExecutableScope.() -> T
): T {
    contract {
        callsInPlace(action, InvocationKind.EXACTLY_ONCE)
    }
    return OneBotApiExecutableScope(executable).action()
}

/**
 * 在 [OneBotApiExecutableScope] 的作用域下执行 [action]。
 *
 * ```kotlin
 * executable.inExecutableScope {
 *     api.execute()
 * }
 * ```
 *
 * @since 1.1.0
 */
@OptIn(ExperimentalContracts::class)
public inline fun <T> OneBotApiExecutable.inExecutableScope(
    action: OneBotApiExecutableScope.() -> T
): T {
    contract {
        callsInPlace(action, InvocationKind.EXACTLY_ONCE)
    }
    return withExecutableScope(this, action)
}
