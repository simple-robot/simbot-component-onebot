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

package love.forte.simbot.component.onebot.v11.core.actor.internal

import love.forte.simbot.common.id.ID
import love.forte.simbot.component.onebot.v11.core.actor.OneBotFriend
import love.forte.simbot.component.onebot.v11.core.actor.OneBotStranger
import love.forte.simbot.component.onebot.v11.core.api.GetFriendListResult
import love.forte.simbot.component.onebot.v11.core.api.GetStrangerInfoApi
import love.forte.simbot.component.onebot.v11.core.api.SendLikeApi
import love.forte.simbot.component.onebot.v11.core.api.SendMsgResult
import love.forte.simbot.component.onebot.v11.core.bot.internal.OneBotBotImpl
import love.forte.simbot.component.onebot.v11.core.event.internal.messageinteraction.OneBotFriendPostSendEventImpl
import love.forte.simbot.component.onebot.v11.core.event.internal.messageinteraction.OneBotFriendPreSendEventImpl
import love.forte.simbot.component.onebot.v11.core.event.messageinteraction.OneBotSegmentsInteractionMessage
import love.forte.simbot.component.onebot.v11.core.event.messageinteraction.segmentsOrNull
import love.forte.simbot.component.onebot.v11.core.event.messageinteraction.toOneBotSegmentsInteractionMessage
import love.forte.simbot.component.onebot.v11.core.internal.message.toReceipt
import love.forte.simbot.component.onebot.v11.core.utils.*
import love.forte.simbot.component.onebot.v11.event.message.RawPrivateMessageEvent
import love.forte.simbot.component.onebot.v11.message.OneBotMessageReceipt
import love.forte.simbot.component.onebot.v11.message.segment.OneBotMessageSegment
import love.forte.simbot.event.InteractionMessage
import love.forte.simbot.message.Message
import love.forte.simbot.message.MessageContent
import love.forte.simbot.message.PlainText
import kotlin.coroutines.CoroutineContext

internal abstract class OneBotFriendImpl : OneBotFriend {
    protected abstract val bot: OneBotBotImpl

    override suspend fun send(text: String): OneBotMessageReceipt {
        val interactionMessage = OneBotSegmentsInteractionMessage(text = text)
        return interceptionAndSend(interactionMessage)
    }

    override suspend fun send(messageContent: MessageContent): OneBotMessageReceipt {
        val interactionMessage = OneBotSegmentsInteractionMessage(content = messageContent)
        return interceptionAndSend(interactionMessage)
    }

    override suspend fun send(message: Message): OneBotMessageReceipt {
        val interactionMessage = OneBotSegmentsInteractionMessage(message = message, bot = bot)
        return interceptionAndSend(interactionMessage)
    }

    private suspend fun interceptionAndSend(
        interactionMessage: OneBotSegmentsInteractionMessage
    ): OneBotMessageReceipt {
        val event = OneBotFriendPreSendEventImpl(
            this,
            bot,
            interactionMessage
        )

        val currentMessage = bot.emitMessagePreSendEventAndUseCurrentMessage(event)
        val segments = currentMessage.segmentsOrNull
        if (segments != null) {
            return sendSegments(segments).toReceipt(bot).alsoPostSend(currentMessage)
        }

        return sendByInteractionMessage(currentMessage).toReceipt(bot).alsoPostSend(currentMessage)
    }

    /**
     * 解析一个 [InteractionMessage] 为一个 [OneBotMessageSegment] 的列表并发送。
     * 始终认为 `segments` 为 `null`。
     */
    private suspend fun sendByInteractionMessage(interactionMessage: InteractionMessage): SendMsgResult {
        return resolveInteractionMessage(
            interactionMessage = interactionMessage,
            onSegments = { sendSegments(it) },
            onMessage = { sendMessage(it) },
            onText = { sendText(it) },
        )
    }

    private suspend fun sendText(text: String): SendMsgResult {
        return bot.executeData(
            sendPrivateTextMsgApi(
                target = id,
                text = text,
            )
        )
    }

    private suspend fun sendMessage(message: Message): SendMsgResult {
        return when (message) {
            is PlainText -> sendText(message.text)
            else -> sendSegments(message.resolveToOneBotSegmentList(bot))
        }
    }

    private suspend fun sendSegments(segments: List<OneBotMessageSegment>): SendMsgResult {
        return bot.executeData(
            sendPrivateMsgApi(
                target = id,
                message = segments,
            )
        )
    }

    private fun OneBotMessageReceipt.alsoPostSend(
        interactionMessage: InteractionMessage
    ): OneBotMessageReceipt = apply {
        val event = OneBotFriendPostSendEventImpl(
            content = this@OneBotFriendImpl,
            bot = bot,
            receipt = this,
            message = interactionMessage.toOneBotSegmentsInteractionMessage()
        )

        bot.pushEventAndLaunch(event)
    }

    override suspend fun sendLike(times: Int) {
        bot.executeData(
            SendLikeApi.create(
                userId = id,
                times = times
            )
        )
    }

    override suspend fun toStranger(): OneBotStranger =
        bot.executeData(
            GetStrangerInfoApi.create(userId = id)
        ).toStranger(bot)

    override fun toString(): String = "OneBotFriend(id=$id, bot=${bot.id})"
}

/**
 * 基于 [RawPrivateMessageEvent.Sender] 的 [OneBotFriend] 实现
 *
 */
internal class OneBotFriendEventSenderImpl(
    private val sender: RawPrivateMessageEvent.Sender,
    override val bot: OneBotBotImpl,
) : OneBotFriendImpl(), OneBotFriend {
    override val coroutineContext: CoroutineContext = bot.subContext

    override val id: ID
        get() = sender.userId

    override val name: String
        get() = sender.nickname
}

internal fun RawPrivateMessageEvent.Sender.toFriend(bot: OneBotBotImpl): OneBotFriendEventSenderImpl =
    OneBotFriendEventSenderImpl(this, bot)

internal class OneBotFriendApiResultImpl(
    private val result: GetFriendListResult,
    override val bot: OneBotBotImpl,
) : OneBotFriendImpl(), OneBotFriend {
    override val coroutineContext: CoroutineContext = bot.subContext

    override val name: String
        get() = result.nickname

    override val id: ID
        get() = result.userId
}

internal fun GetFriendListResult.toFriend(bot: OneBotBotImpl): OneBotFriendApiResultImpl =
    OneBotFriendApiResultImpl(this, bot)

