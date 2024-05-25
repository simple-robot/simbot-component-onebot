package love.forte.simbot.component.onebot.v11.core.api

import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.plus
import love.forte.simbot.common.id.IntID.Companion.ID
import love.forte.simbot.component.onebot.v11.core.OneBot11
import love.forte.simbot.component.onebot.v11.message.segment.OneBotAt
import love.forte.simbot.component.onebot.v11.message.segment.add
import love.forte.simbot.message.At
import love.forte.simbot.message.Messages
import love.forte.simbot.message.buildMessages
import love.forte.simbot.message.encodeMessagesToString
import kotlin.test.Test


/**
 *
 * @author ForteScarlet
 */
class OneBotMessageElementTests {

    @Test
    fun elementSerializationTest() {
        val json = Json(OneBot11.DefaultJson) {
            serializersModule = Messages.standardSerializersModule + serializersModule
        }

        val msgList = buildMessages {
            add(At(123.ID))
            add(OneBotAt.create(1.ID))
        }

        println(json.encodeMessagesToString(msgList))
    }

}
