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

import love.forte.simbot.common.id.LongID
import love.forte.simbot.component.onebot.v11.core.actor.OneBotGroup
import love.forte.simbot.component.onebot.v11.core.actor.OneBotMember
import love.forte.simbot.component.onebot.v11.event.notice.GroupDecreaseEvent
import love.forte.simbot.component.onebot.v11.event.notice.GroupIncreaseEvent
import love.forte.simbot.event.MemberDecreaseEvent
import love.forte.simbot.event.MemberIncreaseEvent
import love.forte.simbot.event.MemberIncreaseOrDecreaseEvent
import love.forte.simbot.suspendrunner.STP

/**
 * 群成员增加或减少事件
 *
 * @see OneBotGroupMemberIncreaseEvent
 * @see OneBotGroupMemberDecreaseEvent
 */
public interface OneBotGroupChangeEvent : OneBotNoticeEvent, MemberIncreaseOrDecreaseEvent {
    /**
     * 群号
     */
    public val groupId: LongID

    /**
     * 增加或离去的群成员ID
     */
    public val userId: LongID

    /**
     * 群
     * @throws Exception
     */
    @STP
    override suspend fun content(): OneBotGroup

    /**
     * 增加或离去的成员。
     * 如果无法获取则可能得到 `null`，
     * 例如成员已经离去而无法获取。
     * @throws Exception
     */
    @STP
    override suspend fun member(): OneBotMember?
}

/**
 * 群成员增加事件
 *
 * @see GroupIncreaseEvent
 */
public interface OneBotGroupMemberIncreaseEvent : OneBotGroupChangeEvent, MemberIncreaseEvent {
    override val sourceEvent: GroupIncreaseEvent

    /**
     * 群号
     */
    override val groupId: LongID
        get() = sourceEvent.groupId

    /**
     * 增加群成员ID
     */
    override val userId: LongID
        get() = sourceEvent.userId

    /**
     * 群
     * @throws Exception
     */
    @STP
    override suspend fun content(): OneBotGroup

    /**
     * 增加的成员
     * @throws Exception
     */
    @STP
    override suspend fun member(): OneBotMember
}

/**
 * 群成员减少事件
 *
 * @see GroupDecreaseEvent
 */
public interface OneBotGroupMemberDecreaseEvent : OneBotGroupChangeEvent, MemberDecreaseEvent {
    override val sourceEvent: GroupDecreaseEvent

    /**
     * 群号
     */
    override val groupId: LongID
        get() = sourceEvent.groupId

    /**
     * 离去的群成员ID
     */
    override val userId: LongID
        get() = sourceEvent.userId

    /**
     * 群
     * @throws Exception
     */
    @STP
    override suspend fun content(): OneBotGroup

    /**
     * 离去的成员。已经离去无法获取，
     * 始终得到 null
     */
    @STP
    override suspend fun member(): OneBotMember? = null
}
