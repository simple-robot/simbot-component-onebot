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

package love.forte.simbot.component.gocqhttp.core.api

import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import love.forte.simbot.component.onebot.common.annotations.ApiResultConstructor
import love.forte.simbot.component.onebot.v11.core.api.OneBotApi
import love.forte.simbot.component.onebot.v11.core.api.OneBotApiResult
import kotlin.jvm.JvmOverloads
import kotlin.jvm.JvmStatic

/**
 * [`download_file`-下载文件到缓存目录](https://docs.go-cqhttp.org/api/#下载文件到缓存目录)
 *
 * @author kuku
 */
public class DownloadFileApi private constructor(
    override val body: Any,
): OneBotApi<DownloadFileResult> {

    override val action: String
        get() = ACTION

    override val resultDeserializer: DeserializationStrategy<DownloadFileResult>
        get() = DownloadFileResult.serializer()

    override val apiResultDeserializer: DeserializationStrategy<OneBotApiResult<DownloadFileResult>>
        get() = RES_SER


    public companion object Factory {
        private const val ACTION: String = "download_file"

        private val RES_SER: KSerializer<OneBotApiResult<DownloadFileResult>> =
            OneBotApiResult.serializer(DownloadFileResult.serializer())

        /**
         * 构建一个 [DownloadFileApi].
         *
         * @param url 链接地址
         * @param base64 base64编码的文件内容
         * @param threadCount 下载线程数
         * @param headers 自定义请求头
         */
        @JvmStatic
        @JvmOverloads
        public fun create(url: String? = null, base64: String? = null, threadCount: Int? = null, headers: String? = null): DownloadFileApi =
            DownloadFileApi(Body(url, base64, threadCount, headers))
    }

    /**
     * @property url 链接地址
     * @property threadCount 下载线程数
     * @property base64 base64编码的文件内容
     * @property headers 自定义请求头
     */
    @Serializable
    internal data class Body(
        @SerialName("url")
        internal val url: String? = null,
        @SerialName("base64")
        internal val base64: String? = null,
        @SerialName("thread_count")
        internal val threadCount: Int? = null,
        @SerialName("headers")
        internal val headers: String? = null
    )

}


/**
 * [DownloadFileApi] 的响应体。
 *
 * @property file 下载文件的绝对路径
 */
@Serializable
public data class DownloadFileResult @ApiResultConstructor constructor(
    public val file: String
)
