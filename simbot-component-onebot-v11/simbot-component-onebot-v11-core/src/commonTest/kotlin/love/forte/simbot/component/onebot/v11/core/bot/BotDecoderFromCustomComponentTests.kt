package love.forte.simbot.component.onebot.v11.core.bot

import io.ktor.client.engine.mock.*
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.PolymorphicSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.jsonPrimitive
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.modules.subclass
import love.forte.simbot.annotations.FragileSimbotAPI
import love.forte.simbot.application.Application
import love.forte.simbot.common.function.ConfigurerFunction
import love.forte.simbot.common.id.UUID
import love.forte.simbot.component.Component
import love.forte.simbot.component.ComponentConfigureContext
import love.forte.simbot.component.ComponentFactory
import love.forte.simbot.component.onebot.v11.core.oneBot11Bots
import love.forte.simbot.component.onebot.v11.core.useOneBot11
import love.forte.simbot.component.onebot.v11.message.segment.OneBotAt
import love.forte.simbot.component.onebot.v11.message.segment.OneBotDice
import love.forte.simbot.component.onebot.v11.message.segment.OneBotMessageSegment
import love.forte.simbot.component.onebot.v11.message.segment.OneBotUnknownSegment
import love.forte.simbot.core.application.launchSimpleApplication
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertTrue


/**
 *
 * @author ForteScarlet
 */
class BotDecoderFromCustomComponentTests {

    @Test
    fun botDecoderWithCustomComponentTest() = runTest {
        val app = launchSimpleApplication {
            useOneBot11()
            install(TestCustomComponentFac)
        }

        val testSeg = doTest(app)
        assertIs<TestSegment>(testSeg)
        assertEquals("test", testSeg.data)
    }

    @OptIn(FragileSimbotAPI::class)
    @Test
    fun botDecoderWithoutCustomComponentTest() = runTest {
        val app = launchSimpleApplication {
            useOneBot11()
        }

        val testSeg = doTest(app)
        assertIs<OneBotUnknownSegment>(testSeg)
        assertEquals("test", testSeg.data!!.jsonPrimitive.content)
    }

    private suspend fun doTest(app: Application): OneBotMessageSegment {
        app.oneBot11Bots {
            val bot = register(
                OneBotBotConfiguration().apply {
                    botUniqueId = UUID.random().toString()
                    MockEngine {
                        respondOk()
                    }.apply {
                        wsClientEngine = this
                        apiClientEngine = this
                    }
                }
            )
            bot.initConfiguration()

            val segments = doSerial(bot)
            assertEquals(3, segments.size)
            val m1 = segments[0]
            val m2 = segments[1]
            val m3 = segments[2]
            assertIs<OneBotDice>(m1)
            assertIs<OneBotAt>(m3)
            assertEquals("all", m3.data.qq)
            assertTrue(m3.isAll)

            return m2
        }

        error("no")
    }

    private fun doSerial(bot: OneBotBot): List<OneBotMessageSegment> {
        val jsonStr = """[
          {"type": "dice", "data": {}},
          {"type": "test", "data": "test"},
          {"type": "at", "data": {"qq": "all"}}
        ]
        """.trimIndent()

        return bot.decoderJson.decodeFromString(
            ListSerializer(PolymorphicSerializer(OneBotMessageSegment::class)),
            jsonStr
        )
    }
}


private val TestCustomComponent = object : Component {
    override val id: String = "TestCustomComponent"

    override val serializersModule: SerializersModule = SerializersModule {
        polymorphic(OneBotMessageSegment::class) {
            subclass(TestSegment.serializer())
        }
    }
}

private val TestCustomComponentFac = object : ComponentFactory<Component, Unit> {
    override val key: ComponentFactory.Key = object : ComponentFactory.Key {}

    override fun create(context: ComponentConfigureContext, configurer: ConfigurerFunction<Unit>): Component {
        return TestCustomComponent
    }
}


@SerialName("test")
@Serializable
private data class TestSegment(override val data: String) : OneBotMessageSegment
