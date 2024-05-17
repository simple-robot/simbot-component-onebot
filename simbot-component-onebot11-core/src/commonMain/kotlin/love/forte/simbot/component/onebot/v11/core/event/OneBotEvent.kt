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

package love.forte.simbot.component.onebot.v11.core.event

import love.forte.simbot.common.id.LongID
import love.forte.simbot.common.time.Timestamp
import love.forte.simbot.event.Event

public typealias OBSourceEvent = love.forte.simbot.component.onebot.v11.event.Event

/**
 * OneBot11的[事件](https://github.com/botuniverse/onebot-11/tree/master/event)。
 *
 * @author ForteScarlet
 */
public interface OneBotEvent : Event {
    /**
     * 来自事件JSON的反序列化数据体。
     */
    public val sourceEvent: OBSourceEvent

    /**
     * 如果能够支持，则获取来自事件JSON的原始字符串。
     * 不支持、无法获取等情况下得到 `null`。
     */
    public val sourceEventRaw: String?

    /**
     * 事件发生的时间戳
     */
    public val timestamp: Timestamp
        get() = Timestamp.ofMilliseconds(sourceEvent.time)
    // (既然是 `int64`, 那么原始数据应该是毫秒值)

    /**
     * 收到事件的机器人 QQ 号
     */
    public val selfId: LongID
        get() = sourceEvent.selfId

    /**
     * 事件类型
     */
    public val postType: String
        get() = sourceEvent.postType
}
