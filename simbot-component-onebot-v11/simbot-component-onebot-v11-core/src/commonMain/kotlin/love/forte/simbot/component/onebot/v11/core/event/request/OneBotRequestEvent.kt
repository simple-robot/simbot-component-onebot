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

import love.forte.simbot.component.onebot.v11.core.event.OneBotBotEvent
import love.forte.simbot.event.RequestEvent

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
