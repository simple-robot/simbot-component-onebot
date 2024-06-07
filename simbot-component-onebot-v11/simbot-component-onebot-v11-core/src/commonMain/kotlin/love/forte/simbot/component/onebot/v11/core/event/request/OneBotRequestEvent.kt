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

package love.forte.simbot.component.onebot.v11.core.event.request

import love.forte.simbot.common.id.LongID
import love.forte.simbot.component.onebot.v11.core.actor.OneBotGroup
import love.forte.simbot.component.onebot.v11.core.actor.OneBotStranger
import love.forte.simbot.component.onebot.v11.core.event.OneBotBotEvent
import love.forte.simbot.component.onebot.v11.event.request.FriendRequestEvent
import love.forte.simbot.component.onebot.v11.event.request.GroupRequestEvent
import love.forte.simbot.event.OrganizationJoinRequestEvent
import love.forte.simbot.event.RequestEvent
import love.forte.simbot.suspendrunner.STP

public typealias OBSourceRequestEvent = love.forte.simbot.component.onebot.v11.event.request.RequestEvent

/**
 * OneBot组件中的 [事件请求][love.forte.simbot.component.onebot.v11.event.request.RequestEvent]
 * 的组件事件类型。
 *
 * @author ForteScarlet
 */
public interface OneBotRequestEvent : OneBotBotEvent, RequestEvent {
    override val sourceEvent: OBSourceRequestEvent
}

/**
 * 好友添加申请事件
 * @see FriendRequestEvent
 */
public interface OneBotFriendRequestEvent : OneBotRequestEvent {
    override val sourceEvent: FriendRequestEvent

    /**
     * 好友添加申请始终是 [主动地][RequestEvent.Type.PROACTIVE]
     */
    override val type: RequestEvent.Type
        get() = RequestEvent.Type.PROACTIVE

    /**
     * 验证信息。
     *
     * @see FriendRequestEvent.comment
     */
    override val message: String
        get() = sourceEvent.comment

    /**
     * 请求 flag，在调用处理请求的 API 时需要传入
     *
     * @see FriendRequestEvent.flag
     */
    public val flag: String
        get() = sourceEvent.flag

    /**
     * 发送请求的 QQ 号
     *
     * @see FriendRequestEvent.userId
     */
    public val requesterId: LongID
        get() = sourceEvent.userId
}

/**
 * 群添加申请事件
 * @see GroupRequestEvent
 */
public interface OneBotGroupRequestEvent : OneBotRequestEvent, OrganizationJoinRequestEvent {
    override val sourceEvent: GroupRequestEvent

    /**
     * 申请加入的群。
     */
    @STP
    override suspend fun content(): OneBotGroup

    /**
     * 根据 [GroupRequestEvent.subType] 的值区分类型。
     * 如果是 `invite` 则为被动，否则（包括 `add`）均视为主动。
     *
     */
    override val type: RequestEvent.Type
        get() = when (sourceEvent.subType) {
            GroupRequestEvent.SUB_TYPE_INVITE -> RequestEvent.Type.PASSIVE
            GroupRequestEvent.SUB_TYPE_ADD -> RequestEvent.Type.PROACTIVE
            else -> RequestEvent.Type.PROACTIVE
        }

    /**
     * 验证信息。
     *
     * @see GroupRequestEvent.comment
     */
    override val message: String
        get() = sourceEvent.comment

    /**
     * 请求 flag，在调用处理请求的 API 时需要传入
     *
     * @see GroupRequestEvent.flag
     */
    public val flag: String
        get() = sourceEvent.flag

    /**
     * 发送请求的 QQ 号
     *
     * @see GroupRequestEvent.userId
     */
    override val requesterId: LongID
        get() = sourceEvent.userId

    /**
     * 申请者的部分信息。
     */
    @STP
    override suspend fun requester(): OneBotStranger
}
