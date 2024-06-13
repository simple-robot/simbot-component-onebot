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

package love.forte.simbot.component.onebot.v11.core.event.meta

import love.forte.simbot.component.onebot.v11.event.meta.RawLifecycleEvent


/**
 * [生命周期](https://github.com/botuniverse/onebot-11/blob/master/event/meta.md#生命周期)
 *
 * @see RawLifecycleEvent
 *
 * @author ForteScarlet
 */
public interface OneBotLifecycleEvent : OneBotMetaEvent {
    override val sourceEvent: RawLifecycleEvent

    /**
     * 事件子类型，分别表示 OneBot 启用、停用、WebSocket 连接成功.
     * 可能的值: `enable`、`disable`、`connect`.
     */
    public val subType: String
        get() = sourceEvent.subType
}
