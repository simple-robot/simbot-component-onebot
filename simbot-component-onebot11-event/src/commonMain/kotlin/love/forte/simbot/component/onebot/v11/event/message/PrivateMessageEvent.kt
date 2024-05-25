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

package love.forte.simbot.component.onebot.v11.event.message

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import love.forte.simbot.common.id.ID
import love.forte.simbot.common.id.LongID
import love.forte.simbot.component.onebot.v11.event.ExpectEventType
import love.forte.simbot.component.onebot.v11.common.event.SourceEventConstructor
import love.forte.simbot.component.onebot.v11.message.segment.OneBotMessageSegment

/**
 * [私聊消息事件](https://github.com/botuniverse/onebot-11/blob/master/event/message.md#私聊消息)
 *
 * @property time 事件发生的时间戳
 * @property selfId 收到事件的机器人 QQ 号
 * @property postType 上报类型
 * @property messageType 消息类型
 * @property subType 消息子类型，如果是好友则是 `friend`，如果是群临时会话则是 `group`
 * @property messageId 消息 ID
 * @property userId 发送者 QQ 号
 * @property message 消息内容
 * @property rawMessage 原始消息内容
 * @property font 字体
 * @property sender 发送人信息
 */
@Serializable
@ExpectEventType(postType = MessageEvent.POST_TYPE, subType = "private")
public data class PrivateMessageEvent @SourceEventConstructor constructor(
    override val time: Long,
    @SerialName("self_id")
    override val selfId: LongID,
    @SerialName("post_type")
    override val postType: String,
    @SerialName("message_id")
    override val messageId: ID,
    @SerialName("message_type")
    override val messageType: String,
    @SerialName("sub_type")
    override val subType: String,
    override val message: List<OneBotMessageSegment> = emptyList(),
    @SerialName("raw_message")
    override val rawMessage: String,
    @SerialName("user_id")
    override val userId: LongID,
    override val font: Int?,
    override val sender: Sender
) : MessageEvent {
    /**
     * 私聊事件的发送人信息
     */
    @Serializable
    public data class Sender @SourceEventConstructor constructor(
        @SerialName("user_id")
        override val userId: LongID,
        override val nickname: String,
        override val sex: String = DEFAULT_SEX,
        override val age: Int = DEFAULT_AGE
    ) : MessageEvent.Sender
}
