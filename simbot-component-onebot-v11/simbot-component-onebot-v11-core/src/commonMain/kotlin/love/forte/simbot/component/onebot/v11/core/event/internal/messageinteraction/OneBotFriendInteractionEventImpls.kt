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

import love.forte.simbot.component.onebot.v11.core.actor.OneBotFriend
import love.forte.simbot.component.onebot.v11.core.bot.OneBotBot
import love.forte.simbot.component.onebot.v11.core.event.messageinteraction.OneBotFriendPostSendEvent
import love.forte.simbot.component.onebot.v11.core.event.messageinteraction.OneBotFriendPreSendEvent
import love.forte.simbot.component.onebot.v11.core.event.messageinteraction.OneBotSegmentsInteractionMessage
import love.forte.simbot.component.onebot.v11.message.OneBotMessageReceipt

internal class OneBotFriendPreSendEventImpl(
    override val content: OneBotFriend,
    override val bot: OneBotBot,
    message: OneBotSegmentsInteractionMessage
) : AbstractMessagePreSendEventImpl(message), OneBotFriendPreSendEvent

internal class OneBotFriendPostSendEventImpl(
    override val content: OneBotFriend,
    override val bot: OneBotBot,
    override val receipt: OneBotMessageReceipt,
    override val message: OneBotSegmentsInteractionMessage,
) : AbstractMessagePostSendEventImpl(), OneBotFriendPostSendEvent
