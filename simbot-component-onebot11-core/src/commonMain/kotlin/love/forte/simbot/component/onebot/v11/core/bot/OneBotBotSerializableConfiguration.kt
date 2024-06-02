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

package love.forte.simbot.component.onebot.v11.core.bot

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import love.forte.simbot.annotations.InternalSimbotAPI
import love.forte.simbot.bot.SerializableBotConfiguration
import love.forte.simbot.component.onebot.v11.core.component.OneBot11Component


/**
 * 用于可序列化场景下的反序列化目标。
 *
 * 仅用于序列化，不保证代码内直接使用的兼容性与稳定性，
 * 请不要直接在代码中使用它。
 *
 * @author ForteScarlet
 */
@InternalSimbotAPI
@Serializable
@SerialName(OneBot11Component.ID_VALUE)
public data class OneBotBotSerializableConfiguration(
    val authorization: Authorization,
) : SerializableBotConfiguration() {
    @Serializable
    public data class Authorization(
        val botUniqueId: String,
        val accessToken: String? = null,
        val apiServerHost: String,
        val eventServerHost: String,
    )


    public fun toConfiguration(): OneBotBotConfiguration {
        TODO()
    }
}
