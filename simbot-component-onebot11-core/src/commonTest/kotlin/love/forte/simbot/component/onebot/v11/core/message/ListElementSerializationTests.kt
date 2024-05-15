package love.forte.simbot.component.onebot.v11.core.message

import love.forte.simbot.common.id.IntID.Companion.ID
import love.forte.simbot.component.onebot.v11.core.OneBot11
import love.forte.simbot.component.onebot.v11.core.message.segment.OneBotFace
import love.forte.simbot.component.onebot.v11.core.message.segment.OneBotText
import kotlin.test.Test
import kotlin.test.assertEquals


/**
 *
 * @author ForteScarlet
 */
class ListElementSerializationTests {

    @Test
    fun listElementTest() {
        val elementList: List<OneBotMessageElement> = listOf(
            OneBotText.create("Text"),
            OneBotFace.create(123.ID),
        )

        val jsonString = OneBot11.DefaultJson.encodeToString(OneBotMessageElementSerializer, elementList)
        assertEquals(
            """[{"type":"text","data":{"text":"Text"}},{"type":"face","data":{"id":"123"}}]""",
            jsonString
        )
    }

}
