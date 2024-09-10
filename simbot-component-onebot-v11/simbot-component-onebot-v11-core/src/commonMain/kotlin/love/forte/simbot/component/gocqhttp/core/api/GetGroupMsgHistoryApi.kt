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
import love.forte.simbot.common.id.LongID
import love.forte.simbot.component.onebot.common.annotations.ApiResultConstructor
import love.forte.simbot.component.onebot.v11.core.api.GetMsgResult
import love.forte.simbot.component.onebot.v11.core.api.OneBotApi
import love.forte.simbot.component.onebot.v11.core.api.OneBotApiResult
import kotlin.jvm.JvmStatic

/**
 * [`get_group_msg_history`-获取群消息历史记录](https://docs.go-cqhttp.org/api/#获取群消息历史记录)
 *
 * @author kuku
 */
public class GetGroupMsgHistoryApi(
    override val body: Any,
): OneBotApi<GetGroupMsgHistoryResult> {

    override val action: String
        get() = ACTION

    override val resultDeserializer: DeserializationStrategy<GetGroupMsgHistoryResult>
        get() = GetGroupMsgHistoryResult.serializer()

    override val apiResultDeserializer: DeserializationStrategy<OneBotApiResult<GetGroupMsgHistoryResult>>
        get() = RES_SER


    public companion object Factory {
        private const val ACTION: String = "get_group_msg_history"

        private val RES_SER: KSerializer<OneBotApiResult<GetGroupMsgHistoryResult>> =
            OneBotApiResult.serializer(GetGroupMsgHistoryResult.serializer())

        /**
         * 构建一个 [GetGroupMsgHistoryApi].
         *
         * @param groupId 群号
         * @param messageSeq 起始消息序号, 可通过 get_msg 获得
         */
        @JvmStatic
        public fun create(groupId: LongID, messageSeq: LongID? = null): GetGroupMsgHistoryApi =
            GetGroupMsgHistoryApi(Body(messageSeq, groupId))
    }

    /**
     * @property messageSeq 起始消息序号, 可通过 get_msg 获得
     * @property groupId 群号
     */
    @Serializable
    internal data class Body(
        @SerialName("message_seq")
        internal val messageSeq: LongID? = null,
        @SerialName("group_id")
        internal val groupId: LongID
    )

}


/**
 * [GetGroupMsgHistoryApi] 的响应体。
 *
 * @property messages 从起始序号开始的前19条消息
 */
@Serializable
public data class GetGroupMsgHistoryResult @ApiResultConstructor constructor(
    val messages: List<GetMsgResult>
)
