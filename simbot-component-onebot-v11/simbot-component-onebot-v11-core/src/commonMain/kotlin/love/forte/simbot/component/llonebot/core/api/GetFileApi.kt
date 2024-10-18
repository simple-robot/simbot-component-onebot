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

package love.forte.simbot.component.llonebot.core.api

import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import love.forte.simbot.component.gocqhttp.core.api.DownloadFileApi
import love.forte.simbot.component.onebot.common.annotations.ApiResultConstructor
import love.forte.simbot.component.onebot.v11.core.api.OneBotApi
import love.forte.simbot.component.onebot.v11.core.api.OneBotApiResult
import kotlin.jvm.JvmStatic

/**
 * [`get_file`-下载收到的群文件或私聊文件](https://llonebot.github.io/zh-CN/develop/extends_api)
 *
 * @author kuku
 */
public class GetFileApi private constructor(
    override val body: Any,
): OneBotApi<GetFileResult> {

    override val action: String
        get() = ACTION

    override val resultDeserializer: DeserializationStrategy<GetFileResult>
        get() = GetFileResult.serializer()

    override val apiResultDeserializer: DeserializationStrategy<OneBotApiResult<GetFileResult>>
        get() = RES_SER


    public companion object Factory {
        private const val ACTION: String = "get_file"

        private val RES_SER: KSerializer<OneBotApiResult<GetFileResult>> =
            OneBotApiResult.serializer(GetFileResult.serializer())

        /**
         * 构建一个 [DownloadFileApi].
         *
         * @param fileId 文件id
         */
        @JvmStatic
        public fun create(fileId: String): GetFileApi =
            GetFileApi(Body(fileId))
    }

    /**
     * @property fileId 文件id
     */
    @Serializable
    internal data class Body(
        @SerialName("file_id")
        internal val fileId: String
    )

}

/**
 * [GetFileApi] 的响应体。
 *
 * @property file 文件的绝对路径
 * @property fileName 文件名
 * @property fileSize 文件大小
 * @property base64 文件的 base64 编码, 需要在 LLOneBot 的配置文件中开启 base64
 */
@Serializable
public data class GetFileResult @ApiResultConstructor constructor(
    val file: String,
    @SerialName("file_name")
    val fileName: String,
    @SerialName("file_size")
    val fileSize: Int,
    val base64: String
)
