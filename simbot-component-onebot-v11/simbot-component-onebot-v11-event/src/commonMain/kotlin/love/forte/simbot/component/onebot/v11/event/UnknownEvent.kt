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

package love.forte.simbot.component.onebot.v11.event

import love.forte.simbot.annotations.FragileSimbotAPI
import love.forte.simbot.common.id.LongID


/**
 * 用于“兜底”的 [RawEvent] 类型实现。
 * 当出现了尚未支持或某种未知的事件体，无法对应到任何现有的已定义结构时，
 * 则应当将其解析并包装为 [UnknownEvent]。
 *
 * [UnknownEvent] 要求事件体必须包括 [time], [selfId] 和 [postType]。
 *
 * ### FragileAPI
 *
 * 这是一个具有**特殊规则**的事件类型。
 * 随着版本地更新，[UnknownEvent] 中可能出现的事件类型会越来越少。
 * 因此此类型只适用于“兜底”，不应过度依赖。
 *
 * @author ForteScarlet
 */
@FragileSimbotAPI
public data class UnknownEvent(
    override val time: Long,
    override val selfId: LongID,
    override val postType: String,
    /**
     * 原始的JSON字符串
     */
    public val raw: String,
) : RawEvent
