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
import love.forte.simbot.component.onebot.v11.core.actor.OneBotMember
import love.forte.simbot.component.onebot.v11.core.actor.OneBotMemberRole
import love.forte.simbot.component.onebot.v11.core.api.GetGroupMemberInfoResult
import love.forte.simbot.component.onebot.v11.core.bot.internal.OneBotBotImpl
import love.forte.simbot.component.onebot.v11.core.bot.requestDataBy
import love.forte.simbot.component.onebot.v11.core.internal.message.toReceipt
import love.forte.simbot.component.onebot.v11.core.utils.sendPrivateMsgApi
import love.forte.simbot.component.onebot.v11.core.utils.sendPrivateTextMsgApi
import love.forte.simbot.component.onebot.v11.event.message.GroupMessageEvent
import love.forte.simbot.component.onebot.v11.event.message.PrivateMessageEvent
import love.forte.simbot.component.onebot.v11.message.OneBotMessageContent
import love.forte.simbot.component.onebot.v11.message.OneBotMessageReceipt
import love.forte.simbot.component.onebot.v11.message.resolveToOneBotSegmentList
import love.forte.simbot.message.Message
import love.forte.simbot.message.MessageContent
import kotlin.coroutines.CoroutineContext


internal abstract class OneBotMemberImpl : OneBotMember {
    protected abstract val bot: OneBotBotImpl

    override suspend fun send(text: String): OneBotMessageReceipt {
        return sendPrivateTextMsgApi(
            target = id,
            text = text,
        ).requestDataBy(bot).toReceipt()
    }

    override suspend fun send(messageContent: MessageContent): OneBotMessageReceipt {
        if (messageContent is OneBotMessageContent) {
            return sendPrivateMsgApi(
                target = id,
                message = messageContent.sourceSegments,
            ).requestDataBy(bot).toReceipt()
        }

        return send(messageContent.messages)
    }

    override suspend fun send(message: Message): OneBotMessageReceipt {
        return sendPrivateMsgApi(
            target = id,
            message = message.resolveToOneBotSegmentList()
        ).requestDataBy(bot).toReceipt()
    }

    override fun toString(): String = "OneBotMember(id=$id, bot=${bot.id})"
}

internal class OneBotMemberPrivateMessageEventSenderImpl(
    private val source: PrivateMessageEvent.Sender,
    override val bot: OneBotBotImpl,
    override val nick: String?,
    override val role: OneBotMemberRole?,
) : OneBotMemberImpl() {
    override val coroutineContext: CoroutineContext = bot.subContext

    override val id: ID
        get() = source.userId

    override val name: String
        get() = source.nickname
}

internal class OneBotMemberGroupMessageEventSenderImpl(
    private val source: GroupMessageEvent.Sender,
    override val bot: OneBotBotImpl,
) : OneBotMemberImpl() {
    override val coroutineContext: CoroutineContext = bot.subContext

    override val id: ID
        get() = source.userId

    override val name: String
        get() = source.nickname

    override val nick: String?
        get() = source.card.takeIf { it.isNotEmpty() }

    override val role: OneBotMemberRole
        get() = OneBotMemberRole.valueOfLenient(source.role)
            ?: OneBotMemberRole.MEMBER
}

internal fun PrivateMessageEvent.Sender.toMember(
    bot: OneBotBotImpl,
    nick: String? = null,
    role: OneBotMemberRole? = null,
): OneBotMemberPrivateMessageEventSenderImpl =
    OneBotMemberPrivateMessageEventSenderImpl(
        source = this,
        bot = bot,
        nick = nick,
        role = role
    )

internal fun GroupMessageEvent.Sender.toMember(
    bot: OneBotBotImpl,
): OneBotMemberGroupMessageEventSenderImpl =
    OneBotMemberGroupMessageEventSenderImpl(
        source = this,
        bot = bot,
    )

internal class OneBotMemberApiResultImpl(
    private val source: GetGroupMemberInfoResult,
    override val bot: OneBotBotImpl,
) : OneBotMemberImpl() {
    override val coroutineContext: CoroutineContext = bot.subContext

    override val id: ID
        get() = source.userId

    override val name: String
        get() = source.nickname

    override val nick: String?
        get() = source.card.takeIf { it.isNotEmpty() }

    override val role: OneBotMemberRole
        get() = OneBotMemberRole.valueOfLenient(source.role)
            ?: OneBotMemberRole.MEMBER
}

internal fun GetGroupMemberInfoResult.toMember(
    bot: OneBotBotImpl,
): OneBotMemberApiResultImpl =
    OneBotMemberApiResultImpl(
        source = this,
        bot = bot,
    )
