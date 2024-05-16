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
import love.forte.simbot.common.id.LongID

/**
 * [`get_group_info`-获取群信息](https://github.com/botuniverse/onebot-11/blob/master/api/public.md#get_group_info-获取群信息)
 *
 * @author ForteScarlet
 */
public class GetGroupInfoApi private constructor(
    override val body: Any,
) : OneBotApi<GetGroupInfoResult> {
    override val action: String
        get() = ACTION

    override val resultDeserializer: DeserializationStrategy<GetGroupInfoResult>
        get() = GetGroupInfoResult.serializer()

    override val apiResultDeserializer: DeserializationStrategy<OneBotApiResult<GetGroupInfoResult>>
        get() = RES_SER

    public companion object Factory {
        private const val ACTION: String = "get_group_info"

        private val RES_SER: KSerializer<OneBotApiResult<GetGroupInfoResult>> =
                OneBotApiResult.serializer(GetGroupInfoResult.serializer())

        /**
         * 构建一个 [GetGroupInfoApi].
         *
         * @param groupId 群号
         * @param noCache 是否不使用缓存（使用缓存可能更新不及时，但响应更快）
         */
        @JvmStatic
        @JvmOverloads
        public fun create(groupId: ID, noCache: Boolean? = null): GetGroupInfoApi =
                GetGroupInfoApi(Body(groupId, noCache))
    }

    /**
     * @param groupId 群号
     * @param noCache 是否不使用缓存（使用缓存可能更新不及时，但响应更快）
     */
    @Serializable
    internal data class Body(
        @SerialName("group_id")
        internal val groupId: ID,
        @SerialName("no_cache")
        internal val noCache: Boolean? = null,
    )
}

/**
 * [GetGroupInfoApi] 的响应体。
 *
 * @param groupId 群号
 * @param groupName 群名称
 * @param memberCount 成员数
 * @param maxMemberCount 最大成员数（群容量）
 */
@Serializable
public data class GetGroupInfoResult @ApiResultType internal constructor(
    @SerialName("group_id")
    public val groupId: LongID,
    @SerialName("group_name")
    public val groupName: String,
    @SerialName("member_count")
    public val memberCount: Int,
    @SerialName("max_member_count")
    public val maxMemberCount: Int,
)
