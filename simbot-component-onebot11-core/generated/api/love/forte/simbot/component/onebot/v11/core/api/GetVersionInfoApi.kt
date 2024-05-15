package love.forte.simbot.component.onebot.v11.core.api

import kotlin.Any
import kotlin.Nothing
import kotlin.String
import kotlin.jvm.JvmStatic
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * [`get_version_info`-获取版本信息](https://github.com/botuniverse/onebot-11/blob/master/api/public.md##get_version_info-获取版本信息)
 *
 * @author ForteScarlet
 */
public class GetVersionInfoApi private constructor() : OneBotApi<GetVersionInfoResult> {
    override val action: String
        get() = ACTION

    override val resultDeserializer: DeserializationStrategy<GetVersionInfoResult>
        get() = GetVersionInfoResult.serializer()

    override val apiResultDeserializer:
        DeserializationStrategy<OneBotApiResult<GetVersionInfoResult>>
        get() = RES_SER

    override val body: Any?
        get() = null

    public companion object Factory {
        private const val ACTION: String = "get_version_info"

        private val RES_SER: KSerializer<OneBotApiResult<GetVersionInfoResult>> =
            OneBotApiResult.serializer(GetVersionInfoResult.serializer())

        private val INSTANCE: GetVersionInfoApi = GetVersionInfoApi()

        /**
         * 构建一个 [GetVersionInfoApi].
         */
        @JvmStatic
        public fun create(): GetVersionInfoApi = INSTANCE
    }
}

/**
 * [GetVersionInfoApi] 的响应体。
 *
 * @param appName 应用标识，如 `mirai-native`
 * @param appVersion 应用版本，如 `1.2.3`
 * @param protocolVersion OneBot 标准版本，如 `v11`
 * @param …… OneBot 实现自行添加的其它内容
 */
@Serializable
public data class GetVersionInfoResult @ApiResultType internal constructor(
    @SerialName("app_name")
    public val appName: String,
    @SerialName("app_version")
    public val appVersion: String,
    @SerialName("protocol_version")
    public val protocolVersion: String,
    public val `……`: Nothing = TODO("……?"),
)
