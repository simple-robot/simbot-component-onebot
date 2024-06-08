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

package love.forte.simbot.component.onebot.v11.core.event.internal.notice

import love.forte.simbot.common.id.ID
import love.forte.simbot.common.id.StringID.Companion.ID
import love.forte.simbot.component.onebot.v11.core.actor.OneBotGroup
import love.forte.simbot.component.onebot.v11.core.actor.OneBotMember
import love.forte.simbot.component.onebot.v11.core.actor.internal.toMember
import love.forte.simbot.component.onebot.v11.core.api.GetGroupMemberInfoApi
import love.forte.simbot.component.onebot.v11.core.bot.internal.OneBotBotImpl
import love.forte.simbot.component.onebot.v11.core.bot.requestDataBy
import love.forte.simbot.component.onebot.v11.core.event.internal.eventToString
import love.forte.simbot.component.onebot.v11.core.event.notice.OneBotBotSelfPokeEvent
import love.forte.simbot.component.onebot.v11.core.event.notice.OneBotHonorEvent
import love.forte.simbot.component.onebot.v11.core.event.notice.OneBotLuckyKingEvent
import love.forte.simbot.component.onebot.v11.core.event.notice.OneBotMemberPokeEvent
import love.forte.simbot.component.onebot.v11.core.event.notice.OneBotNotifyEvent
import love.forte.simbot.component.onebot.v11.event.notice.NotifyEvent


/**
 *
 * @author ForteScarlet
 */
internal abstract class OneBotNotifyEventImpl(
    override val sourceEventRaw: String?,
    override val sourceEvent: NotifyEvent,
    override val bot: OneBotBotImpl
) : OneBotNotifyEvent {
    override val id: ID
        get() = with(sourceEvent) {
            "$postType-$noticeType-$subType-$groupId-$userId-$time"
        }.ID

    override suspend fun source(): OneBotGroup {
        return bot.groupRelation.group(groupId)
            ?: error("Group with id $groupId not found")
    }

    override suspend fun content(): OneBotMember {
        // TODO 换成 groupRelation
        return GetGroupMemberInfoApi.create(
            groupId = sourceEvent.groupId,
            userId = sourceEvent.userId
        ).requestDataBy(bot).toMember(bot)
    }
}

internal class OneBotHonorEventImpl(
    sourceEventRaw: String?,
    sourceEvent: NotifyEvent,
    bot: OneBotBotImpl
) : OneBotNotifyEventImpl(sourceEventRaw, sourceEvent, bot),
    OneBotHonorEvent {
    override fun toString(): String =
        eventToString("OneBotHonorEvent")
}

internal class OneBotLuckyKingEventImpl(
    sourceEventRaw: String?,
    sourceEvent: NotifyEvent,
    bot: OneBotBotImpl
) : OneBotNotifyEventImpl(sourceEventRaw, sourceEvent, bot),
    OneBotLuckyKingEvent {
    override fun toString(): String =
        eventToString("OneBotLuckyKingEvent")
}

internal class OneBotMemberPokeEventImpl(
    sourceEventRaw: String?,
    sourceEvent: NotifyEvent,
    bot: OneBotBotImpl
) : OneBotNotifyEventImpl(sourceEventRaw, sourceEvent, bot),
    OneBotMemberPokeEvent {
    override fun toString(): String =
        eventToString("OneBotMemberPokeEvent")
}

internal class OneBotBotSelfPokeEventImpl(
    sourceEventRaw: String?,
    sourceEvent: NotifyEvent,
    bot: OneBotBotImpl
) : OneBotNotifyEventImpl(sourceEventRaw, sourceEvent, bot),
    OneBotBotSelfPokeEvent {
    override fun toString(): String =
        eventToString("OneBotBotSelfPokeEvent")
}
