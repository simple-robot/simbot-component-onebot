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
import love.forte.simbot.component.onebot.v11.core.bot.OneBotBot
import love.forte.simbot.component.onebot.v11.core.bot.internal.OneBotBotImpl
import love.forte.simbot.component.onebot.v11.core.event.internal.eventToString
import love.forte.simbot.component.onebot.v11.core.event.notice.OneBotGroupChangeEvent
import love.forte.simbot.component.onebot.v11.core.event.notice.OneBotGroupMemberDecreaseEvent
import love.forte.simbot.component.onebot.v11.core.event.notice.OneBotGroupMemberIncreaseEvent
import love.forte.simbot.component.onebot.v11.event.notice.RawGroupDecreaseEvent
import love.forte.simbot.component.onebot.v11.event.notice.RawGroupIncreaseEvent


/**
 *
 * @author ForteScarlet
 */
internal abstract class OneBotGroupChangeEventImpl : OneBotGroupChangeEvent {
    override val id: ID
        get() = with(sourceEvent) {
            "$postType-$noticeType-$groupId-$userId-$time"
        }.ID

    override suspend fun content(): OneBotGroup {
        return bot.groupRelation.group(groupId)
            ?: error("Group with id $groupId is not found")
    }
}

internal class OneBotGroupMemberIncreaseEventImpl(
    override val sourceEventRaw: String?,
    override val sourceEvent: RawGroupIncreaseEvent,
    override val bot: OneBotBot
) : OneBotGroupChangeEventImpl(), OneBotGroupMemberIncreaseEvent {
    override suspend fun member(): OneBotMember {
        return bot.groupRelation.member(
            groupId = sourceEvent.groupId,
            memberId = sourceEvent.userId
        ) ?: error(
            "Member with id ${sourceEvent.userId} " +
                "in Group ${sourceEvent.groupId} is not found"
        )
    }

    override fun toString(): String =
        eventToString("OneBotGroupMemberIncreaseEvent")
}

internal class OneBotGroupMemberDecreaseEventImpl(
    override val sourceEventRaw: String?,
    override val sourceEvent: RawGroupDecreaseEvent,
    override val bot: OneBotBot
) : OneBotGroupChangeEventImpl(), OneBotGroupMemberDecreaseEvent {

    override fun toString(): String =
        eventToString("OneBotGroupMemberDecreaseEvent")
}
