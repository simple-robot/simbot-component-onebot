package love.forte.simbot.component.onebot.v11.core.actor

import io.ktor.client.*
import io.ktor.client.engine.mock.*
import io.ktor.utils.io.core.*
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.booleanOrNull
import kotlinx.serialization.json.jsonPrimitive
import love.forte.simbot.common.id.UIntID.Companion.ID
import love.forte.simbot.common.id.ULongID.Companion.ID
import love.forte.simbot.component.onebot.v11.core.actor.internal.OneBotMemberImpl
import love.forte.simbot.component.onebot.v11.core.api.SetGroupKickApi
import love.forte.simbot.component.onebot.v11.core.api.requestData
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertTrue


/**
 *
 * @author ForteScarlet
 */
class OneBotMemberDeleteTests {
    @Test
    fun oneBotMemberDeleteMarkTest() {
        var mark = OneBotMemberImpl.DeleteMark()
        assertFalse(mark.isRejectRequest)
        assertFalse(mark.isIgnoreFailure)

        mark = mark.rejectRequest()
        assertTrue(mark.isRejectRequest)

        mark = mark.ignoreFailure()
        assertTrue(mark.isIgnoreFailure)
    }

    @Test
    fun oneBotMemberDeleteTest() = runTest {
        HttpClient(
            MockEngine { reqData ->
                val json = reqData.body.toByteArray().decodeToString()
                val obj = Json.decodeFromString(JsonObject.serializer(), json)
                val rejectRequest = obj["reject_add_request"]?.jsonPrimitive?.booleanOrNull
                assertNotNull(rejectRequest)
                assertTrue(rejectRequest)
                respondOk("""{"retcode":0,"status":null,"data":null}""")
            }
        ).use { client ->
            SetGroupKickApi.create(ULong.MAX_VALUE.ID, UInt.MAX_VALUE.ID, rejectAddRequest = true).requestData(
                client,
                "127.0.0.1"
            )
        }

    }

}
