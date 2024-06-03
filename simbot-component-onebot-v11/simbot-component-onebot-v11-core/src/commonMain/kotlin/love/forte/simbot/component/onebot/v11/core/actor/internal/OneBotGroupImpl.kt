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

import love.forte.simbot.common.collectable.Collectable
import love.forte.simbot.common.collectable.flowCollectable
import love.forte.simbot.common.id.ID
import love.forte.simbot.component.onebot.v11.core.actor.OneBotGroup
import love.forte.simbot.component.onebot.v11.core.actor.OneBotMember
import love.forte.simbot.component.onebot.v11.core.api.GetGroupInfoResult
import love.forte.simbot.component.onebot.v11.core.api.GetGroupMemberInfoApi
import love.forte.simbot.component.onebot.v11.core.api.GetGroupMemberListApi
import love.forte.simbot.component.onebot.v11.core.bot.internal.OneBotBotImpl
import love.forte.simbot.component.onebot.v11.core.bot.requestDataBy
import love.forte.simbot.component.onebot.v11.core.bot.requestResultBy
import love.forte.simbot.component.onebot.v11.core.internal.message.toReceipt
import love.forte.simbot.component.onebot.v11.core.utils.sendGroupMsgApi
import love.forte.simbot.component.onebot.v11.core.utils.sendGroupTextMsgApi
import love.forte.simbot.component.onebot.v11.event.message.GroupMessageEvent
import love.forte.simbot.component.onebot.v11.message.OneBotMessageContent
import love.forte.simbot.component.onebot.v11.message.OneBotMessageReceipt
import love.forte.simbot.component.onebot.v11.message.resolveToOneBotSegmentList
import love.forte.simbot.message.Message
import love.forte.simbot.message.MessageContent
import kotlin.coroutines.CoroutineContext


internal abstract class OneBotGroupImpl : OneBotGroup {
    protected abstract val bot: OneBotBotImpl

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

    override fun toString(): String = "OneBotGroup(id=$id, bot=${bot.id})"
}

internal class OneBotGroupEventImpl(
    private val source: GroupMessageEvent,
    override val bot: OneBotBotImpl,

    /**
     * 群名称
     */
    override val name: String,

    /**
     * 群主ID
     */
    override val ownerId: ID?
) : OneBotGroupImpl() {
    override val coroutineContext: CoroutineContext = bot.subContext

    override val id: ID
        get() = source.groupId
}

// TODO 群名称, 可能需要使用缓存或API初始化,
//  事件中似乎取不到
internal fun GroupMessageEvent.toGroup(
    bot: OneBotBotImpl,
    name: String = "",
    ownerId: ID? = null
): OneBotGroupEventImpl =
    OneBotGroupEventImpl(
        source = this,
        bot = bot,
        name = name,
        ownerId = ownerId,
    )

/**
 * 通过API查询得到的群信息。
 */
internal class OneBotGroupApiResultImpl(
    private val source: GetGroupInfoResult,
    override val bot: OneBotBotImpl,
    override val ownerId: ID?,
) : OneBotGroupImpl() {
    override val coroutineContext: CoroutineContext = bot.subContext
    override val id: ID
        get() = source.groupId

    override val name: String
        get() = source.groupName
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
