package love.forte.simbot.component.onebot.v11.core.api

import kotlinx.coroutines.test.runTest
import love.forte.simbot.component.findAndInstallAllComponents
import love.forte.simbot.component.onebot.v11.core.bot.firstOneBotBotManagerOrNull
import love.forte.simbot.component.onebot.v11.core.bot.useOneBot11BotManager
import love.forte.simbot.component.onebot.v11.core.component.OneBot11Component
import love.forte.simbot.component.onebot.v11.core.component.useOneBot11Component
import love.forte.simbot.component.onebot.v11.core.useOneBot11
import love.forte.simbot.core.application.launchSimpleApplication
import love.forte.simbot.plugin.findAndInstallAllPlugins
import kotlin.test.Test
import kotlin.test.assertNotNull
import kotlin.test.assertTrue


/**
 *
 * @author ForteScarlet
 */
class ApplicationUsageTests {

    @Test
    fun applicationUsageTest() = runTest {
        with(
            launchSimpleApplication {
                useOneBot11()
            }
        ) {
            assertTrue(components.any { it is OneBot11Component })
            assertNotNull(plugins.firstOneBotBotManagerOrNull())
            assertNotNull(botManagers.firstOneBotBotManagerOrNull())
        }
        with(
            launchSimpleApplication {
                useOneBot11Component()
                useOneBot11BotManager()
            }
        ) {
            assertTrue(components.any { it is OneBot11Component })
            assertNotNull(plugins.firstOneBotBotManagerOrNull())
            assertNotNull(botManagers.firstOneBotBotManagerOrNull())
        }

        with(
            launchSimpleApplication {
                findAndInstallAllComponents(false)
                findAndInstallAllPlugins(false)
            }
        ) {
            assertTrue(components.any { it is OneBot11Component })
            assertNotNull(plugins.firstOneBotBotManagerOrNull())
            assertNotNull(botManagers.firstOneBotBotManagerOrNull())
        }

        with(
            launchSimpleApplication {
                useOneBot11Component()
            }
        ) {
            assertTrue(components.any { it is OneBot11Component })
        }


    }


}
