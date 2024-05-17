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

package love.forte.simbot.component.onebot.v11.core.event.message

import love.forte.simbot.event.ContactMessageEvent


/**
 * [私聊消息事件](https://github.com/botuniverse/onebot-11/blob/master/event/message.md#私聊消息)
 *
 * @author ForteScarlet
 */
public interface OneBotPrivateMessageEvent : OneBotMessageEvent, ContactMessageEvent {
    /**
     * private 消息类型
     */
    public val messageType: String

    /**
     * friend、group、other	消息子类型。
     * 如果是好友则是 friend，如果是群临时会话则是 group
     */
    public val subType: String
}

/*
事件数据
字段名	数据类型	可能的值	说明
time	number (int64)	-	事件发生的时间戳
self_id	number (int64)	-	收到事件的机器人 QQ 号
post_type	string	message	上报类型

message_type	string	private	消息类型
sub_type	string	friend、group、other	消息子类型，如果是好友则是 friend，如果是群临时会话则是 group
message_id	number (int32)	-	消息 ID
user_id	number (int64)	-	发送者 QQ 号
message	message	-	消息内容
raw_message	string	-	原始消息内容
font	number (int32)	-	字体
sender	object	-	发送人信息
 */
