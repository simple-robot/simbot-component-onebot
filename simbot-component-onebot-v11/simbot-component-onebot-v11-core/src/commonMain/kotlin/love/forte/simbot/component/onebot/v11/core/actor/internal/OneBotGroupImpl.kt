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
import love.forte.simbot.common.collectable.Collectable
import love.forte.simbot.common.collectable.flowCollectable
import love.forte.simbot.common.id.ID
import love.forte.simbot.component.onebot.v11.core.actor.OneBotGroup
import love.forte.simbot.component.onebot.v11.core.actor.OneBotGroupDeleteOption
import love.forte.simbot.component.onebot.v11.core.actor.OneBotMember
import love.forte.simbot.component.onebot.v11.core.api.*
import love.forte.simbot.component.onebot.v11.core.bot.internal.OneBotBotImpl
import love.forte.simbot.component.onebot.v11.core.bot.requestDataBy
import love.forte.simbot.component.onebot.v11.core.bot.requestResultBy
import love.forte.simbot.component.onebot.v11.core.internal.message.toReceipt
import love.forte.simbot.component.onebot.v11.core.utils.sendGroupMsgApi
import love.forte.simbot.component.onebot.v11.core.utils.sendGroupTextMsgApi
import love.forte.simbot.component.onebot.v11.message.OneBotMessageContent
import love.forte.simbot.component.onebot.v11.message.OneBotMessageReceipt
import love.forte.simbot.component.onebot.v11.message.resolveToOneBotSegmentList
import love.forte.simbot.message.Message
import love.forte.simbot.message.MessageContent
import kotlin.concurrent.Volatile
import kotlin.coroutines.CoroutineContext
import kotlin.jvm.JvmInline


internal abstract class OneBotGroupImpl(
    initialName: String
) : OneBotGroup {
    protected abstract val bot: OneBotBotImpl

    @Volatile
    override var name: String = initialName
        protected set

    override val members: Collectable<OneBotMember>
        get() = flowCollectable {
            val list = GetGroupMemberListApi.create(id)
                .requestDataBy(bot)

            for (memberInfoResult in list) {
                emit(memberInfoResult.toMember(bot))
            }
        }

    override suspend fun botAsMember(): OneBotMember =
        member(bot.userId) ?: error("Cannot find the member with userId ${bot.userId} of bot")

    override suspend fun member(id: ID): OneBotMember? {
        val result = GetGroupMemberInfoApi.create(
            groupId = this.id,
            userId = id,
        ).requestResultBy(bot)

        // TODO 如果没找到，如何判断？404？

        return result.dataOrThrow.toMember(bot)
    }

    override suspend fun send(text: String): OneBotMessageReceipt {
        return sendGroupTextMsgApi(
            target = id,
            text = text,
        ).requestDataBy(bot).toReceipt(bot)
    }

    override suspend fun send(messageContent: MessageContent): OneBotMessageReceipt {
        if (messageContent is OneBotMessageContent) {
            return sendGroupMsgApi(
                target = id,
                message = messageContent.sourceSegments,
            ).requestDataBy(bot).toReceipt(bot)
        }

        return send(messageContent.messages)
    }

    override suspend fun send(message: Message): OneBotMessageReceipt {
        return sendGroupMsgApi(
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
                option == OneBotGroupDeleteOption.Dismiss -> mark = mark.dismiss()
            }
        }

        doDelete(mark)
    }

    private suspend fun doDelete(mark: DeleteMark) {
        kotlin.runCatching {
            SetGroupLeaveApi.create(
                groupId = id,
                isDismiss = mark.isDismiss
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
        fun dismiss(): DeleteMark = DeleteMark(mark or DISMISS_MARK)

        val isIgnoreFailure: Boolean get() = mark and IGNORE_FAILURE_MARK != 0
        val isDismiss: Boolean get() = mark and DISMISS_MARK != 0
        val isFull: Boolean get() = mark == FULL

        private companion object {
            const val IGNORE_FAILURE_MARK = 0b01
            const val DISMISS_MARK = 0b10

            const val FULL = 0b11
        }
    }

    override suspend fun ban(enable: Boolean) {
        SetGroupWholeBanApi.create(
            groupId = id,
            enable = enable
        ).requestDataBy(bot)
    }

    override suspend fun setName(newName: String) {
        SetGroupNameApi.create(
            id,
            newName
        ).requestDataBy(bot)

        this.name = newName
    }

    override suspend fun setBotGroupNick(newNick: String?) {
        SetGroupCardApi.create(
            groupId = id,
            userId = bot.userId,
            card = newNick
        ).requestDataBy(bot)
    }

    override suspend fun setAdmin(memberId: ID, enable: Boolean) {
        SetGroupAdminApi.create(
            groupId = id,
            userId = memberId,
            enable = enable,
        ).requestDataBy(bot)
    }

    override suspend fun getHonorInfo(type: String): GetGroupHonorInfoResult =
        GetGroupHonorInfoApi.create(groupId = id, type = type).requestDataBy(bot)

    override fun toString(): String = "OneBotGroup(id=$id, bot=${bot.id})"
}

/**
 * 通过API查询得到的群信息。
 */
internal class OneBotGroupApiResultImpl(
    private val source: GetGroupInfoResult,
    override val bot: OneBotBotImpl,
    override val ownerId: ID?,
) : OneBotGroupImpl(source.groupName) {
    override val coroutineContext: CoroutineContext = bot.subContext
    override val id: ID
        get() = source.groupId
    override val memberCount: Int
        get() = source.memberCount
    override val maxMemberCount: Int
        get() = source.maxMemberCount
}

internal fun GetGroupInfoResult.toGroup(
    bot: OneBotBotImpl,
    ownerId: ID? = null,
): OneBotGroupApiResultImpl =
    OneBotGroupApiResultImpl(
        source = this,
        bot = bot,
        ownerId = ownerId
    )
