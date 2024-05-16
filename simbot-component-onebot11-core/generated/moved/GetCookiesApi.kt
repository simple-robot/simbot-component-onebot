package love.forte.simbot.component.onebot.v11.core.api

import kotlin.Any
import kotlin.String
import kotlin.jvm.JvmOverloads
import kotlin.jvm.JvmStatic
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable

/**
 * [`get_cookies`-获取
 * Cookies](https://github.com/botuniverse/onebot-11/blob/master/api/public.md#get_cookies-获取-cookies)
 *
 * @author ForteScarlet
 */
public class GetCookiesApi private constructor(
    override val body: Any,
) : OneBotApi<GetCookiesResult> {
    override val action: String
        get() = ACTION

    override val resultDeserializer: DeserializationStrategy<GetCookiesResult>
        get() = GetCookiesResult.serializer()

    override val apiResultDeserializer: DeserializationStrategy<OneBotApiResult<GetCookiesResult>>
        get() = RES_SER

    public companion object Factory {
        private const val ACTION: String = "get_cookies"

        private val RES_SER: KSerializer<OneBotApiResult<GetCookiesResult>> =
                OneBotApiResult.serializer(GetCookiesResult.serializer())

        /**
         * 构建一个 [GetCookiesApi].
         *
         * @param domain 需要获取 cookies 的域名
         */
        @JvmStatic
        @JvmOverloads
        public fun create(domain: String? = null): GetCookiesApi = GetCookiesApi(Body(domain))
    }

    /**
     * @param domain 需要获取 cookies 的域名
     */
    @Serializable
    internal data class Body(
        internal val domain: String? = null,
    )
}

/**
 * [GetCookiesApi] 的响应体。
 *
 * @param cookies Cookies
 */
@Serializable
public data class GetCookiesResult @ApiResultType internal constructor(
    public val cookies: String,
)
