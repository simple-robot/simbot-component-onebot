package love.forte.simbot.component.onebot.v11.core.api

import kotlinx.serialization.json.Json
import kotlin.test.*


/**
 *
 * @author ForteScarlet
 */
class OneBotApiEmptySerializerTest {
    val json = Json {
        isLenient = true
        ignoreUnknownKeys = true
    }

    @Test
    fun deserializeNullDataTest() {
        val jsonText = """
            {"retcode":0,"status":"OK","data":null}
        """.trim()

        val data = json.decodeFromString(
            OneBotApiResult.emptySerializer(),
            jsonText
        )

        assertTrue(data.isSuccess)
        assertEquals(Unit, data.data)
    }

    @Test
    fun deserializeNoFieldDataTest() {
        val jsonText = """
            {"retcode":0,"status":"OK"}
        """.trim()

        val data = json.decodeFromString(
            OneBotApiResult.emptySerializer(),
            jsonText
        )

        assertTrue(data.isSuccess)
        assertEquals(Unit, data.data)
    }

    @Test
    fun deserializeFailedDataTest() {
        val jsonText = """
            {"retcode":100,"status":"failed"}
        """.trim()

        val data = json.decodeFromString(
            OneBotApiResult.emptySerializer(),
            jsonText
        )

        assertFalse(data.isSuccess)
        assertNull(data.data)
    }

}
