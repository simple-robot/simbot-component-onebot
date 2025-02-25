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

package love.forte.simbot.component.onebot.v11.core.event.internal.messageinteraction

import love.forte.simbot.annotations.ExperimentalSimbotAPI
import love.forte.simbot.common.atomic.atomic
import love.forte.simbot.common.id.ID
import love.forte.simbot.common.id.UUID
import love.forte.simbot.common.time.Timestamp
import love.forte.simbot.component.onebot.v11.core.actor.OneBotGroup
import love.forte.simbot.component.onebot.v11.core.bot.OneBotBot
import love.forte.simbot.component.onebot.v11.core.event.messageinteraction.OneBotGroupPostSendEvent
import love.forte.simbot.component.onebot.v11.core.event.messageinteraction.OneBotGroupPreSendEvent
import love.forte.simbot.component.onebot.v11.core.event.messageinteraction.OneBotSegmentsInteractionMessage
import love.forte.simbot.component.onebot.v11.message.OneBotMessageReceipt
import love.forte.simbot.event.InteractionMessage

internal class OneBotGroupPreSendEventImpl(
    override val content: OneBotGroup,
    override val bot: OneBotBot,
    override val message: OneBotSegmentsInteractionMessage
) : OneBotGroupPreSendEvent {
    override val id: ID = UUID.random()

    @OptIn(ExperimentalSimbotAPI::class)
    override val time: Timestamp = Timestamp.now()

    private val currentMessageUsed = atomic(false)

    private var _currentMessage: InteractionMessage = message

    override var currentMessage: InteractionMessage
        get() = _currentMessage
        set(value) {
            if (currentMessageUsed.value) {
                error("`currentMessage` has been used.")
            }
            _currentMessage = value
        }

    internal fun useCurrentMessage(): InteractionMessage {
        if (!currentMessageUsed.compareAndSet(false, true)) {
            error("`currentMessage` has been used.")
        }

        return _currentMessage
    }
}

internal class OneBotGroupPostSendEventImpl(
    override val content: OneBotGroup,
    override val bot: OneBotBot,
    override val receipt: OneBotMessageReceipt,
    override val message: OneBotSegmentsInteractionMessage
) : OneBotGroupPostSendEvent {
    override val id: ID = UUID.random()

    @OptIn(ExperimentalSimbotAPI::class)
    override val time: Timestamp = Timestamp.now()
}
