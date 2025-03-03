/*
 * Copyright (c) 2025. ForteScarlet.
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

package love.forte.simbot.component.onebot.v11.core.event.messageinteraction

import love.forte.simbot.event.FuzzyEventTypeImplementation
import love.forte.simbot.event.InternalMessageInteractionEvent
import love.forte.simbot.event.InternalMessagePostSendEvent
import love.forte.simbot.event.InternalMessagePreSendEvent
import love.forte.simbot.message.Message

/**
 * OneBot 组件中与 [Message] 交互有关的事件。
 *
 * @since 4.6.0
 *
 * @author ForteScarlet
 */
@SubclassOptInRequired(FuzzyEventTypeImplementation::class)
public interface OneBotInternalMessageInteractionEvent : InternalMessageInteractionEvent

/**
 * OneBot 组件针对消息发送前的拦截事件。
 *
 * @since 4.6.0
 *
 * @author ForteScarlet
 */
@SubclassOptInRequired(FuzzyEventTypeImplementation::class)
public interface OneBotInternalMessagePreSendEvent :
    OneBotInternalMessageInteractionEvent,
    InternalMessagePreSendEvent

/**
 * OneBot 组件针对消息发送后的通知事件。
 *
 * @since 4.6.0
 *
 * @author ForteScarlet
 */
@SubclassOptInRequired(FuzzyEventTypeImplementation::class)
public interface OneBotInternalMessagePostSendEvent :
    OneBotInternalMessageInteractionEvent,
    InternalMessagePostSendEvent {
    override val message: OneBotSegmentsInteractionMessage
}
