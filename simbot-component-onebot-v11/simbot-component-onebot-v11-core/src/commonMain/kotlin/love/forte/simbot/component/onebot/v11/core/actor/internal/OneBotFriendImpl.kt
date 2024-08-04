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
import love.forte.simbot.component.onebot.v11.core.bot.internal.OneBotBotImpl
import love.forte.simbot.component.onebot.v11.core.internal.message.toReceipt
import love.forte.simbot.component.onebot.v11.core.utils.resolveToOneBotSegmentList
import love.forte.simbot.component.onebot.v11.core.utils.sendPrivateMsgApi
import love.forte.simbot.component.onebot.v11.core.utils.sendPrivateTextMsgApi
import love.forte.simbot.component.onebot.v11.event.message.RawPrivateMessageEvent
import love.forte.simbot.component.onebot.v11.message.OneBotMessageContent
import love.forte.simbot.component.onebot.v11.message.OneBotMessageReceipt
import love.forte.simbot.message.Message
import love.forte.simbot.message.MessageContent
import kotlin.coroutines.CoroutineContext

internal abstract class OneBotFriendImpl : OneBotFriend {
    protected abstract val bot: OneBotBotImpl

    override suspend fun send(text: String): OneBotMessageReceipt {
        return bot.executeData(
            sendPrivateTextMsgApi(
                target = id,
                text = text,
            )
        ).toReceipt(bot)
    }

    override suspend fun send(messageContent: MessageContent): OneBotMessageReceipt {
        if (messageContent is OneBotMessageContent) {
            return bot.executeData(
                sendPrivateMsgApi(
                    target = id,
                    message = messageContent.sourceSegments,
                )
            ).toReceipt(bot)
        }

        return send(messageContent.messages)
    }

    override suspend fun send(message: Message): OneBotMessageReceipt {
        return bot.executeData(
            sendPrivateMsgApi(
                target = id,
                message = message.resolveToOneBotSegmentList(bot)
            )
        ).toReceipt(bot)
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

