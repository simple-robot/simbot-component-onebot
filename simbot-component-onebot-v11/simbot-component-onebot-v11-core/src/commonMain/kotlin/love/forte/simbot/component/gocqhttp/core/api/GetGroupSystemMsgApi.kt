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
import kotlin.jvm.JvmStatic

/**
 * [`get_group_system_msg`-获取群系统消息](https://docs.go-cqhttp.org/api/#获取群系统消息)
 *
 * @author kuku
 */
public class GetGroupSystemMsgApi private constructor(
    override val body: Any?,
): OneBotApi<GetGroupSystemMsgResult> {

    override val action: String
        get() = ACTION

    override val resultDeserializer: DeserializationStrategy<GetGroupSystemMsgResult>
        get() = GetGroupSystemMsgResult.serializer()

    override val apiResultDeserializer: DeserializationStrategy<OneBotApiResult<GetGroupSystemMsgResult>>
        get() = RES_SER

    public companion object Factory {
        private const val ACTION: String = "get_group_system_msg"

        private val RES_SER: KSerializer<OneBotApiResult<GetGroupSystemMsgResult>> =
            OneBotApiResult.serializer(GetGroupSystemMsgResult.serializer())

        /**
         * 构建一个 [GetGroupSystemMsgApi].
         *
         */
        @JvmStatic
        public fun create(): GetGroupSystemMsgApi = GetGroupSystemMsgApi(null)
    }

}

/**
 * [GetGroupSystemMsgApi] 的响应体。
 *
 * @property invitedRequests 邀请消息列表
 * @property joinRequests 进群消息列表
 */
@Serializable
public data class GetGroupSystemMsgResult @ApiResultConstructor constructor(
    @SerialName("invited_requests")
    public val invitedRequests: List<InvitedRequest> = emptyList(),
    @SerialName("join_requests")
    public val joinRequests: List<JoinRequest> = emptyList(),
) {

    /**
     * 邀请消息
     * @property requestId 请求ID
     * @property invitorUin 邀请者
     * @property invitorNick 邀请者昵称
     * @property groupId 群号
     * @property groupName 群名
     * @property checked 是否已被处理
     * @property actor 处理者, 未处理为0
     */
    @Serializable
    public data class InvitedRequest(
        @SerialName("request_id")
        val requestId: Long,
        @SerialName("invitor_uin")
        val invitorUin: LongID,
        @SerialName("invitor_nick")
        val invitorNick: String,
        @SerialName("group_id")
        val groupId: LongID,
        @SerialName("group_name")
        val groupName: String,
        @SerialName("checked")
        val checked: Boolean,
        @SerialName("actor")
        val actor: LongID
    )

    /**
     * 邀请消息
     * @property requestId 请求ID
     * @property requesterUin 请求者ID
     * @property requesterNick 请求者昵称
     * @property message 验证消息
     * @property groupId 群号
     * @property groupName 群名
     * @property checked 是否已被处理
     * @property actor 处理者, 未处理为0
     */
    @Serializable
    public data class JoinRequest(
        @SerialName("request_id")
        val requestId: Long,
        @SerialName("requester_uin")
        val requesterUin: LongID,
        @SerialName("requester_nick")
        val requesterNick: String,
        @SerialName("message")
        val message: String,
        @SerialName("group_id")
        val groupId: LongID,
        @SerialName("group_name")
        val groupName: String,
        @SerialName("checked")
        val checked: Boolean,
        @SerialName("actor")
        val actor: LongID
    )

}
