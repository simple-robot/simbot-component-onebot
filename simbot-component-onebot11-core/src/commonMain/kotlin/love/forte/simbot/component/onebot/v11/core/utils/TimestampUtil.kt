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

package love.forte.simbot.component.onebot.v11.core.utils

import love.forte.simbot.common.time.TimeUnit
import love.forte.simbot.common.time.Timestamp
import love.forte.simbot.component.onebot.v11.core.event.OBSourceEvent

// 虽然是 Long 类型，但是似乎是10位秒值
internal fun OBSourceEvent.timestamp(): Timestamp =
    Timestamp.ofMilliseconds(TimeUnit.SECONDS.toMillis(time))
