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

import love.forte.simbot.annotations.Api4J
import love.forte.simbot.component.onebot.v11.common.api.StatusResult
import love.forte.simbot.component.onebot.v11.event.meta.HeartbeatEvent
import love.forte.simbot.component.onebot.v11.event.meta.MetaEvent
import kotlin.jvm.JvmSynthetic
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds


/**
 * [心跳事件](https://github.com/botuniverse/onebot-11/blob/master/event/meta.md#心跳)
 *
 * @see MetaEvent
 *
 * @author ForteScarlet
 */
public interface OneBotHeartbeatEvent : OneBotMetaEvent {
    override val sourceEvent: HeartbeatEvent

    /**
     * 状态信息
     */
    public val status: StatusResult
        get() = sourceEvent.status

    /**
     * 到下次心跳的间隔，单位毫秒
     */
    @Api4J
    public val intervalValue: Long
        get() = sourceEvent.interval

    /**
     * 到下次心跳的间隔
     */
    @get:JvmSynthetic
    public val interval: Duration
        get() = sourceEvent.interval.milliseconds
}
