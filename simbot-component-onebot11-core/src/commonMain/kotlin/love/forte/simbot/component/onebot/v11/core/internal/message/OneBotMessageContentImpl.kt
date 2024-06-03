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

package love.forte.simbot.component.onebot.v11.core.internal.message

import love.forte.simbot.ability.DeleteOption
import love.forte.simbot.ability.StandardDeleteOption
import love.forte.simbot.common.id.ID
import love.forte.simbot.component.onebot.v11.core.api.DeleteMsgApi
import love.forte.simbot.component.onebot.v11.core.bot.internal.OneBotBotImpl
import love.forte.simbot.component.onebot.v11.core.bot.requestDataBy
import love.forte.simbot.component.onebot.v11.message.OneBotMessageContent
import love.forte.simbot.component.onebot.v11.message.resolveToMessageElement
import love.forte.simbot.component.onebot.v11.message.segment.OneBotMessageSegment
import love.forte.simbot.component.onebot.v11.message.segment.OneBotText
import love.forte.simbot.message.Messages
import love.forte.simbot.message.toMessages


/**
 *
 * @author ForteScarlet
 */
internal class OneBotMessageContentImpl(
    override val id: ID,
    override val sourceSegments: List<OneBotMessageSegment>,
    private val bot: OneBotBotImpl,
) : OneBotMessageContent {
    override val messages: Messages by lazy(LazyThreadSafetyMode.PUBLICATION) {
        sourceSegments.map { it.resolveToMessageElement() }.toMessages()
    }

    override val plainText: String? by lazy(LazyThreadSafetyMode.PUBLICATION) {
        var sb: StringBuilder? = null

        for (segment in sourceSegments) {
            if (segment is OneBotText) {
                (sb ?: StringBuilder().also { sb = it })
                    .append(segment.data.text)
            }
        }

        sb?.toString()
    }

    override suspend fun delete(vararg options: DeleteOption) {
        runCatching {
            DeleteMsgApi.create(id).requestDataBy(bot)
        }.onFailure { ex ->
            if (StandardDeleteOption.IGNORE_ON_FAILURE !in options) {
                throw ex
            }
        }
    }
}
