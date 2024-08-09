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

package love.forte.simbot.component.onebot.v11.message

import love.forte.simbot.ability.DeleteOption
import love.forte.simbot.ability.StandardDeleteOption
import love.forte.simbot.common.id.ID
import love.forte.simbot.component.onebot.v11.message.segment.OneBotMessageSegment
import love.forte.simbot.component.onebot.v11.message.segment.OneBotMessageSegmentElement
import love.forte.simbot.component.onebot.v11.message.segment.OneBotReply
import love.forte.simbot.component.onebot.v11.message.segment.OneBotText
import love.forte.simbot.message.MessageContent
import love.forte.simbot.message.Messages
import love.forte.simbot.message.PlainText
import love.forte.simbot.suspendrunner.STP


/**
 * 事件中接收到的消息内容。
 *
 * @author ForteScarlet
 */
public interface OneBotMessageContent : MessageContent {
    /**
     * 消息的ID
     */
    override val id: ID

    /**
     * 接收到的消息事件中的原始消息元素列表。
     */
    public val sourceSegments: List<OneBotMessageSegment>

    /**
     * 接收到的所有消息元素。
     * OneBot中，接收到的元素大概率都会是 [OneBotMessageElement]，
     * 其中大部分内容大概率都是 [OneBotMessageSegmentElement]。
     *
     * 由 [sourceSegments] 解析而来。
     *
     * @see OneBotMessageSegmentElement
     */
    override val messages: Messages

    /**
     * 寻找 [messages] 中第一个 [OneBotReply] 类型的元素，
     * 如果没有则得到 `null`。
     * 寻找的过程是即时的，不会发生挂起。
     *
     * @since 1.2.0
     * @see messages
     * @see OneBotReply
     */
    @STP
    override suspend fun reference(): OneBotReply? =
        messages.firstNotNullOfOrNull { it as? OneBotReply }

    /**
     * 消息中所有的 [文本消息][PlainText]
     * (或者说 [sourceSegments] 中所有的 [OneBotText])
     * 的合并结果。
     *
     * 如果没有任何文本消息类型的消息段，则会得到 `null`。
     */
    override val plainText: String?

    /**
     * 删除此消息。
     *
     * 支持的操作：
     * - [StandardDeleteOption.IGNORE_ON_FAILURE] 忽略产生的异常
     *
     * @throws Exception 任何请求API过程中可能会产生的异常，
     * 例如因权限不足或消息不存在得到的请求错误
     */
    override suspend fun delete(vararg options: DeleteOption)
}

// 实现在core模块里，因为要用到API
