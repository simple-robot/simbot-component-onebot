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

package love.forte.simbot.component.onebot.v11.core.actor

import love.forte.simbot.common.id.ID
import love.forte.simbot.common.id.literal
import love.forte.simbot.component.onebot.common.annotations.OneBotInternalImplementationsOnly
import love.forte.simbot.component.onebot.v11.common.utils.qqAvatar640
import love.forte.simbot.component.onebot.v11.core.api.GetStrangerInfoApi
import love.forte.simbot.definition.User


/**
 * 一个陌生人。
 * 通常是通过 [GetStrangerInfoApi]
 * 得到的信息。
 *
 * @author ForteScarlet
 */
@OneBotInternalImplementationsOnly
public interface OneBotStranger : User {
    override val id: ID
    override val name: String

    override val avatar: String
        get() = qqAvatar640(id.literal)

    /**
     * 年龄。
     * 如果无法获取则有可能会被填充一个默认值。
     */
    public val age: Int

    /**
     * 性别。`male` 或 `female` 或 `unknown`
     */
    public val sex: String
}
