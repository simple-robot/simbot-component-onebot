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

import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import love.forte.simbot.bot.SerializableBotConfiguration
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext


/**
 * 用于 [OneBotBot] 的配置类。
 * [OneBotBotConfiguration] 本身可直接作为 [SerializableBotConfiguration]
 * 使用在 [OneBotBotManager] 中。
 * 不过需要主要的是，小部分配置属性可能不支持序列化。
 *
 * @author ForteScarlet
 */
@Serializable
public class OneBotBotConfiguration : SerializableBotConfiguration() {
    /**
     * 用于 Bot 的协程上下文实例。
     *
     * 如果其中包含 [Job][kotlinx.coroutines.Job]，
     * 则会在与 [OneBotBotManager]
     * 中（也可能存在的）[Job][kotlinx.coroutines.Job] 合并后，
     * 作为一个 parent Job 使用。
     */
    @Transient
    public var coroutineContext: CoroutineContext = EmptyCoroutineContext

    // TODO 更多其他属性...
}
