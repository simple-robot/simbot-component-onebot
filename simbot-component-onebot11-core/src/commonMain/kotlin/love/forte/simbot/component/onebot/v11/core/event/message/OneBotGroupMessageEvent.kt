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

import love.forte.simbot.common.id.ID
import love.forte.simbot.component.onebot.v11.core.actor.OneBotGroup
import love.forte.simbot.component.onebot.v11.event.message.GroupMessageEvent
import love.forte.simbot.definition.Member
import love.forte.simbot.event.ChatGroupEvent
import love.forte.simbot.event.ChatGroupMessageEvent


/**
 * [群消息事件](https://github.com/botuniverse/onebot-11/blob/master/event/message.md#群消息)
 *
 * @see GroupMessageEvent
 *
 * @author ForteScarlet
 */
public interface OneBotGroupMessageEvent : OneBotMessageEvent, ChatGroupEvent {
    override val sourceEvent: GroupMessageEvent

    /**
     * 事件发生所在群
     */
    override suspend fun content(): OneBotGroup

    /**
     * 消息子类型，正常消息是 `normal`，匿名消息是 `anonymous`，
     * 系统提示（如「管理员已禁止群内匿名聊天」）是 `notice`
     */
    public val subType: String
        get() = sourceEvent.subType

    /**
     * 消息 ID
     */
    public val messageId: ID
        get() = sourceEvent.messageId

    /**
     * 群号
     */
    public val groupId: ID
        get() = sourceEvent.groupId

    /**
     * 发送者 QQ 号
     */
    public val userId: ID
        get() = sourceEvent.userId

    /**
     * 原始消息内容
     */
    public val rawMessage: String
        get() = sourceEvent.rawMessage
}

/**
 * 正常消息类型的 [OneBotGroupMessageEvent]。
 * 即 [subType] == `normal`
 */
public interface OneBotNormalGroupMessageEvent : OneBotGroupMessageEvent, ChatGroupMessageEvent {
    override suspend fun content(): OneBotGroup
    override suspend fun author(): Member // TODO OneBotMember?
}

/**
 * 匿名消息类型的 [OneBotGroupMessageEvent]。
 * 即 [subType] == `anonymous`
 */
public interface OneBotAnonymousGroupMessageEvent : OneBotGroupMessageEvent, ChatGroupMessageEvent {
    override suspend fun content(): OneBotGroup
    override suspend fun author(): Member // TODO OneBotAnonymousMember?
}

/**
 * 系统提示消息类型的 [OneBotGroupMessageEvent]。
 * 即 [subType] == `notice`
 */
public interface OneBotNoticeGroupMessageEvent : OneBotGroupMessageEvent {
    override suspend fun content(): OneBotGroup
}
