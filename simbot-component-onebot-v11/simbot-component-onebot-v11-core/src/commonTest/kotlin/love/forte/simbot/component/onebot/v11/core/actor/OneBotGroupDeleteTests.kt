package love.forte.simbot.component.onebot.v11.core.actor

import io.ktor.client.*
import io.ktor.client.engine.mock.*
import io.ktor.utils.io.core.*
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.booleanOrNull
import kotlinx.serialization.json.jsonPrimitive
import love.forte.simbot.common.id.IntID.Companion.ID
import love.forte.simbot.component.onebot.v11.core.actor.internal.OneBotGroupImpl
import love.forte.simbot.component.onebot.v11.core.api.SetGroupLeaveApi
import love.forte.simbot.component.onebot.v11.core.api.requestData
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertTrue


/**
 *
 * @author ForteScarlet
 */
class OneBotGroupDeleteTests {

    @Test
    fun oneBotGroupDeleteMarkTest() {
        var mark = OneBotGroupImpl.DeleteMark()
        assertFalse(mark.isDismiss)
        assertFalse(mark.isIgnoreFailure)

        mark = mark.dismiss()
        assertTrue(mark.isDismiss)

        mark = mark.ignoreFailure()
        assertTrue(mark.isIgnoreFailure)
    }

    @Test
    fun oneBotGroupDeleteTest() = runTest {
        HttpClient(
            MockEngine { reqData ->
                val json = reqData.body.toByteArray().decodeToString()
                val obj = Json.decodeFromString(JsonObject.serializer(), json)
                val dismiss = obj["is_dismiss"]?.jsonPrimitive?.booleanOrNull
                assertNotNull(dismiss)
                assertTrue(dismiss)
                respondOk("""{"retcode":0,"status":null,"data":null}""")
            }
        ).use { client ->
            SetGroupLeaveApi.create(123.ID, isDismiss = true).requestData(
                client,
                "127.0.0.1"
            )
        }

    }

}
