package love.forte.simbot.component.onebot.v11.core.api

import kotlin.Any
import kotlin.String
import kotlin.jvm.JvmStatic
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import love.forte.simbot.common.id.LongID

/**
 * [`get_login_info`-获取登录号信息](https://github.com/botuniverse/onebot-11/blob/master/api/public.md#get_login_info-获取登录号信息)
 *
 * @author ForteScarlet
 */
public class GetLoginInfoApi private constructor() : OneBotApi<GetLoginInfoResult> {
    override val action: String
        get() = ACTION

    override val resultDeserializer: DeserializationStrategy<GetLoginInfoResult>
        get() = GetLoginInfoResult.serializer()

    override val apiResultDeserializer: DeserializationStrategy<OneBotApiResult<GetLoginInfoResult>>
        get() = RES_SER

    override val body: Any?
        get() = null

    public companion object Factory {
        private const val ACTION: String = "get_login_info"

        private val RES_SER: KSerializer<OneBotApiResult<GetLoginInfoResult>> =
                OneBotApiResult.serializer(GetLoginInfoResult.serializer())

        private val INSTANCE: GetLoginInfoApi = GetLoginInfoApi()

        /**
         * 构建一个 [GetLoginInfoApi].
         */
        @JvmStatic
        public fun create(): GetLoginInfoApi = INSTANCE
    }
}

/**
 * [GetLoginInfoApi] 的响应体。
 *
 * @param userId QQ 号
 * @param nickname QQ 昵称
 */
@Serializable
public data class GetLoginInfoResult @ApiResultType internal constructor(
    @SerialName("user_id")
    public val userId: LongID,
    public val nickname: String,
)
