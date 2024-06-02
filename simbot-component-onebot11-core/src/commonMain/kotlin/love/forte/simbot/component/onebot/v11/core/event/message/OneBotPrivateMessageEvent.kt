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

import love.forte.simbot.component.onebot.v11.core.actor.OneBotFriend
import love.forte.simbot.component.onebot.v11.core.actor.OneBotGroup
import love.forte.simbot.component.onebot.v11.core.actor.OneBotMember
import love.forte.simbot.component.onebot.v11.event.message.PrivateMessageEvent
import love.forte.simbot.event.ContactMessageEvent
import love.forte.simbot.event.MemberMessageEvent
import love.forte.simbot.suspendrunner.STP


/**
 * [私聊消息事件](https://github.com/botuniverse/onebot-11/blob/master/event/message.md#私聊消息)
 *
 * @see PrivateMessageEvent
 *
 * @author ForteScarlet
 */
public interface OneBotPrivateMessageEvent : OneBotMessageEvent {
    override val sourceEvent: PrivateMessageEvent

    /**
     * private 消息类型
     */
    public val messageType: String
        get() = sourceEvent.messageType

    /**
     * friend、group、other	消息子类型。
     * 如果是好友则是 friend，如果是群临时会话则是 group
     */
    public val subType: String
        get() = sourceEvent.subType
}

/**
 * 一个代表好友私聊消息的 [OneBotPrivateMessageEvent]。
 * 即 [subType] == `friend`。
 */
@STP
public interface OneBotFriendMessageEvent : OneBotPrivateMessageEvent, ContactMessageEvent {
    override suspend fun content(): OneBotFriend
}

/**
 * 一个代表群成员私聊的临时消息（群临时会话）的 [OneBotPrivateMessageEvent]。
 * 即 [subType] == `group`。
 */
@STP
public interface OneBotGroupPrivateMessageEvent : OneBotPrivateMessageEvent, MemberMessageEvent {

    /**
     * 发起会话的成员
     */
    override suspend fun content(): OneBotMember

    /**
     * [content] 的所属群
     */
    override suspend fun source(): OneBotGroup
}
