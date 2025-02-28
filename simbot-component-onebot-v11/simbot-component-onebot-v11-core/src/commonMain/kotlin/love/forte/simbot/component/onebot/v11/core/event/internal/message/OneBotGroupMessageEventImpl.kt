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
import love.forte.simbot.component.onebot.v11.core.actor.internal.toMember
import love.forte.simbot.component.onebot.v11.core.api.SendMsgResult
import love.forte.simbot.component.onebot.v11.core.bot.internal.OneBotBotImpl
import love.forte.simbot.component.onebot.v11.core.event.internal.eventToString
import love.forte.simbot.component.onebot.v11.core.event.internal.messageinteraction.*
import love.forte.simbot.component.onebot.v11.core.event.message.OneBotAnonymousGroupMessageEvent
import love.forte.simbot.component.onebot.v11.core.event.message.OneBotGroupMessageEvent
import love.forte.simbot.component.onebot.v11.core.event.message.OneBotNormalGroupMessageEvent
import love.forte.simbot.component.onebot.v11.core.event.message.OneBotNoticeGroupMessageEvent
import love.forte.simbot.component.onebot.v11.core.event.messageinteraction.OneBotSegmentsInteractionMessage
import love.forte.simbot.component.onebot.v11.core.event.messageinteraction.OnebotGroupMessageEventPostReplyEvent
import love.forte.simbot.component.onebot.v11.core.event.messageinteraction.segmentsOrNull
import love.forte.simbot.component.onebot.v11.core.event.messageinteraction.toOneBotSegmentsInteractionMessage
import love.forte.simbot.component.onebot.v11.core.internal.message.OneBotMessageContentImpl
import love.forte.simbot.component.onebot.v11.core.internal.message.toReceipt
import love.forte.simbot.component.onebot.v11.core.utils.*
import love.forte.simbot.component.onebot.v11.event.message.RawGroupMessageEvent
import love.forte.simbot.component.onebot.v11.message.OneBotMessageContent
import love.forte.simbot.component.onebot.v11.message.OneBotMessageReceipt
import love.forte.simbot.component.onebot.v11.message.segment.OneBotMessageSegment
import love.forte.simbot.event.InteractionMessage
import love.forte.simbot.message.Message
import love.forte.simbot.message.MessageContent
import love.forte.simbot.message.PlainText

internal abstract class OneBotGroupMessageEventImpl(
    sourceEvent: RawGroupMessageEvent,
    final override val bot: OneBotBotImpl
) : OneBotGroupMessageEvent {

    override val id: ID
        get() = "${sourceEvent.groupId}-${sourceEvent.messageId}".ID

    override val authorId: ID
        get() = sourceEvent.userId

    override val messageContent: OneBotMessageContent = OneBotMessageContentImpl(
        sourceEvent.messageId,
        sourceEvent.message,
        bot
    )

    override suspend fun content(): OneBotGroup {
        return bot.groupRelation.group(sourceEvent.groupId)
            ?: error("Group with id $groupId is not found")
    }

    override suspend fun reply(text: String): OneBotMessageReceipt {
        val interactionMessage = OneBotSegmentsInteractionMessage(text = text)
        return interceptionAndReply(interactionMessage)
    }

    override suspend fun reply(messageContent: MessageContent): OneBotMessageReceipt {
        val interactionMessage = OneBotSegmentsInteractionMessage(content = messageContent)
        return interceptionAndReply(interactionMessage)
    }

    override suspend fun reply(message: Message): OneBotMessageReceipt {
        val interactionMessage = OneBotSegmentsInteractionMessage(message = message, bot = bot)
        return interceptionAndReply(interactionMessage)
    }

    protected abstract fun preReplyEvent(interactionMessage: OneBotSegmentsInteractionMessage):
        AbstractMessagePreSendEventImpl

    private suspend fun interceptionAndReply(
        interactionMessage: OneBotSegmentsInteractionMessage
    ): OneBotMessageReceipt {
        val event = preReplyEvent(interactionMessage)

        val currentMessage = bot.emitMessagePreSendEventAndUseCurrentMessage(event)
        val segments = currentMessage.segmentsOrNull
        if (segments != null) {
            return replySegments(segments).toReceipt(bot).alsoPostReply(currentMessage)
        }

        return replyByInteractionMessage(currentMessage).toReceipt(bot).alsoPostReply(currentMessage)
    }

    /**
     * 解析一个 [InteractionMessage] 为一个 [OneBotMessageSegment] 的列表并发送。
     * 始终认为 `segments` 为 `null`。
     */
    private suspend fun replyByInteractionMessage(interactionMessage: InteractionMessage): SendMsgResult {
        return resolveInteractionMessage(
            interactionMessage = interactionMessage,
            onSegments = { replySegments(it) },
            onMessage = { replyMessage(it) },
            onText = { replyText(it) },
        )
    }

    private suspend fun replyText(text: String): SendMsgResult {
        return bot.executeData(
            sendGroupTextMsgApi(
                target = sourceEvent.groupId,
                text,
                reply = sourceEvent.messageId
            )
        )
    }

    private suspend fun replyMessage(message: Message): SendMsgResult {
        return when (message) {
            is PlainText -> replyText(message.text)
            else -> replySegments(message.resolveToOneBotSegmentList(bot))
        }
    }

    private suspend fun replySegments(segments: List<OneBotMessageSegment>): SendMsgResult {
        return bot.executeData(
            sendGroupMsgApi(
                target = sourceEvent.groupId,
                message = segments,
                reply = sourceEvent.messageId
            )
        )
    }

    protected abstract fun OneBotMessageReceipt.postReplyEvent(interactionMessage: InteractionMessage):
        OnebotGroupMessageEventPostReplyEvent

    private fun OneBotMessageReceipt.alsoPostReply(
        interactionMessage: InteractionMessage
    ): OneBotMessageReceipt = apply {
        val event = postReplyEvent(interactionMessage)
        bot.pushEventAndLaunch(event)
    }
}

internal class OneBotNormalGroupMessageEventImpl(
    override val sourceEventRaw: String?,
    override val sourceEvent: RawGroupMessageEvent,
    bot: OneBotBotImpl,
) : OneBotGroupMessageEventImpl(sourceEvent, bot),
    OneBotNormalGroupMessageEvent {
    override suspend fun author(): OneBotMember {
        return sourceEvent.sender.toMember(bot, sourceEvent.groupId, sourceEvent.anonymous)
    }

    override fun preReplyEvent(interactionMessage: OneBotSegmentsInteractionMessage): AbstractMessagePreSendEventImpl {
        return OneBotNormalGroupMessageEventPreReplyEventImpl(
            bot,
            this,
            interactionMessage
        )
    }

    override fun OneBotMessageReceipt.postReplyEvent(interactionMessage: InteractionMessage):
        OnebotGroupMessageEventPostReplyEvent {
        return OneBotNormalGroupMessageEventPostReplyEventImpl(
            bot,
            content = this@OneBotNormalGroupMessageEventImpl,
            receipt = this,
            message = interactionMessage.toOneBotSegmentsInteractionMessage()
        )
    }

    override fun toString(): String = eventToString("OneBotNormalGroupMessageEvent")
}


internal class OneBotAnonymousGroupMessageEventImpl(
    override val sourceEventRaw: String?,
    override val sourceEvent: RawGroupMessageEvent,
    bot: OneBotBotImpl,
) : OneBotGroupMessageEventImpl(sourceEvent, bot),
    OneBotAnonymousGroupMessageEvent {
    override suspend fun author(): OneBotMember {
        return sourceEvent.sender.toMember(bot, sourceEvent.groupId, sourceEvent.anonymous)
    }

    override fun preReplyEvent(interactionMessage: OneBotSegmentsInteractionMessage): AbstractMessagePreSendEventImpl {
        return OneBotAnonymousGroupMessageEventPreReplyEventImpl(
            bot,
            this,
            interactionMessage
        )
    }

    override fun OneBotMessageReceipt.postReplyEvent(interactionMessage: InteractionMessage):
        OnebotGroupMessageEventPostReplyEvent {
        return OneBotAnonymousGroupMessageEventPostReplyEventImpl(
            bot,
            content = this@OneBotAnonymousGroupMessageEventImpl,
            receipt = this,
            message = interactionMessage.toOneBotSegmentsInteractionMessage()
        )
    }

    override fun toString(): String = eventToString("OneBotAnonymousGroupMessageEvent")
}

internal class OneBotNoticeGroupMessageEventImpl(
    override val sourceEventRaw: String?,
    override val sourceEvent: RawGroupMessageEvent,
    bot: OneBotBotImpl,
) : OneBotGroupMessageEventImpl(sourceEvent, bot),
    OneBotNoticeGroupMessageEvent {
    override fun preReplyEvent(interactionMessage: OneBotSegmentsInteractionMessage): AbstractMessagePreSendEventImpl {
        return OneBotNoticeGroupMessageEventPreReplyEventImpl(
            bot,
            this,
            interactionMessage
        )
    }

    override fun OneBotMessageReceipt.postReplyEvent(interactionMessage: InteractionMessage):
        OnebotGroupMessageEventPostReplyEvent {
        return OneBotNoticeGroupMessageEventPostReplyEventImpl(
            bot,
            content = this@OneBotNoticeGroupMessageEventImpl,
            receipt = this,
            message = interactionMessage.toOneBotSegmentsInteractionMessage()
        )
    }

    override fun toString(): String = eventToString("OneBotNoticeGroupMessageEvent")
}


internal class OneBotDefaultGroupMessageEventImpl(
    override val sourceEventRaw: String?,
    override val sourceEvent: RawGroupMessageEvent,
    bot: OneBotBotImpl,
) : OneBotGroupMessageEventImpl(sourceEvent, bot) {
    override fun preReplyEvent(interactionMessage: OneBotSegmentsInteractionMessage): AbstractMessagePreSendEventImpl {
        return OneBotDefaultGroupMessageEventPreReplyEventImpl(
            bot,
            this,
            interactionMessage
        )
    }

    override fun OneBotMessageReceipt.postReplyEvent(interactionMessage: InteractionMessage):
        OnebotGroupMessageEventPostReplyEvent {
        return OneBotDefaultGroupMessageEventPostReplyEventImpl(
            bot,
            content = this@OneBotDefaultGroupMessageEventImpl,
            receipt = this,
            message = interactionMessage.toOneBotSegmentsInteractionMessage()
        )
    }

    override fun toString(): String = eventToString("OneBotDefaultGroupMessageEvent")
}
