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

package love.forte.simbot.component.onebot.v11.event.notice

import kotlin.Long
import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import love.forte.simbot.common.id.LongID
import love.forte.simbot.component.onebot.v11.event.ExpectEventType

/**
 * [群成员荣誉变更](https://github.com/botuniverse/onebot-11/blob/master/event/notice.md#群成员荣誉变更)
 *
 * @property subType 提示类型。
 * 可能的值: `honor`
 * @property groupId 群号。
 * @property honorType 荣誉类型，分别表示龙王、群聊之火、快乐源泉。
 * 可能的值: `talkative`、`performer`、`emotion`
 * @property userId 成员 QQ 号。
 */
@ExpectEventType(
    postType = NoticeEvent.POST_TYPE,
    subType = "notify",
)
@Serializable
public data class NotifyEvent(
    override val time: Long,
    @SerialName("self_id")
    override val selfId: LongID,
    @SerialName("post_type")
    override val postType: String,
    @SerialName("notice_type")
    override val noticeType: String,
    @SerialName("sub_type")
    public val subType: String,
    @SerialName("group_id")
    public val groupId: LongID,
    @SerialName("honor_type")
    public val honorType: String,
    @SerialName("user_id")
    public val userId: LongID,
) : NoticeEvent
