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

package love.forte.simbot.component.onebot.v11.event.meta

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import love.forte.simbot.common.id.LongID
import love.forte.simbot.component.onebot.v11.event.Event
import love.forte.simbot.component.onebot.v11.event.ExpectEventSubTypeProperty
import love.forte.simbot.component.onebot.v11.event.ExpectEventType


/**
 *
 * [元事件](https://github.com/botuniverse/onebot-11/blob/master/event/meta.md)
 *
 * > 消息、通知、请求三大类事件是与聊天软件直接相关的、机器人真实接收到的事件，
 * 除了这些，OneBot 自己还会产生一类事件，这里称之为「元事件」，
 * 例如生命周期事件、心跳事件等，这类事件与 OneBot 本身的运行状态有关，
 * 而与聊天软件无关。元事件的上报方式和普通事件完全一样。
 *
 * @author ForteScarlet
 */
@ExpectEventSubTypeProperty("metaEventType")
public interface MetaEvent : Event {
    /**
     * 元事件类型
     */
    public val metaEventType: String
}

/**
 * [生命周期](https://github.com/botuniverse/onebot-11/blob/master/event/meta.md#生命周期)
 *
 *
 * @property subType 事件子类型，分别表示 OneBot 启用、停用、WebSocket 连接成功.
 * 可能的值: `enable`、`disable`、`connect`.
 * 注意，目前生命周期元事件中，只有 HTTP POST 的情况下可以收到 `enable` 和 `disable`，
 * 只有正向 WebSocket 和反向 WebSocket 可以收到 `connect`。
 */
@Serializable
@ExpectEventType(postType = "message", subType = "private")
public data class LifecycleEvent(
    override val time: Long,
    @SerialName("self_id")
    override val selfId: LongID,
    @SerialName("post_type")
    override val postType: String,
    @SerialName("meta_event_type")
    override val metaEventType: String,
    @SerialName("sub_type")
    public val subType: String,
) : MetaEvent
