package love.forte.simbot.component.onebot.v11.core.api

import kotlin.Any
import kotlin.Int
import kotlin.String
import kotlin.jvm.JvmStatic
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import love.forte.simbot.component.onebot.v11.common.api.ApiResultConstructor

/**
 * [`get_csrf_token`-获取 CSRF Token](https://github.com/botuniverse/onebot-11/blob/master/api/public.md#get_csrf_token-获取-csrf-token)
 *
 * @author ForteScarlet
 */
public class GetCsrfTokenApi private constructor() : OneBotApi<GetCsrfTokenResult> {
    override val action: String
        get() = ACTION

    override val resultDeserializer: DeserializationStrategy<GetCsrfTokenResult>
        get() = GetCsrfTokenResult.serializer()

    override val apiResultDeserializer: DeserializationStrategy<OneBotApiResult<GetCsrfTokenResult>>
        get() = RES_SER

    override val body: Any?
        get() = null

    public companion object Factory {
        private const val ACTION: String = "get_csrf_token"

        private val RES_SER: KSerializer<OneBotApiResult<GetCsrfTokenResult>> =
            OneBotApiResult.serializer(GetCsrfTokenResult.serializer())

        private val INSTANCE: GetCsrfTokenApi = GetCsrfTokenApi()

        /**
         * 构建一个 [GetCsrfTokenApi].
         */
        @JvmStatic
        public fun create(): GetCsrfTokenApi = INSTANCE
    }
}

/**
 * [GetCsrfTokenApi] 的响应体。
 *
 * @param token CSRF Token
 */
@Serializable
public data class GetCsrfTokenResult @ApiResultConstructor internal constructor(
    public val token: Int,
)
