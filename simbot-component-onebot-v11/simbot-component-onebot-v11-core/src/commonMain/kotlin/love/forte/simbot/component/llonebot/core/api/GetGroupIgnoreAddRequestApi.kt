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
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.ListSerializer
import love.forte.simbot.common.id.LongID
import love.forte.simbot.component.onebot.common.annotations.ApiResultConstructor
import love.forte.simbot.component.onebot.v11.core.api.OneBotApi
import love.forte.simbot.component.onebot.v11.core.api.OneBotApiResult
import kotlin.jvm.JvmStatic

/**
 * [`get_group_ignore_add_request`-获取已过滤的加群通知](https://llonebot.github.io/zh-CN/develop/extends_api)
 *
 * @author kuku
 */
public class GetGroupIgnoreAddRequestApi private constructor(): OneBotApi<List<GetGroupIgnoreAddRequestResult>> {

    override val action: String
        get() = ACTION

    override val resultDeserializer: DeserializationStrategy<List<GetGroupIgnoreAddRequestResult>>
        get() = SER

    override val apiResultDeserializer: DeserializationStrategy<OneBotApiResult<List<GetGroupIgnoreAddRequestResult>>>
        get() = RES_SER

    override val body: Any?
        get() = null

    public companion object Factory {
        private const val ACTION: String = "get_group_ignore_add_request"

        private val SER = ListSerializer(GetGroupIgnoreAddRequestResult.serializer())

        private val RES_SER = OneBotApiResult.serializer(SER)

        private val INSTANCE: GetGroupIgnoreAddRequestApi = GetGroupIgnoreAddRequestApi()

        /**
         * 构建一个 [GetGroupIgnoreAddRequestApi].
         */
        @JvmStatic
        public fun create(): GetGroupIgnoreAddRequestApi = INSTANCE
    }

}

/**
 * [GetGroupIgnoreAddRequestApi] 的响应体。
 *
 * @property groupId 群号
 * @property userId qq号
 * @property flag
 */
@Serializable
public data class GetGroupIgnoreAddRequestResult @ApiResultConstructor constructor(
    @SerialName("group_id")
    val groupId: LongID,
    @SerialName("user_id")
    val userId: LongID,
    val flag: String
)
