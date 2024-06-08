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
import love.forte.simbot.component.onebot.v11.event.notice.GroupUploadEvent
import love.forte.simbot.event.ChatGroupEvent
import love.forte.simbot.suspendrunner.STP


/**
 * 群文件上传事件
 *
 * @see GroupUploadEvent
 * @author ForteScarlet
 */
public interface OneBotGroupUploadEvent : OneBotNoticeEvent, ChatGroupEvent {
    override val sourceEvent: GroupUploadEvent

    /**
     * 群号
     */
    public val groupId: LongID
        get() = sourceEvent.groupId

    /**
     * 上传者ID
     */
    public val userId: LongID
        get() = sourceEvent.userId

    /**
     * [GroupUploadEvent] 原始事件中的 [文件信息][GroupUploadEvent.file]
     *
     * @see GroupUploadEvent.file
     */
    public val fileInfo: GroupUploadEvent.FileInfo
        get() = sourceEvent.file

    /**
     * 群
     *
     * @throws Exception
     */
    @STP
    override suspend fun content(): OneBotGroup
}
