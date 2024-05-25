package love.forte.simbot.component.onebot.v11.core.api

import io.ktor.client.*
import io.ktor.client.engine.mock.*
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.KSerializer
import love.forte.simbot.common.id.IntID.Companion.ID
import love.forte.simbot.common.id.literal
import love.forte.simbot.component.onebot.v11.common.api.ApiResultConstructor
import love.forte.simbot.component.onebot.v11.core.OneBot11
import love.forte.simbot.component.onebot.v11.message.segment.OneBotFace
import love.forte.simbot.component.onebot.v11.message.segment.OneBotText
import love.forte.simbot.logger.LoggerFactory
import kotlin.test.Test
import kotlin.test.assertEquals


/**
 *
 * @author ForteScarlet
 */
@OptIn(ApiResultConstructor::class)
class ApiRequestTests {
    private val logger = LoggerFactory.getLogger("love.forte.simbot.component.onebot.ApiRequestTests")

    private inline fun <R : Any> createClient(
        action: String,
        crossinline respDataSer: () -> KSerializer<R>,
        crossinline respData: () -> R
    ): HttpClient {
        return HttpClient(
            MockEngine { req ->
                assertEquals(action, req.url.pathSegments.last())
                logger.debug("Data: {}", req.body.toByteArray().decodeToString())
                respondOk(
                    OneBot11.DefaultJson.encodeToString(
                        OneBotApiResult.serializer(respDataSer()),
                        OneBotApiResult(
                            OneBotApiResult.SUCCESS_RETCODE,
                            null,
                            respData()
                        )
                    )
                )
            }
        )
    }

    @Test
    fun sendPrivateMsgApiTest() = runTest {
        val client = createClient(
            "send_private_msg",
            respDataSer = { SendPrivateMsgResult.serializer() },
            respData = { SendPrivateMsgResult(123.ID) }
        )

        val data = SendPrivateMsgApi.create(
            123.ID,
            listOf(
                OneBotText.create("Hello, World"),
                OneBotFace.create(555.ID)
            ),
        ).requestData(
            client,
            "http://127.0.0.1:8080/",
        )

        assertEquals("123", data.messageId.literal)
    }

}
