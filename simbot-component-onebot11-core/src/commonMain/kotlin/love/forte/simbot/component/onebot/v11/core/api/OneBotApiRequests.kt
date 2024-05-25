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

@file:JvmName("OneBotApiRequests")

package love.forte.simbot.component.onebot.v11.core.api

import io.ktor.client.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.utils.io.charsets.*
import love.forte.simbot.common.serialization.guessSerializer
import love.forte.simbot.component.onebot.v11.core.OneBot11
import kotlin.jvm.JvmName
import kotlin.jvm.JvmOverloads
import kotlin.jvm.JvmSynthetic

// TODO JVM async, blocking

// TODO logger

/**
 * 对 [this] 发起一次请求，并得到相应的 [HttpResponse] 响应。
 *
 * ### Body
 *
 * 当请求的 [OneBotApi.body] 不为 `null` 时，会做如下处理：
 * - 如果它是 [OutgoingContent] 类型或者字符串类型，直接使用。
 * - 如果提供的 [client] 中安装了 [ContentNegotiation] 插件，则直接使用。
 * - 否则，尝试使用 [guessSerializer] 获取到它的序列化器，然后使用 [OneBot11.DefaultJson]
 * 将其序列化为JSON字符串后赋值。这过程中如果出现异常，则会放弃，并最终尝试直接使用 [OneBotApi.body]。
 * - 上述一切均不符，则最终会抛出异常。
 *
 * @param client 用于请求的 [HttpClient].
 * @param host 用于请求的路径前缀。最终发起请求的完整地址为 [host] + [OneBotApi.action] + [actionSuffixes].
 * @param accessToken 参考 [鉴权](https://github.com/botuniverse/onebot-11/blob/master/communication/authorization.md)
 * 中涉及到 `access token` 请求头的内容 (`Authorization`)。
 * 如果不为 `null`，会追加前缀 `Bearer ` 并添加到请求头 `Authorization` 中。
 * @param actionSuffixes 会被拼接到 [OneBotApi.action] 的行为后缀，可参考 [OneBotApi.Actions].
 */
@JvmSynthetic
@JvmOverloads
public suspend fun OneBotApi<*>.request(
    client: HttpClient,
    host: Url,
    accessToken: String? = null,
    actionSuffixes: Collection<String>? = null,
): HttpResponse {
    return client.post {
        url {
            takeFrom(host)
            if (actionSuffixes?.isEmpty() != false) {
                appendPathSegments(action)
            } else {
                appendPathSegments(
                    buildString(action.length) {
                        append(action)
                        actionSuffixes.forEach { sf -> append(sf) }
                    }
                )
            }
        }

        headers {
            contentType(ContentType.Application.Json)
            accessToken?.also { bearerAuth(it) }
        }

        when (val b = this@request.body) {
            null -> {}
            is OutgoingContent, is String -> {
                setBody(b)
            }

            else -> {
                if (isContentNegotiationRuntimeAvailable &&
                    client.pluginOrNull(ContentNegotiation) != null
                ) {
                    setBody(b)
                } else {
                    try {
                        val json = OneBot11.DefaultJson
                        val serializer = guessSerializer(b, json.serializersModule)
                        val jsonText = json.encodeToString(serializer, b)
                        setBody(jsonText)
                    } catch (e: Throwable) {
                        try {
                            setBody(b)
                        } catch (e0: Throwable) {
                            e0.addSuppressed(e)
                            throw e0
                        }
                    }
                }
            }
        }
    }
}

/**
 * 对 [this] 发起一次请求，并得到相应的 [HttpResponse] 响应。
 *
 * ### Body
 *
 * 当请求的 [OneBotApi.body] 不为 `null` 时，会做如下处理：
 * - 如果它是 [OutgoingContent] 类型或者字符串类型，直接使用。
 * - 如果提供的 [client] 中安装了 [ContentNegotiation] 插件，则直接使用。
 * - 否则，尝试使用 [guessSerializer] 获取到它的序列化器，然后使用 [OneBot11.DefaultJson]
 * 将其序列化为JSON字符串后赋值。这过程中如果出现异常，则会放弃，并最终尝试直接使用 [OneBotApi.body]。
 * - 上述一切均不符，则最终会抛出异常。
 *
 * @param client 用于请求的 [HttpClient].
 * @param host 用于请求的路径前缀。最终发起请求的完整地址为 [host] + [OneBotApi.action] + [actionSuffixes].
 * @param actionSuffixes 会被拼接到 [OneBotApi.action] 的行为后缀，可参考 [OneBotApi.Actions].
 */
@JvmSynthetic
@JvmOverloads
public suspend fun OneBotApi<*>.request(
    client: HttpClient,
    host: String,
    accessToken: String? = null,
    actionSuffixes: Collection<String>? = null,
): HttpResponse = request(client, Url(host), accessToken, actionSuffixes)

/**
 * 对 [this] 发起一次请求，并得到响应体的字符串内容。
 *
 * 更多描述参考 [OneBotApi.request].
 *
 * @throws OneBotApiResponseNotSuccessException 如果响应状态码不是 2xx (参考 [HttpStatusCode.isSuccess])
 * @see OneBotApi.request
 *
 */
@JvmSynthetic
@JvmOverloads
public suspend fun OneBotApi<*>.requestRaw(
    client: HttpClient,
    host: Url,
    accessToken: String? = null,
    actionSuffixes: Collection<String>? = null,
    charset: Charset = Charsets.UTF_8
): String {
    val response = request(client, host, accessToken, actionSuffixes)
    val status = response.status
    if (!status.isSuccess()) {
        throw OneBotApiResponseNotSuccessException(status)
    }
    return response.bodyAsText(charset)
}

/**
 * 对 [this] 发起一次请求，并得到响应体的字符串内容。
 *
 * 更多描述参考 [OneBotApi.request].
 *
 * @throws OneBotApiResponseNotSuccessException 如果响应状态码不是 2xx (参考 [HttpStatusCode.isSuccess])
 * @see OneBotApi.request
 *
 */
@JvmSynthetic
@JvmOverloads
public suspend fun OneBotApi<*>.requestRaw(
    client: HttpClient,
    host: String,
    accessToken: String? = null,
    actionSuffixes: Collection<String>? = null,
    charset: Charset = Charsets.UTF_8
): String = requestRaw(client, Url(host), accessToken, actionSuffixes, charset)

/**
 * 对 [this] 发起一次请求，并得到响应体的 [OneBotApiResult] 结果。
 *
 * 更多描述参考 [OneBotApi.request].
 *
 * @throws OneBotApiResponseNotSuccessException 如果响应状态码不是 2xx (参考 [HttpStatusCode.isSuccess])
 * @see OneBotApi.request
 */
@JvmSynthetic
@JvmOverloads
public suspend fun <T : Any> OneBotApi<T>.requestResult(
    client: HttpClient,
    host: Url,
    accessToken: String? = null,
    actionSuffixes: Collection<String>? = null,
    charset: Charset = Charsets.UTF_8,
): OneBotApiResult<T> {
    val raw = requestRaw(client, host, accessToken, actionSuffixes, charset)
    return OneBot11.DefaultJson.decodeFromString(apiResultDeserializer, raw)
}

/**
 * 对 [this] 发起一次请求，并得到响应体的 [OneBotApiResult] 结果。
 *
 * 更多描述参考 [OneBotApi.request].
 *
 * @throws OneBotApiResponseNotSuccessException 如果响应状态码不是 2xx (参考 [HttpStatusCode.isSuccess])
 * @see OneBotApi.request
 */
@JvmSynthetic
@JvmOverloads
public suspend fun <T : Any> OneBotApi<T>.requestResult(
    client: HttpClient,
    host: String,
    accessToken: String? = null,
    actionSuffixes: Collection<String>? = null,
    charset: Charset = Charsets.UTF_8
): OneBotApiResult<T> = requestResult(client, Url(host), accessToken, actionSuffixes, charset)

/**
 * 对 [this] 发起一次请求，并得到响应体的 [T] 类型结果。
 *
 * 更多描述参考 [OneBotApi.request].
 *
 * @throws OneBotApiResponseNotSuccessException 如果响应状态码不是 2xx (参考 [HttpStatusCode.isSuccess])
 * @throws IllegalStateException 如果响应结果体的状态 [OneBotApiResult.retcode]
 * 不是成功或 [OneBotApiResult.data] 为 `null`
 * @see OneBotApi.request
 */
@JvmSynthetic
@JvmOverloads
public suspend fun <T : Any> OneBotApi<T>.requestData(
    client: HttpClient,
    host: Url,
    accessToken: String? = null,
    actionSuffixes: Collection<String>? = null,
    charset: Charset = Charsets.UTF_8,
): T {
    val result = requestResult(client, host, accessToken, actionSuffixes, charset)
    return result.dataOrThrow
}

/**
 * 对 [this] 发起一次请求，并得到响应体的 [T] 结果。
 *
 * 更多描述参考 [OneBotApi.request].
 *
 * @throws OneBotApiResponseNotSuccessException 如果响应状态码不是 2xx (参考 [HttpStatusCode.isSuccess])
 * @throws IllegalStateException 如果响应结果体的状态 [OneBotApiResult.retcode]
 * 不是成功或 [OneBotApiResult.data] 为 `null`
 * @see OneBotApi.request
 */
@JvmSynthetic
@JvmOverloads
public suspend fun <T : Any> OneBotApi<T>.requestData(
    client: HttpClient,
    host: String,
    accessToken: String? = null,
    actionSuffixes: Collection<String>? = null,
    charset: Charset = Charsets.UTF_8
): T = requestData(client, Url(host), accessToken, actionSuffixes, charset)


/**
 * 判断在运行时是否可以直接使用 [ContentNegotiation] 类型。
 * 在JVM中默认为仅编译期可用，因此在JVM平台下需要通过类加载校验一下；
 * 其他平台始终得到 `true`。
 */
internal expect val isContentNegotiationRuntimeAvailable: Boolean
