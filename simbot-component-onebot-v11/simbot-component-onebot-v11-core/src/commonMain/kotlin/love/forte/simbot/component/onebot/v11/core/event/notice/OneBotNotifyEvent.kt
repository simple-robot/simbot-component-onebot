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
import love.forte.simbot.component.onebot.v11.event.notice.NotifyEvent
import love.forte.simbot.event.MemberEvent
import love.forte.simbot.suspendrunner.STP


/**
 * 群成员荣誉变更事件
 *
 * @see NotifyEvent
 * @author ForteScarlet
 */
public interface OneBotNotifyEvent : OneBotNoticeEvent, MemberEvent {
    override val sourceEvent: NotifyEvent

    /**
     * 荣誉类型.
     *
     * @see NotifyEvent.honorType
     */
    public val honorType: String
        get() = sourceEvent.honorType

    /**
     * 群号
     */
    public val groupId: LongID
        get() = sourceEvent.groupId

    /**
     * 成员ID
     */
    public val userId: LongID
        get() = sourceEvent.userId

    /**
     * 群
     *
     * @throws Exception
     */
    @STP
    override suspend fun source(): OneBotGroup

    /**
     * 群成员
     *
     * @throws Exception
     */
    @STP
    override suspend fun content(): OneBotMember

}
