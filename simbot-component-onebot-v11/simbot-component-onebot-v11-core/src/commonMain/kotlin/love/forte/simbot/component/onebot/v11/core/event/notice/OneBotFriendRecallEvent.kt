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

package love.forte.simbot.component.onebot.v11.core.event.notice

import love.forte.simbot.common.id.ID
import love.forte.simbot.common.id.LongID
import love.forte.simbot.component.onebot.v11.core.actor.OneBotFriend
import love.forte.simbot.component.onebot.v11.event.notice.RawFriendRecallEvent
import love.forte.simbot.event.ContactEvent
import love.forte.simbot.event.FuzzyEventTypeImplementation
import love.forte.simbot.suspendrunner.STP


/**
 * 好友消息撤回事件
 * @see RawFriendRecallEvent
 * @author ForteScarlet
 */
@OptIn(FuzzyEventTypeImplementation::class)
public interface OneBotFriendRecallEvent : OneBotNoticeEvent, ContactEvent {
    override val sourceEvent: RawFriendRecallEvent

    /**
     * 消息ID
     */
    public val messageId: ID
        get() = sourceEvent.messageId

    /**
     * 消息发送人的ID
     */
    public val authorId: LongID
        get() = sourceEvent.userId

    /**
     * 好友
     *
     * @throws Exception
     */
    @STP
    override suspend fun content(): OneBotFriend
}
