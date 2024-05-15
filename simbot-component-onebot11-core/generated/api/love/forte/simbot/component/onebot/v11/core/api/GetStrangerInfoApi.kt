package love.forte.simbot.component.onebot.v11.core.api

import kotlin.Any
import kotlin.Boolean
import kotlin.Int
import kotlin.String
import kotlin.jvm.JvmOverloads
import kotlin.jvm.JvmStatic
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import love.forte.simbot.common.id.ID

/**
 * [`get_stranger_info`-获取陌生人信息](https://github.com/botuniverse/onebot-11/blob/master/api/public.md##get_stranger_info-获取陌生人信息)
 *
 * @author ForteScarlet
 */
public class GetStrangerInfoApi private constructor(
    override val body: Any,
) : OneBotApi<GetStrangerInfoResult> {
    override val action: String
        get() = ACTION

    override val resultDeserializer: DeserializationStrategy<GetStrangerInfoResult>
        get() = GetStrangerInfoResult.serializer()

    override val apiResultDeserializer:
        DeserializationStrategy<OneBotApiResult<GetStrangerInfoResult>>
        get() = RES_SER

    public companion object Factory {
        private const val ACTION: String = "get_stranger_info"

        private val RES_SER: KSerializer<OneBotApiResult<GetStrangerInfoResult>> =
            OneBotApiResult.serializer(GetStrangerInfoResult.serializer())

        /**
         * 构建一个 [GetStrangerInfoApi].
         *
         * @param userId QQ 号
         * @param noCache 是否不使用缓存（使用缓存可能更新不及时，但响应更快）
         */
        @JvmStatic
        @JvmOverloads
        public fun create(userId: ID, noCache: Boolean? = null): GetStrangerInfoApi =
            GetStrangerInfoApi(Body(userId, noCache))
    }

    /**
     * @param userId QQ 号
     * @param noCache 是否不使用缓存（使用缓存可能更新不及时，但响应更快）
     */
    @Serializable
    internal data class Body(
        @SerialName("user_id")
        internal val userId: ID,
        @SerialName("no_cache")
        internal val noCache: Boolean? = null,
    )
}

/**
 * [GetStrangerInfoApi] 的响应体。
 *
 * @param userId QQ 号
 * @param nickname 昵称
 * @param sex 性别，`male` 或 `female` 或 `unknown`
 * @param age 年龄
 */
@Serializable
public data class GetStrangerInfoResult @ApiResultType internal constructor(
    @SerialName("user_id")
    public val userId: ID,
    public val nickname: String,
    public val sex: String,
    public val age: Int,
)
