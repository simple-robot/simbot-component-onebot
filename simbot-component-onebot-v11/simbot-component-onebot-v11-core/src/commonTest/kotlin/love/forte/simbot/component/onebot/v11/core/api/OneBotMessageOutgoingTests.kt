package love.forte.simbot.component.onebot.v11.core.api

import kotlinx.serialization.json.Json
import love.forte.simbot.common.id.StringID.Companion.ID
import love.forte.simbot.component.onebot.v11.core.OneBot11
import love.forte.simbot.component.onebot.v11.message.segment.OneBotAt
import love.forte.simbot.component.onebot.v11.message.segment.OneBotFace
import kotlin.test.Test
import kotlin.test.assertEquals


/**
 *
 * @author ForteScarlet
 */
class OneBotMessageOutgoingTests {
    private val json: Json = OneBot11.DefaultJson

    @Test
    fun serializationTest() {
        with(OneBotMessageOutgoing.create("[CQ:at,code=123]")) {
            val jsonStr = json.encodeToString(OneBotMessageOutgoing.serializer(), this)
            assertEquals("\"[CQ:at,code=123]\"", jsonStr)
        }

        with(
            OneBotMessageOutgoing.create(
                listOf(
                    OneBotAt.create("123"),
                    OneBotFace.create("456".ID)
                )
            )
        ) {
            val jsonStr = json.encodeToString(OneBotMessageOutgoing.serializer(), this)
            assertEquals(
                """[{"type":"at","data":{"qq":"123"}},{"type":"face","data":{"id":"456"}}]""",
                jsonStr
            )
        }
    }

}
