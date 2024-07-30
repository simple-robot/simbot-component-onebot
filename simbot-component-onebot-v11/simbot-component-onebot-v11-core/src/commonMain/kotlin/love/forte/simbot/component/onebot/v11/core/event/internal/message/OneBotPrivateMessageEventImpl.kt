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

package love.forte.simbot.component.onebot.v11.core.event.internal.message

import love.forte.simbot.common.id.ID
import love.forte.simbot.common.id.StringID.Companion.ID
import love.forte.simbot.component.onebot.v11.core.actor.OneBotFriend
import love.forte.simbot.component.onebot.v11.core.actor.OneBotGroup
import love.forte.simbot.component.onebot.v11.core.actor.OneBotMember
import love.forte.simbot.component.onebot.v11.core.actor.internal.toFriend
import love.forte.simbot.component.onebot.v11.core.actor.internal.toMember
import love.forte.simbot.component.onebot.v11.core.bot.internal.OneBotBotImpl
import love.forte.simbot.component.onebot.v11.core.event.internal.eventToString
import love.forte.simbot.component.onebot.v11.core.event.message.OneBotFriendMessageEvent
import love.forte.simbot.component.onebot.v11.core.event.message.OneBotGroupPrivateMessageEvent
import love.forte.simbot.component.onebot.v11.core.event.message.OneBotPrivateMessageEvent
import love.forte.simbot.component.onebot.v11.core.internal.message.OneBotMessageContentImpl
import love.forte.simbot.component.onebot.v11.core.internal.message.toReceipt
import love.forte.simbot.component.onebot.v11.core.utils.resolveToOneBotSegmentList
import love.forte.simbot.component.onebot.v11.core.utils.sendPrivateMsgApi
import love.forte.simbot.component.onebot.v11.core.utils.sendPrivateTextMsgApi
import love.forte.simbot.component.onebot.v11.event.message.RawPrivateMessageEvent
import love.forte.simbot.component.onebot.v11.message.OneBotMessageContent
import love.forte.simbot.component.onebot.v11.message.OneBotMessageReceipt
import love.forte.simbot.message.Message
import love.forte.simbot.message.MessageContent


internal abstract class OneBotPrivateMessageEventImpl(
    final override val sourceEvent: RawPrivateMessageEvent,
    final override val bot: OneBotBotImpl,
) : OneBotPrivateMessageEvent {
    override val id: ID
        get() = "${sourceEvent.userId}-${sourceEvent.messageId}".ID

    override val authorId: ID
        get() = sourceEvent.userId

    override val messageContent: OneBotMessageContent = OneBotMessageContentImpl(
        sourceEvent.messageId,
        sourceEvent.message,
        bot
    )

    override suspend fun reply(text: String): OneBotMessageReceipt {
        val api = sendPrivateTextMsgApi(
            target = sourceEvent.userId,
            text,
        )

        return bot.executeData(api).toReceipt(bot)
    }

    override suspend fun reply(messageContent: MessageContent): OneBotMessageReceipt {
        if (messageContent is OneBotMessageContent) {
            return bot.executeData(
                sendPrivateMsgApi(
                    target = sourceEvent.userId,
                    message = sourceEvent.message,
                )
            ).toReceipt(bot)
        }

        return reply(messageContent.messages)
    }

    override suspend fun reply(message: Message): OneBotMessageReceipt {
        return bot.executeData(
            sendPrivateMsgApi(
                target = sourceEvent.userId,
                message = message.resolveToOneBotSegmentList(bot)
            )
        ).toReceipt(bot)
    }
}

internal class OneBotFriendMessageEventImpl(
    override val sourceEventRaw: String?,
    sourceEvent: RawPrivateMessageEvent,
    bot: OneBotBotImpl,
) : OneBotPrivateMessageEventImpl(sourceEvent, bot),
    OneBotFriendMessageEvent {
    override suspend fun content(): OneBotFriend {
        return sourceEvent.sender.toFriend(bot)
    }

    override fun toString(): String =
        eventToString("OneBotFriendMessageEvent")
}

internal class OneBotGroupPrivateMessageEventImpl(
    override val sourceEventRaw: String?,
    sourceEvent: RawPrivateMessageEvent,
    bot: OneBotBotImpl,
) : OneBotPrivateMessageEventImpl(sourceEvent, bot),
    OneBotGroupPrivateMessageEvent {
    override suspend fun content(): OneBotMember {
        return sourceEvent.sender.toMember(bot, groupId = null) // group id is unknown.
    }

    override suspend fun source(): OneBotGroup {
        // TODO 额，怎么知道群号？
        throw UnsupportedOperationException("The way to get the group id from PrivateMessageEvent is unknown yet.")
    }

    override fun toString(): String =
        eventToString("OneBotGroupPrivateMessageEvent")
}

internal class OneBotDefaultPrivateMessageEventImpl(
    override val sourceEventRaw: String?,
    sourceEvent: RawPrivateMessageEvent,
    bot: OneBotBotImpl,
) : OneBotPrivateMessageEventImpl(sourceEvent, bot) {

    override fun toString(): String =
        eventToString("OneBotDefaultPrivateMessageEvent")
}
