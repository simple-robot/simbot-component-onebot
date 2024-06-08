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

package love.forte.simbot.component.onebot.v11.core.event.internal.request

import love.forte.simbot.common.id.ID
import love.forte.simbot.common.id.UUID
import love.forte.simbot.component.onebot.v11.core.actor.OneBotGroup
import love.forte.simbot.component.onebot.v11.core.actor.OneBotStranger
import love.forte.simbot.component.onebot.v11.core.actor.internal.toStranger
import love.forte.simbot.component.onebot.v11.core.api.GetStrangerInfoApi
import love.forte.simbot.component.onebot.v11.core.api.SetFriendAddRequestApi
import love.forte.simbot.component.onebot.v11.core.api.SetGroupAddRequestApi
import love.forte.simbot.component.onebot.v11.core.bot.OneBotBot
import love.forte.simbot.component.onebot.v11.core.bot.internal.OneBotBotImpl
import love.forte.simbot.component.onebot.v11.core.bot.requestDataBy
import love.forte.simbot.component.onebot.v11.core.event.internal.eventToString
import love.forte.simbot.component.onebot.v11.core.event.request.OneBotFriendRequestEvent
import love.forte.simbot.component.onebot.v11.core.event.request.OneBotGroupRequestEvent
import love.forte.simbot.component.onebot.v11.core.event.request.OneBotRequestEvent
import love.forte.simbot.component.onebot.v11.event.request.FriendRequestEvent
import love.forte.simbot.component.onebot.v11.event.request.GroupRequestEvent


internal abstract class OneBotRequestEventImpl : OneBotRequestEvent {
    override val id: ID = UUID.random()

    override suspend fun accept() {
        doSetRequest(true)
    }

    override suspend fun reject() {
        doSetRequest(false)
    }

    protected abstract suspend fun doSetRequest(approve: Boolean)
}

internal class OneBotFriendRequestEventImpl(
    override val sourceEventRaw: String?,
    override val sourceEvent: FriendRequestEvent,
    override val bot: OneBotBot,
) : OneBotRequestEventImpl(), OneBotFriendRequestEvent {
    override suspend fun doSetRequest(approve: Boolean) {
        SetFriendAddRequestApi.create(
            flag = sourceEvent.flag,
            approve = approve,
        ).requestDataBy(bot)
    }

    override fun toString(): String =
        eventToString("OneBotFriendRequestEvent")
}

internal class OneBotGroupRequestEventImpl(
    override val sourceEventRaw: String?,
    override val sourceEvent: GroupRequestEvent,
    override val bot: OneBotBotImpl,
) : OneBotRequestEventImpl(), OneBotGroupRequestEvent {
    override suspend fun doSetRequest(approve: Boolean) {
        SetGroupAddRequestApi.create(
            flag = sourceEvent.flag,
            subType = sourceEvent.subType,
            approve = approve
        ).requestDataBy(bot)
    }

    override suspend fun content(): OneBotGroup {
        return bot.groupRelation.group(sourceEvent.groupId)
            ?: error("Group with id ${sourceEvent.groupId} not found")
    }

    override suspend fun requester(): OneBotStranger {
        return GetStrangerInfoApi
            .create(sourceEvent.userId)
            .requestDataBy(bot)
            .toStranger(bot)
    }

    override fun toString(): String =
        eventToString("OneBotGroupRequestEvent")
}

