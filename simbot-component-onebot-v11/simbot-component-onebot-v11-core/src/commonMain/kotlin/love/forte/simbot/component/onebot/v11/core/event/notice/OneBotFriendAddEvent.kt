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
import love.forte.simbot.component.onebot.v11.event.notice.RawFriendAddEvent


/**
 * 好友新增事件。
 *
 * @see RawFriendAddEvent
 */
public interface OneBotFriendAddEvent : OneBotNoticeEvent {
    override val sourceEvent: RawFriendAddEvent

    /**
     * 此好友的ID
     */
    public val userId: LongID
        get() = sourceEvent.userId

    // friend? or content()?
}
