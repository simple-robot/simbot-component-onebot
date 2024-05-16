package love.forte.simbot.component.onebot.v11.core.api

import kotlin.Any
import kotlin.Int
import kotlin.String
import kotlin.jvm.JvmOverloads
import kotlin.jvm.JvmStatic
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * [`get_credentials`-获取 QQ
 * 相关接口凭证](https://github.com/botuniverse/onebot-11/blob/master/api/public.md#get_credentials-获取-qq-相关接口凭证)
 *
 * @author ForteScarlet
 */
public class GetCredentialsApi private constructor(
    override val body: Any,
) : OneBotApi<GetCredentialsResult> {
    override val action: String
        get() = ACTION

    override val resultDeserializer: DeserializationStrategy<GetCredentialsResult>
        get() = GetCredentialsResult.serializer()

    override val apiResultDeserializer:
            DeserializationStrategy<OneBotApiResult<GetCredentialsResult>>
        get() = RES_SER

    public companion object Factory {
        private const val ACTION: String = "get_credentials"

        private val RES_SER: KSerializer<OneBotApiResult<GetCredentialsResult>> =
                OneBotApiResult.serializer(GetCredentialsResult.serializer())

        /**
         * 构建一个 [GetCredentialsApi].
         *
         * @param domain 需要获取 cookies 的域名
         */
        @JvmStatic
        @JvmOverloads
        public fun create(domain: String? = null): GetCredentialsApi =
                GetCredentialsApi(Body(domain))
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
 * [GetCredentialsApi] 的响应体。
 *
 * @param cookies Cookies
 * @param csrfToken CSRF Token
 */
@Serializable
public data class GetCredentialsResult @ApiResultType internal constructor(
    public val cookies: String,
    @SerialName("csrf_token")
    public val csrfToken: Int,
)
