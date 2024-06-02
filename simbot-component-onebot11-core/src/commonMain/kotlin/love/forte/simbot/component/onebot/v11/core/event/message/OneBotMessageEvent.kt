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

import love.forte.simbot.common.time.Timestamp
import love.forte.simbot.component.onebot.v11.core.bot.OneBotBot
import love.forte.simbot.component.onebot.v11.core.event.OneBotEvent
import love.forte.simbot.component.onebot.v11.core.utils.timestamp
import love.forte.simbot.component.onebot.v11.message.OneBotMessageContent
import love.forte.simbot.component.onebot.v11.message.OneBotMessageReceipt
import love.forte.simbot.component.onebot.v11.message.segment.OneBotReply
import love.forte.simbot.event.MessageEvent
import love.forte.simbot.message.Message
import love.forte.simbot.message.MessageContent
import love.forte.simbot.suspendrunner.ST

/**
 * OneBot11原始的消息事件结构体定义类型。
 */
public typealias OBSourceMessageEvent = love.forte.simbot.component.onebot.v11.event.message.MessageEvent

/**
 * OneBot组件中的消息相关事件。
 *
 * @author ForteScarlet
 */
public interface OneBotMessageEvent : OneBotEvent, MessageEvent {
    override val bot: OneBotBot
    override val sourceEvent: OBSourceMessageEvent

    override val time: Timestamp
        get() = sourceEvent.timestamp()

    /**
     * 基于本事件回复一条消息 [message]。
     *
     * 如果是在群聊中，可能会自动附加一个 [OneBotReply] 的效果。
     * 如果消息链中已经存在 [OneBotReply] 则不附加。
     *
     * @throws Exception 任何可能会在请求API时产生的异常，
     * 例如无权限（被禁言）、已经不在此群中、任意网络问题等。
     */
    @ST
    override suspend fun reply(message: Message): OneBotMessageReceipt

    /**
     * 基于本事件回复一条消息 [messageContent]。
     * 如果 [messageContent] 是 [OneBotMessageContent],
     * 则会直接使用其中的消息段，可简化解析过程。
     *
     * 如果是在群聊中，可能会自动附加一个 [OneBotReply] 的效果，
     * 如果消息链中已经存在 [OneBotReply] 则不附加。
     *
     * @throws Exception 任何可能会在请求API时产生的异常，
     * 例如无权限（被禁言）、已经不在此群中、任意网络问题等。
     */
    @ST
    override suspend fun reply(messageContent: MessageContent): OneBotMessageReceipt

    /**
     * 基于本事件回复一条文本消息。
     *
     * 如果是在群聊中，会自动附加一个 [OneBotReply] 的效果。
     *
     * @throws Exception 任何可能会在请求API时产生的异常，
     * 例如无权限（被禁言）、已经不在此群中、任意网络问题等。
     */
    @ST
    override suspend fun reply(text: String): OneBotMessageReceipt
}
