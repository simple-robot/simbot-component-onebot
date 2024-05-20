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

package love.forte.simbot.component.onebot.v11.event.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import love.forte.simbot.common.id.LongID
import love.forte.simbot.component.onebot.v11.event.ExpectEventType


/**
 * [加好友请求](https://github.com/botuniverse/onebot-11/blob/master/event/request.md#加好友请求)
 *
 * @property userId 发送请求的 QQ 号
 * @property comment 验证信息
 * @property flag 请求 flag，在调用处理请求的 API 时需要传入
 *
 * @author ForteScarlet
 */
@Serializable
@ExpectEventType(postType = RequestEvent.POST_TYPE, subType = "friend")
public data class FriendRequestEvent(
    override val time: Long,
    @SerialName("request_type")
    override val requestType: String,
    @SerialName("self_id")
    override val selfId: LongID,
    @SerialName("post_type")
    override val postType: String,
    @SerialName("user_id")
    public val userId: LongID,
    public val comment: String = "",
    public val flag: String,
) : RequestEvent
