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
import love.forte.simbot.component.onebot.v11.core.actor.OneBotGroup
import love.forte.simbot.component.onebot.v11.core.actor.OneBotMember
import love.forte.simbot.component.onebot.v11.core.api.SendMsgApi
import love.forte.simbot.component.onebot.v11.core.bot.internal.OneBotBotImpl
import love.forte.simbot.component.onebot.v11.core.bot.requestDataBy
import love.forte.simbot.component.onebot.v11.core.event.message.OneBotFriendMessageEvent
import love.forte.simbot.component.onebot.v11.core.event.message.OneBotGroupPrivateMessageEvent
import love.forte.simbot.component.onebot.v11.core.event.message.OneBotPrivateMessageEvent
import love.forte.simbot.component.onebot.v11.core.internal.message.OneBotMessageContentImpl
import love.forte.simbot.component.onebot.v11.core.internal.message.toReceipt
import love.forte.simbot.component.onebot.v11.core.utils.sendMsgApi
import love.forte.simbot.component.onebot.v11.core.utils.sendTextMsgApi
import love.forte.simbot.component.onebot.v11.event.message.PrivateMessageEvent
import love.forte.simbot.component.onebot.v11.message.OneBotMessageContent
import love.forte.simbot.component.onebot.v11.message.OneBotMessageReceipt
import love.forte.simbot.definition.Contact
import love.forte.simbot.message.Message
import love.forte.simbot.message.MessageContent
import love.forte.simbot.message.MessageReceipt


internal abstract class OneBotPrivateMessageEventImpl(
    override val sourceEvent: PrivateMessageEvent
) : OneBotPrivateMessageEvent {
    override val id: ID
        get() = "${sourceEvent.userId}-${sourceEvent.messageId}".ID

    override val authorId: ID
        get() = sourceEvent.userId

    override val messageContent: MessageContent = OneBotMessageContentImpl(
        sourceEvent.messageId,
        sourceEvent.message
    )

    override suspend fun reply(text: String): OneBotMessageReceipt {
        val api = sendTextMsgApi(
            messageType = SendMsgApi.MESSAGE_TYPE_PRIVATE,
            target = sourceEvent.userId,
            text,
        )

        return api.requestDataBy(bot).toReceipt()
    }

    override suspend fun reply(messageContent: MessageContent): OneBotMessageReceipt {
        if (messageContent is OneBotMessageContent) {
            return sendMsgApi(
                messageType = SendMsgApi.MESSAGE_TYPE_PRIVATE,
                target = sourceEvent.userId,
                message = sourceEvent.message,
            ).requestDataBy(bot).toReceipt()
        }

        return reply(messageContent.messages)
    }

    override suspend fun reply(message: Message): OneBotMessageReceipt {
        TODO("Not yet implemented")
    }
}

internal class OneBotFriendMessageEventImpl(
    override val sourceEventRaw: String?,
    sourceEvent: PrivateMessageEvent,
    override val bot: OneBotBotImpl,
) : OneBotPrivateMessageEventImpl(sourceEvent),
    OneBotFriendMessageEvent {
    override suspend fun content(): Contact {
        TODO("Not yet implemented")
    }
}

internal class OneBotGroupPrivateMessageEventImpl(
    override val sourceEventRaw: String?,
    sourceEvent: PrivateMessageEvent,
    override val bot: OneBotBotImpl,
) : OneBotPrivateMessageEventImpl(sourceEvent),
    OneBotGroupPrivateMessageEvent {
    override suspend fun content(): OneBotMember {
        TODO("Not yet implemented")
    }

    override suspend fun source(): OneBotGroup {
        TODO("Not yet implemented")
    }
}

internal class OneBotDefaultPrivateMessageEventImpl(
    override val sourceEventRaw: String?,
    override val sourceEvent: PrivateMessageEvent,
    override val bot: OneBotBotImpl,
) : OneBotPrivateMessageEventImpl(sourceEvent)
