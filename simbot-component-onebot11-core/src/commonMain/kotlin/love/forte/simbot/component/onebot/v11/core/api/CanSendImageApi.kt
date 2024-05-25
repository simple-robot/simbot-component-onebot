package love.forte.simbot.component.onebot.v11.core.api

import kotlin.Any
import kotlin.Boolean
import kotlin.String
import kotlin.jvm.JvmStatic
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import love.forte.simbot.component.onebot.v11.common.api.ApiResultConstructor

/**
 * [`can_send_image`-检查是否可以发送图片](https://github.com/botuniverse/onebot-11/blob/master/api/public.md#can_send_image-检查是否可以发送图片)
 *
 * @author ForteScarlet
 */
public class CanSendImageApi private constructor() : OneBotApi<CanSendImageResult> {
    override val action: String
        get() = ACTION

    override val resultDeserializer: DeserializationStrategy<CanSendImageResult>
        get() = CanSendImageResult.serializer()

    override val apiResultDeserializer: DeserializationStrategy<OneBotApiResult<CanSendImageResult>>
        get() = RES_SER

    override val body: Any?
        get() = null

    public companion object Factory {
        private const val ACTION: String = "can_send_image"

        private val RES_SER: KSerializer<OneBotApiResult<CanSendImageResult>> =
            OneBotApiResult.serializer(CanSendImageResult.serializer())

        private val INSTANCE: CanSendImageApi = CanSendImageApi()

        /**
         * 构建一个 [CanSendImageApi].
         */
        @JvmStatic
        public fun create(): CanSendImageApi = INSTANCE
    }
}

/**
 * [CanSendImageApi] 的响应体。
 *
 * @param yes 是或否
 */
@Serializable
public data class CanSendImageResult @ApiResultConstructor internal constructor(
    public val yes: Boolean,
)
