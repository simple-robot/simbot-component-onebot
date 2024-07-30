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

@file:JvmName("OneBotBotRequests")
@file:JvmMultifileClass

package love.forte.simbot.component.onebot.v11.core.bot

import io.ktor.client.statement.*
import io.ktor.http.*
import love.forte.simbot.annotations.Api4J
import love.forte.simbot.component.onebot.v11.core.api.OneBotApi
import love.forte.simbot.component.onebot.v11.core.api.OneBotApiResponseNotSuccessException
import love.forte.simbot.component.onebot.v11.core.api.OneBotApiResult
import love.forte.simbot.component.onebot.v11.core.api.requestResult
import love.forte.simbot.suspendrunner.reserve.SuspendReserve
import love.forte.simbot.suspendrunner.reserve.suspendReserve
import love.forte.simbot.suspendrunner.runInAsync
import love.forte.simbot.suspendrunner.runInNoScopeBlocking
import java.util.concurrent.CompletableFuture
import kotlin.coroutines.EmptyCoroutineContext

//region blocking

/**
 * 使用 [bot] 对 [this] 发起一次阻塞地请求，
 *
 * 更多描述参考 [OneBotApi.requestBy]
 *
 *  @see requestBy
 */
@Api4J
@Deprecated(
    "Use OneBotBot.execute* API",
    ReplaceWith("bot.executeBlocking(this)")
)
public fun OneBotApi<*>.requestByBlocking(
    bot: OneBotBot,
): HttpResponse = runInNoScopeBlocking { bot.execute(this) }

/**
 * 使用 [bot] 对 [this] 发起一次阻塞地请求，
 *
 * 更多描述参考 [OneBotApi.requestRawBy]
 *
 * @throws OneBotApiResponseNotSuccessException 如果响应状态码不是 2xx (参考 [HttpStatusCode.isSuccess])
 * @see requestRawBy
 */
@Api4J
@Deprecated(
    "Use OneBotBot.execute* API",
    ReplaceWith("bot.executeRawBlocking(this)")
)
public fun OneBotApi<*>.requestRawByBlocking(
    bot: OneBotBot,
): String = runInNoScopeBlocking { bot.executeRaw(this) }

/**
 * 使用 [bot] 对 [this] 发起一次阻塞地请求，
 *
 * 更多描述参考 [OneBotApi.requestResultBy]
 *
 * 更多描述参考 [OneBotApi.requestResult]
 *
 * @throws OneBotApiResponseNotSuccessException 如果响应状态码不是 2xx (参考 [HttpStatusCode.isSuccess])
 * @see requestResultBy
 */
@Api4J
@Deprecated(
    "Use OneBotBot.execute* API",
    ReplaceWith("bot.executeResultBlocking(this)")
)
public fun <T : Any> OneBotApi<T>.requestResultByBlocking(
    bot: OneBotBot,
): OneBotApiResult<T> = runInNoScopeBlocking { bot.executeResult(this) }

/**
 * 使用 [bot] 对 [this] 发起一次阻塞地请求，
 *
 * 更多描述参考 [OneBotApi.requestDataBy]
 *
 * @throws OneBotApiResponseNotSuccessException 如果响应状态码不是 2xx (参考 [HttpStatusCode.isSuccess])
 * @throws IllegalStateException 如果响应结果体的状态 [OneBotApiResult.retcode]
 * 不是成功或 [OneBotApiResult.data] 为 `null`
 *
 * @see requestDataBy
 */
@Api4J
@Deprecated(
    "Use OneBotBot.execute* API",
    ReplaceWith("bot.executeDataBlocking(this)")
)
public fun <T : Any> OneBotApi<T>.requestDataByBlocking(
    bot: OneBotBot,
): T = runInNoScopeBlocking { bot.executeData(this) }
//endregion

//region async

/**
 * 使用 [bot] 对 [this] 发起一次异步地请求，
 *
 * 更多描述参考 [OneBotApi.requestBy]
 *
 *  @see requestBy
 */
@Api4J
@Deprecated(
    "Use OneBotBot.execute* API",
    ReplaceWith("bot.executeAsync(this)")
)
public fun OneBotApi<*>.requestByAsync(
    bot: OneBotBot,
): CompletableFuture<out HttpResponse> = runInAsync(bot) { bot.execute(this@requestByAsync) }

/**
 * 使用 [bot] 对 [this] 发起一次异步地请求，
 *
 * 更多描述参考 [OneBotApi.requestRawBy]
 *
 * @throws OneBotApiResponseNotSuccessException 如果响应状态码不是 2xx (参考 [HttpStatusCode.isSuccess])
 * @see requestRawBy
 */
@Api4J
@Deprecated(
    "Use OneBotBot.execute* API",
    ReplaceWith("bot.executeRawAsync(this)")
)
public fun OneBotApi<*>.requestRawByAsync(
    bot: OneBotBot,
): CompletableFuture<out String> = runInAsync(bot) { bot.executeRaw(this@requestRawByAsync) }

/**
 * 使用 [bot] 对 [this] 发起一次异步地请求，
 *
 * 更多描述参考 [OneBotApi.requestResultBy]
 *
 * 更多描述参考 [OneBotApi.requestResult]
 *
 * @throws OneBotApiResponseNotSuccessException 如果响应状态码不是 2xx (参考 [HttpStatusCode.isSuccess])
 * @see requestResultBy
 */
@Api4J
@Deprecated(
    "Use OneBotBot.execute* API",
    ReplaceWith("bot.executeResultAsync(this)")
)
public fun <T : Any> OneBotApi<T>.requestResultByAsync(
    bot: OneBotBot,
): CompletableFuture<out OneBotApiResult<T>> = runInAsync(bot) { bot.executeResult(this@requestResultByAsync) }

/**
 * 使用 [bot] 对 [this] 发起一次异步地请求，
 *
 * 更多描述参考 [OneBotApi.requestDataBy]
 *
 * @throws OneBotApiResponseNotSuccessException 如果响应状态码不是 2xx (参考 [HttpStatusCode.isSuccess])
 * @throws IllegalStateException 如果响应结果体的状态 [OneBotApiResult.retcode]
 * 不是成功或 [OneBotApiResult.data] 为 `null`
 *
 * @see requestDataBy
 */
@Api4J
@Deprecated(
    "Use OneBotBot.execute* API",
    ReplaceWith("bot.executeDataAsync(this)")
)
public fun <T : Any> OneBotApi<T>.requestDataByAsync(
    bot: OneBotBot,
): CompletableFuture<out T> = runInAsync(bot) { bot.executeData(this@requestDataByAsync) }
//endregion

//region reserve

/**
 * 使用 [bot] 对 [this] 发起一次预处理地请求，
 *
 * 更多描述参考 [OneBotApi.requestBy]
 *
 *  @see requestBy
 */
@Api4J
@Deprecated(
    "Use OneBotBot.execute* API",
    ReplaceWith("bot.executeReserve(this)")
)
public fun OneBotApi<*>.requestByReserve(
    bot: OneBotBot,
): SuspendReserve<HttpResponse> = suspendReserve(bot, EmptyCoroutineContext) { bot.execute(this) }

/**
 * 使用 [bot] 对 [this] 发起一次预处理地请求，
 *
 * 更多描述参考 [OneBotApi.requestRawBy]
 *
 * @throws OneBotApiResponseNotSuccessException 如果响应状态码不是 2xx (参考 [HttpStatusCode.isSuccess])
 * @see requestRawBy
 */
@Api4J
@Deprecated(
    "Use OneBotBot.execute* API",
    ReplaceWith("bot.executeRawReserve(this)")
)
public fun OneBotApi<*>.requestRawByReserve(
    bot: OneBotBot,
): SuspendReserve<String> = suspendReserve(bot, EmptyCoroutineContext) { bot.executeRaw(this) }

/**
 * 使用 [bot] 对 [this] 发起一次预处理地请求，
 *
 * 更多描述参考 [OneBotApi.requestResultBy]
 *
 * 更多描述参考 [OneBotApi.requestResult]
 *
 * @throws OneBotApiResponseNotSuccessException 如果响应状态码不是 2xx (参考 [HttpStatusCode.isSuccess])
 * @see requestResultBy
 */
@Api4J
@Deprecated(
    "Use OneBotBot.execute* API",
    ReplaceWith("bot.executeResultReserve(this)")
)
public fun <T : Any> OneBotApi<T>.requestResultByReserve(
    bot: OneBotBot,
): SuspendReserve<OneBotApiResult<T>> = suspendReserve(bot, EmptyCoroutineContext) { bot.executeResult(this) }

/**
 * 使用 [bot] 对 [this] 发起一次预处理地请求，
 *
 * 更多描述参考 [OneBotApi.requestDataBy]
 *
 * @throws OneBotApiResponseNotSuccessException 如果响应状态码不是 2xx (参考 [HttpStatusCode.isSuccess])
 * @throws IllegalStateException 如果响应结果体的状态 [OneBotApiResult.retcode]
 * 不是成功或 [OneBotApiResult.data] 为 `null`
 *
 * @see requestDataBy
 */
@Api4J
@Deprecated(
    "Use OneBotBot.execute* API",
    ReplaceWith("bot.executeDataReserve(this)")
)
public fun <T : Any> OneBotApi<T>.requestDataByReserve(
    bot: OneBotBot,
): SuspendReserve<T> = suspendReserve(bot, EmptyCoroutineContext) { bot.executeData(this) }
//endregion
