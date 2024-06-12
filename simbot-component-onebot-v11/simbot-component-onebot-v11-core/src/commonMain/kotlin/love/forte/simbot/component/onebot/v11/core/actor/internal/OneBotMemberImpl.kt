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

import love.forte.simbot.ability.DeleteOption
import love.forte.simbot.ability.StandardDeleteOption
import love.forte.simbot.common.id.ID
import love.forte.simbot.common.time.TimeUnit
import love.forte.simbot.component.onebot.v11.core.actor.OneBotMember
import love.forte.simbot.component.onebot.v11.core.actor.OneBotMemberDeleteOption
import love.forte.simbot.component.onebot.v11.core.actor.OneBotMemberRole
import love.forte.simbot.component.onebot.v11.core.api.GetGroupMemberInfoResult
import love.forte.simbot.component.onebot.v11.core.api.SetGroupAnonymousBanApi
import love.forte.simbot.component.onebot.v11.core.api.SetGroupBanApi
import love.forte.simbot.component.onebot.v11.core.api.SetGroupKickApi
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
import kotlin.jvm.JvmInline
import kotlin.time.Duration


internal abstract class OneBotMemberImpl : OneBotMember {
    protected abstract val bot: OneBotBotImpl
    protected abstract val groupId: ID?

    protected inline val groupIdOrFailure: ID
        get() = groupId
            ?: error("The group id for current member $this is unknown")

    override suspend fun send(text: String): OneBotMessageReceipt {
        return sendPrivateTextMsgApi(
            target = id,
            text = text,
        ).requestDataBy(bot).toReceipt(bot)
    }

    override suspend fun send(messageContent: MessageContent): OneBotMessageReceipt {
        if (messageContent is OneBotMessageContent) {
            return sendPrivateMsgApi(
                target = id,
                message = messageContent.sourceSegments,
            ).requestDataBy(bot).toReceipt(bot)
        }

        return send(messageContent.messages)
    }

    override suspend fun send(message: Message): OneBotMessageReceipt {
        return sendPrivateMsgApi(
            target = id,
            message = message.resolveToOneBotSegmentList()
        ).requestDataBy(bot).toReceipt(bot)
    }

    override suspend fun delete(vararg options: DeleteOption) {
        var mark = DeleteMark()
        for (option in options) {
            when {
                mark.isFull -> break
                option == StandardDeleteOption.IGNORE_ON_FAILURE -> mark = mark.ignoreFailure()
                option == OneBotMemberDeleteOption.RejectRequest -> mark = mark.rejectRequest()
            }
        }

        doDelete(mark)
    }

    private suspend fun doDelete(mark: DeleteMark) {
        val groupId = this.groupId
            ?: if (mark.isIgnoreFailure) {
                return
            } else {
                error("The group id for current member $this is unknown")
            }

        kotlin.runCatching {
            SetGroupKickApi.create(
                groupId = groupId,
                userId = id,
                rejectAddRequest = mark.isRejectRequest
            ).requestDataBy(bot)
        }.onFailure { err ->
            if (!mark.isIgnoreFailure) {
                throw err
            }
        }
    }

    @JvmInline
    internal value class DeleteMark(private val mark: Int = 0) {
        fun ignoreFailure(): DeleteMark = DeleteMark(mark or IGNORE_FAILURE_MARK)
        fun rejectRequest(): DeleteMark = DeleteMark(mark or REJECT_REQUEST)

        val isIgnoreFailure: Boolean get() = mark and IGNORE_FAILURE_MARK != 0
        val isRejectRequest: Boolean get() = mark and REJECT_REQUEST != 0
        val isFull: Boolean get() = mark == FULL

        private companion object {
            const val IGNORE_FAILURE_MARK = 0b01
            const val REJECT_REQUEST = 0b10

            const val FULL = 0b11
        }
    }

    override suspend fun ban(duration: Duration) {
        if (duration == Duration.ZERO) {
            doUnban()
        }

        val seconds = duration.inWholeSeconds
        require(seconds >= 0) {
            "The seconds for ban must >= 0, but $seconds"
        }

        doBan(seconds)
    }

    override suspend fun ban(duration: Long, unit: TimeUnit) {
        require(duration >= 0) {
            "The duration for ban must >= 0, but $duration in $unit"
        }

        if (duration == 0L) {
            doUnban()
        }

        doBan(unit.toSeconds(duration))
    }

    private suspend fun doUnban() {
        doBan(0L)
    }

    protected open suspend fun doBan(seconds: Long) {
        val groupId = groupIdOrFailure

        SetGroupBanApi.create(
            groupId = groupId,
            userId = id,
            duration = seconds
        ).requestDataBy(bot)
    }

    override fun toString(): String = "OneBotMember(id=$id, bot=${bot.id})"
}

internal class OneBotMemberPrivateMessageEventSenderImpl(
    private val source: PrivateMessageEvent.Sender,
    override val groupId: ID?,
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
    override val groupId: ID?,
    private val anonymous: GroupMessageEvent.Anonymous?,
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

    override suspend fun doBan(seconds: Long) {
        val anonymous = this.anonymous
        if (anonymous == null) {
            super.doBan(seconds)
        } else {
            val groupId = groupIdOrFailure

            SetGroupAnonymousBanApi.create(
                groupId = groupId,
                anonymousFlag = anonymous.flag,
                duration = seconds
            ).requestDataBy(bot)
        }
    }
}

internal fun PrivateMessageEvent.Sender.toMember(
    bot: OneBotBotImpl,
    groupId: ID? = null,
    nick: String? = null,
    role: OneBotMemberRole? = null,
): OneBotMemberPrivateMessageEventSenderImpl =
    OneBotMemberPrivateMessageEventSenderImpl(
        source = this,
        groupId = groupId,
        bot = bot,
        nick = nick,
        role = role
    )

internal fun GroupMessageEvent.Sender.toMember(
    bot: OneBotBotImpl,
    groupId: ID,
    anonymous: GroupMessageEvent.Anonymous?
): OneBotMemberGroupMessageEventSenderImpl =
    OneBotMemberGroupMessageEventSenderImpl(
        source = this,
        groupId = groupId,
        anonymous = anonymous,
        bot = bot,
    )

internal class OneBotMemberApiResultImpl(
    private val source: GetGroupMemberInfoResult,
    override val bot: OneBotBotImpl,
) : OneBotMemberImpl() {
    override val coroutineContext: CoroutineContext = bot.subContext

    override val id: ID
        get() = source.userId

    override val groupId: ID
        get() = source.groupId

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
