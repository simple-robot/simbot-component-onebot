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

package love.forte.simbot.component.onebot.v11.core.actor.internal

import love.forte.simbot.common.id.ID
import love.forte.simbot.component.onebot.v11.core.actor.OneBotStranger
import love.forte.simbot.component.onebot.v11.core.api.GetStrangerInfoResult
import love.forte.simbot.component.onebot.v11.core.bot.OneBotBot
import love.forte.simbot.component.onebot.v11.core.bot.internal.OneBotBotImpl
import kotlin.coroutines.CoroutineContext


/**
 *
 * @author ForteScarlet
 */
internal class OneBotStrangerImpl(
    override val coroutineContext: CoroutineContext,
    private val source: GetStrangerInfoResult
) : OneBotStranger {

    override val id: ID
        get() = source.userId

    override val name: String
        get() = source.nickname

    override val age: Int
        get() = source.age

    override val sex: String
        get() = source.sex

    override fun toString(): String =
        "OneBotStranger(source=$source)"
}

internal fun GetStrangerInfoResult.toStranger(
    bot: OneBotBot,
    coroutineContext: CoroutineContext = bot.subContext,
): OneBotStrangerImpl =
    OneBotStrangerImpl(coroutineContext, this)
