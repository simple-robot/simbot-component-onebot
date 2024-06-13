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
import love.forte.simbot.component.onebot.v11.core.actor.OneBotFriend
import love.forte.simbot.component.onebot.v11.core.bot.OneBotBot
import love.forte.simbot.component.onebot.v11.core.event.internal.eventToString
import love.forte.simbot.component.onebot.v11.core.event.notice.OneBotFriendRecallEvent
import love.forte.simbot.component.onebot.v11.event.notice.RawFriendRecallEvent


/**
 *
 * @author ForteScarlet
 */
internal class OneBotFriendRecallEventImpl(
    override val sourceEventRaw: String?,
    override val sourceEvent: RawFriendRecallEvent,
    override val bot: OneBotBot
) : OneBotFriendRecallEvent {
    override val id: ID
        get() = with(sourceEvent) {
            "$postType-$noticeType-$userId-$messageId-$time"
        }.ID

    override suspend fun content(): OneBotFriend {
        return bot.contactRelation.contact(sourceEvent.userId)
            ?: error("Friend with id ${sourceEvent.userId} not found")
    }

    override fun toString(): String =
        eventToString("OneBotFriendRecallEvent")
}
