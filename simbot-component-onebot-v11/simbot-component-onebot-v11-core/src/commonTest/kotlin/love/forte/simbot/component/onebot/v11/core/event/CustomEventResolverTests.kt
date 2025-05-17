package love.forte.simbot.component.onebot.v11.core.event

import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import love.forte.simbot.annotations.ExperimentalSimbotAPI
import love.forte.simbot.application.listeners
import love.forte.simbot.common.atomic.atomic
import love.forte.simbot.common.id.ID
import love.forte.simbot.common.id.UUID
import love.forte.simbot.common.time.Timestamp
import love.forte.simbot.component.onebot.v11.core.bot.OneBotBot
import love.forte.simbot.component.onebot.v11.core.bot.OneBotBotConfiguration
import love.forte.simbot.component.onebot.v11.core.bot.addCustomKotlinSerializationEventResolver
import love.forte.simbot.component.onebot.v11.core.bot.register
import love.forte.simbot.component.onebot.v11.core.oneBot11Bots
import love.forte.simbot.component.onebot.v11.core.useOneBot11
import love.forte.simbot.core.application.launchSimpleApplication
import love.forte.simbot.event.Event
import love.forte.simbot.event.EventListenerRegistrar
import love.forte.simbot.event.FuzzyEventTypeImplementation
import love.forte.simbot.event.process
import kotlin.test.Test
import kotlin.test.assertEquals

/**
 *
 * @author ForteScarlet
 */
class CustomEventResolverTests {
    /**
     * postType: custom
     *
     * subType: test1
     *
     * ```json
     * {
     *   "post_type": "custom",
     *   "custom_type": "test1",
     *   "value": "test1"
     * }
     * ```
     */
    @OptIn(FuzzyEventTypeImplementation::class)
    @Serializable
    private data class CustomEvent1(
        @SerialName("post_type")
        val postType: String,
        @SerialName("custom_type")
        val customType: String,
        val value: String
    ) : Event {
        override val id: ID = UUID.random()

        @OptIn(ExperimentalSimbotAPI::class)
        override val time: Timestamp = Timestamp.now()
    }

    private suspend inline fun bot(
        eventHandle: EventListenerRegistrar.() -> Unit = {},
        config: OneBotBotConfiguration.() -> Unit = {}
    ): OneBotBot {
        val app = launchSimpleApplication {
            useOneBot11()
        }

        app.listeners {
            eventHandle()
        }

        app.oneBot11Bots {
            return register {
                botUniqueId = UUID.random().toString()
                config()
            }
        }
    }

    @Test
    fun testCustomEventResolver() = runTest {
        val count = atomic(0)

        val bot = bot({
            process<CustomEvent1> { event ->
                assertEquals("custom", event.postType)
                assertEquals("test1", event.customType)
                assertEquals("Hello, World", event.value)
                count.incrementAndGet()
            }
        }) {
            addCustomEventResolver { context ->
                val rawEventResolveResult = context.rawEventResolveResult
                if (rawEventResolveResult.postType == "custom" && rawEventResolveResult.subType == "test1") {
                    return@addCustomEventResolver context.json.decodeFromJsonElement(
                        CustomEvent1.serializer(),
                        rawEventResolveResult.json
                    )
                }

                null
            }
        }

        bot.push(
            """
            {
              "post_type": "custom",
              "custom_type": "test1",
              "value": "Hello, World"
            }
        """.trimIndent()
        ).collect()

        assertEquals(1, count.value)
    }

    @Test
    fun testCustomEventResolverByKtxSerialization() = runTest {
        val count = atomic(0)

        val bot = bot({
            process<CustomEvent1> { event ->
                assertEquals("custom", event.postType)
                assertEquals("test1", event.customType)
                assertEquals("Hello, World", event.value)
                count.incrementAndGet()
            }
        }) {
            addCustomKotlinSerializationEventResolver("custom", "test1") {
                CustomEvent1.serializer()
            }
        }

        bot.push(
            """
            {
              "post_type": "custom",
              "custom_type": "test1",
              "value": "Hello, World"
            }
        """.trimIndent()
        ).collect()

        assertEquals(1, count.value)
    }

}
