package love.forte.simbot.component.onebot.v11.message

import love.forte.simbot.component.onebot.v11.message.segment.OneBotImage
import love.forte.simbot.component.onebot.v11.message.segment.OneBotRecord
import love.forte.simbot.component.onebot.v11.message.segment.OneBotVideo
import love.forte.simbot.resource.toResource
import kotlin.io.encoding.ExperimentalEncodingApi
import kotlin.test.Test
import kotlin.test.assertEquals


/**
 *
 * @author ForteScarlet
 */
class ResourceMessageBase64Tests {
    companion object {
        private const val BASE64_D = "qr6f9jnXeM/3txdpG94OQA=="
        private const val BASE64_U = "qr6f9jnXeM_3txdpG94OQA=="

        @OptIn(ExperimentalStdlibApi::class)
        private val original = "AABE9FF639D778CFF7B717691BDE0E40".hexToByteArray()
    }

    @OptIn(ExperimentalEncodingApi::class)
    @Test
    fun imageBase64DefaultTest() {
        assertEquals(
            "base64://$BASE64_D",
            OneBotImage.create(
                original.toResource(),
            ).data.file
        )

        assertEquals(
            "base64://$BASE64_U",
            OneBotImage.create(
                original.toResource(),
                OneBotImage.AdditionalParams().apply {
                    base64Encoder = Base64Encoder.UrlSafe
                }
            ).data.file
        )
    }

    @OptIn(ExperimentalEncodingApi::class)
    @Test
    fun videoBase64DefaultTest() {
        assertEquals(
            "base64://$BASE64_D",
            OneBotVideo.create(
                original.toResource(),
            ).data.file
        )

        assertEquals(
            "base64://$BASE64_U",
            OneBotVideo.create(
                original.toResource(),
                OneBotVideo.AdditionalParams().apply {
                    base64Encoder = Base64Encoder.UrlSafe
                }
            ).data.file
        )
    }

    @OptIn(ExperimentalEncodingApi::class)
    @Test
    fun recordBase64DefaultTest() {
        assertEquals(
            "base64://$BASE64_D",
            OneBotRecord.create(
                original.toResource(),
            ).data.file
        )

        assertEquals(
            "base64://$BASE64_U",
            OneBotRecord.create(
                original.toResource(),
                OneBotRecord.AdditionalParams().apply {
                    base64Encoder = Base64Encoder.UrlSafe
                }
            ).data.file
        )
    }

}
