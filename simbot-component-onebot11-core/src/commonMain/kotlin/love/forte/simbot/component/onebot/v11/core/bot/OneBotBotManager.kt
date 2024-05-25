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

import love.forte.simbot.bot.*
import love.forte.simbot.common.function.ConfigurerFunction
import love.forte.simbot.plugin.PluginConfigureContext
import love.forte.simbot.plugin.PluginFactory


/**
 * 一个用于管理 [OneBotBot] 的 [BotManager]。
 *
 * @author ForteScarlet
 */
public abstract class OneBotBotManager : BotManager {
    /**
     * 检测 [configuration] 的类型是否为 [OneBotBotConfiguration]。
     */
    override fun configurable(configuration: SerializableBotConfiguration): Boolean =
        configuration is OneBotBotConfiguration

    /**
     * 使用 [configuration] 构建一个 [OneBotBot]。
     * @throws UnsupportedBotConfigurationException [configuration] 类型不符合预期 [OneBotBotConfiguration]
     * @throws BotRegisterFailureException 注册过程出现异常
     */
    override fun register(configuration: SerializableBotConfiguration): OneBotBot {
        val obConfig = configuration as? OneBotBotConfiguration
            ?: throw UnsupportedBotConfigurationException(
                "`configuration` is expected to be an instance of `OneBotBotConfiguration`, " +
                    "but is $configuration (${configuration::class})"
            )

        return register0(obConfig)
    }

    /**
     * 使用 [OneBotBotConfiguration] 构建 [OneBotBot]。
     *
     * @throws BotRegisterFailureException 注册过程出现异常
     */
    protected abstract fun register0(configuration: OneBotBotConfiguration): OneBotBot

    public companion object Factory : BotManagerFactory<OneBotBotManager, OneBotBotManagerConfiguration> {
        override val key: PluginFactory.Key = object : PluginFactory.Key {}

        override fun create(
            context: PluginConfigureContext,
            configurer: ConfigurerFunction<OneBotBotManagerConfiguration>
        ): OneBotBotManager {
            TODO("Not yet implemented")
        }
    }
}

/**
 * [OneBotBotManager] 的工厂配置类。
 */
public class OneBotBotManagerConfiguration

// TODO SPI provider
