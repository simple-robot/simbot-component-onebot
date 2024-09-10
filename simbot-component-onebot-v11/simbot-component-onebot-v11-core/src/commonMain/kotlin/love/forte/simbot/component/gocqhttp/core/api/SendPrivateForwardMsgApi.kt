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
import love.forte.simbot.component.onebot.v11.core.api.OneBotApi
import love.forte.simbot.component.onebot.v11.core.api.OneBotApiResult
import love.forte.simbot.component.onebot.v11.core.api.OneBotMessageOutgoing
import love.forte.simbot.component.onebot.v11.message.segment.OneBotForwardNode
import kotlin.jvm.JvmStatic

/**
 * [`send_private_forward_msg`-发送合并转发 ( 好友 )](https://docs.go-cqhttp.org/api/#发送合并转发-好友)
 *
 * @author kuku
 */
public class SendPrivateForwardMsgApi private constructor(
    override val body: Any,
): OneBotApi<SendForwardMsgResult> {

    override val action: String
        get() = ACTION

    override val resultDeserializer: DeserializationStrategy<SendForwardMsgResult>
        get() = SendForwardMsgResult.serializer()

    override val apiResultDeserializer: DeserializationStrategy<OneBotApiResult<SendForwardMsgResult>>
        get() = RES_SER


    public companion object Factory {
        private const val ACTION: String = "send_private_forward_msg"

        private val RES_SER: KSerializer<OneBotApiResult<SendForwardMsgResult>> =
            OneBotApiResult.serializer(SendForwardMsgResult.serializer())

        /**
         * 构建一个 [SendPrivateForwardMsgApi].
         *
         * @param userId qq号
         * @param messages 自定义转发消息
         */
        @JvmStatic
        public fun create(userId: LongID, messages: List<OneBotForwardNode>): SendPrivateForwardMsgApi =
            SendPrivateForwardMsgApi(Body(userId, OneBotMessageOutgoing.create(messages)))
    }

    /**
     * @property userId 链接地址
     * @property messages 自定义转发消息
     */
    @Serializable
    internal data class Body(
        @SerialName("user_id")
        internal val userId: LongID,
        @SerialName("messages")
        internal val messages: OneBotMessageOutgoing
    )

}

/**
 * [SendPrivateForwardMsgApi] 的响应体。
 *
 * @property messageId 消息 ID
 */
@Serializable
public data class SendForwardMsgResult @ApiResultConstructor constructor(
    @SerialName("message_id")
    val messageId: LongID
)
