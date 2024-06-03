package love.forte.simbot.component.onebot.v11.core.api

import io.ktor.http.Url
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import love.forte.simbot.bot.SerializableBotConfiguration
import love.forte.simbot.component.onebot.v11.core.bot.OneBotBotSerializableConfiguration
import love.forte.simbot.component.onebot.v11.core.useOneBot11
import love.forte.simbot.core.application.launchSimpleApplication
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs


/**
 *
 * @author ForteScarlet
 */
class ApplicationSerializableConfigurationTests {


    @Test
    fun serializableConfigurationTest() = runTest {
        val app = launchSimpleApplication { useOneBot11() }

        val json = Json {
            isLenient = true
            ignoreUnknownKeys = true
            serializersModule = app.components.serializersModule
        }

        val configuration = json.decodeFromString(
            SerializableBotConfiguration.serializer(),
            """{
              "component": "simbot.onebot11",
              "authorization": {
                "botUniqueId": "123456",
                "apiServerHost": "http://localhost:8080",
                "eventServerHost": "ws://localhost:9090"
              },
              "config": {
                "apiHttpRequestTimeoutMillis": 99999,
                "apiHttpConnectTimeoutMillis": 99999,
                "apiHttpSocketTimeoutMillis": 99999,
                "wsConnectMaxRetryTimes": 99999,
                "wsConnectRetryDelayMillis": 99999
              }
            }"""
        )

        assertIs<OneBotBotSerializableConfiguration>(configuration)

        val conf = configuration.toConfiguration()
        assertEquals("123456", conf.botUniqueId)
        assertEquals(Url("http://localhost:8080"), conf.apiServerHost)
        assertEquals(Url("ws://localhost:9090"), conf.eventServerHost)
        assertEquals(99999, conf.apiHttpRequestTimeoutMillis)
        assertEquals(99999, conf.apiHttpConnectTimeoutMillis)
        assertEquals(99999, conf.apiHttpSocketTimeoutMillis)
        assertEquals(99999, conf.wsConnectMaxRetryTimes)
        assertEquals(99999, conf.wsConnectRetryDelayMillis)

    }

}
