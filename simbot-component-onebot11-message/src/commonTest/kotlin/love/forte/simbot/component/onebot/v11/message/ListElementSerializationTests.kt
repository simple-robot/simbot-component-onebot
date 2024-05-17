package love.forte.simbot.component.onebot.v11.message

import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import love.forte.simbot.annotations.InternalSimbotAPI
import love.forte.simbot.common.id.IntID.Companion.ID
import love.forte.simbot.component.onebot.v11.message.segment.OneBotFace
import love.forte.simbot.component.onebot.v11.message.segment.OneBotMessageSegment
import love.forte.simbot.component.onebot.v11.message.segment.OneBotText
import love.forte.simbot.message.messageElementPolymorphic
import kotlin.test.Test
import kotlin.test.assertEquals


/**
 *
 * @author ForteScarlet
 */
class ListElementSerializationTests {
    @OptIn(InternalSimbotAPI::class)
    @Suppress("VariableNaming")
    private val defaultJson = Json {
        isLenient = true
        isLenient = true
        ignoreUnknownKeys = true
        allowSpecialFloatingPointValues = true
        prettyPrint = false
        serializersModule = SerializersModule {
            messageElementPolymorphic {
                includeAllComponentMessageElementImpls()
            }
            polymorphic(OneBotMessageElement::class) {
                includeAllComponentMessageElementImpls()
            }
            polymorphic(OneBotMessageSegment::class) {
                includeAllOneBotSegmentImpls()
            }
        }
    }

    @Test
    fun listElementTest() {
        val elementList: List<OneBotMessageElement> = listOf(
            OneBotText.create("Text"),
            OneBotFace.create(123.ID),
        )

        val jsonString = defaultJson.encodeToString(OneBotMessageElementSerializer, elementList)
        assertEquals(
            """[{"type":"text","data":{"text":"Text"}},{"type":"face","data":{"id":"123"}}]""",
            jsonString
        )
    }

}
