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

package love.forte.simbot.component.onebot.v11.message.segment

import love.forte.simbot.component.onebot.v11.message.OneBotMessageElement
import love.forte.simbot.message.Message


/**
 * OneBot11的 [消息段](https://github.com/botuniverse/onebot-11/blob/master/message/array.md)
 * 类型定义。
 * 其中，消息段的类型 `type` 由多态序列化类型名决定，而 `data` 的类型则由实现者决定。
 *
 * 定义的可序列化子类型会被统一以 [Message.Element] 的多态类型被添加到 `OneBot11Component.serializersModule` 中。
 * (通过
 * [includeAllComponentMessageElementImpls][love.forte.simbot.component.onebot.v11.message.includeAllComponentMessageElementImpls] 或
 * [includeAllOneBotSegmentImpls][love.forte.simbot.component.onebot.v11.message.includeAllOneBotSegmentImpls]
 * )
 *
 * @author ForteScarlet
 */
public interface OneBotMessageSegment : OneBotMessageElement {
    /**
     * 消息段的内容。
     */
    public val data: Any?
}
