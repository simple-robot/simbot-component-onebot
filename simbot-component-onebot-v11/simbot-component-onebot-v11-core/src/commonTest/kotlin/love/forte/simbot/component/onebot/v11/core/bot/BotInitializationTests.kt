package love.forte.simbot.component.onebot.v11.core.bot

import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.test.runTest
import love.forte.simbot.component.onebot.v11.core.OneBot11
import love.forte.simbot.component.onebot.v11.core.bot.internal.OneBotBotImpl
import love.forte.simbot.component.onebot.v11.core.component.OneBot11Component
import love.forte.simbot.event.Event
import love.forte.simbot.event.EventProcessor
import love.forte.simbot.event.EventResult
import kotlin.coroutines.EmptyCoroutineContext
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

/**
 *
 * @author ForteScarlet
 */
class BotInitializationTests {
    private fun bot() = OneBotBotImpl(
        uniqueId = "UNIQUE_ID",
        coroutineContext = EmptyCoroutineContext,
        job = SupervisorJob(),
        configuration = OneBotBotConfiguration(),
        component = OneBot11Component(),
        eventProcessor = object : EventProcessor {
            override fun push(event: Event): Flow<EventResult> {
                TODO("Not yet implemented")
            }
        },
        baseDecoderJson = OneBot11.DefaultJson
    )

    @Test
    fun testBotInit() = runTest {
        val bot = bot()

        assertFalse(bot.isConfigurationInitialized)
        assertFalse(bot.isConfigurationInitializing)

        assertTrue(bot.initConfiguration())
        assertTrue(bot.isConfigurationInitialized)
        assertFalse(bot.initConfiguration())

        assertTrue(bot.isConfigurationInitialized)
        bot.cancel()
    }

    @Test
    fun testBotStartInit() = runTest {
        val bot = bot()

        assertFalse(bot.isConfigurationInitialized)
        assertFalse(bot.isConfigurationInitializing)

        runCatching { bot.start() }

        assertTrue(bot.isConfigurationInitialized)
        assertFalse(bot.initConfiguration())

        assertTrue(bot.isConfigurationInitialized)

        bot.cancel()
    }



}
