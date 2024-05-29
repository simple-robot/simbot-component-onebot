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

package love.forte.simbot.component.onebot.v11.core.bot.internal

import kotlinx.coroutines.Job
import love.forte.simbot.bot.BotRegisterFailureException
import love.forte.simbot.bot.NoSuchBotException
import love.forte.simbot.common.collection.computeValue
import love.forte.simbot.common.collection.concurrentMutableMap
import love.forte.simbot.common.collection.removeValue
import love.forte.simbot.common.coroutines.mergeWith
import love.forte.simbot.common.id.ID
import love.forte.simbot.common.id.literal
import love.forte.simbot.component.onebot.v11.core.OneBot11Component
import love.forte.simbot.component.onebot.v11.core.bot.OneBotBot
import love.forte.simbot.component.onebot.v11.core.bot.OneBotBotConfiguration
import love.forte.simbot.component.onebot.v11.core.bot.OneBotBotManager
import love.forte.simbot.event.EventProcessor
import kotlin.coroutines.CoroutineContext


/**
 * [OneBotBotManager] 的实现
 * @author ForteScarlet
 */
internal class OneBotBotManagerImpl(
    override val job: Job,
    private val coroutineContext: CoroutineContext,
    private val component: OneBot11Component,
    private val eventProcessor: EventProcessor
) : OneBotBotManager() {
    private val bots = concurrentMutableMap<String, OneBotBot>()

    override fun all(): Sequence<OneBotBot> =
        bots.values.asSequence()

    override fun register(configuration: OneBotBotConfiguration): OneBotBot {
        val uniqueId = requireNotNull(configuration.botUniqueId) {
            "Required `botUniqueId` is null"
        }

        val configContext = configuration.coroutineContext
        val mergedContext = configContext.mergeWith(coroutineContext)
        val job = mergedContext[Job]!!

        fun createBot(): OneBotBotImpl =
            OneBotBotImpl(
                uniqueId,
                mergedContext,
                job,
                configuration,
                component,
                eventProcessor,
            )

        val created = bots.computeValue(
            uniqueId,
        ) { key, old ->
            if (old == null || !old.isActive) {
                createBot()
            } else {
                throw BotRegisterFailureException("Conflict bot with unique id $key")
            }
        }!!

        created.onCompletion {
            bots.removeValue(uniqueId) { created }
        }

        return created
    }

    override fun get(id: ID): OneBotBot =
        find(id) ?: throw NoSuchBotException(id.toString())

    override fun find(id: ID): OneBotBot? =
        bots[id.literal]
}
