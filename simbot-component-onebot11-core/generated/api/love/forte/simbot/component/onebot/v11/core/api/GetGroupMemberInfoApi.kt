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
 * [`get_group_member_info`-获取群成员信息](https://github.com/botuniverse/onebot-11/blob/master/api/public.md##get_group_member_info-获取群成员信息)
 *
 * @author ForteScarlet
 */
public class GetGroupMemberInfoApi private constructor(
    override val body: Any,
) : OneBotApi<GetGroupMemberInfoResult> {
    override val action: String
        get() = ACTION

    override val resultDeserializer: DeserializationStrategy<GetGroupMemberInfoResult>
        get() = GetGroupMemberInfoResult.serializer()

    override val apiResultDeserializer:
        DeserializationStrategy<OneBotApiResult<GetGroupMemberInfoResult>>
        get() = RES_SER

    public companion object Factory {
        private const val ACTION: String = "get_group_member_info"

        private val RES_SER: KSerializer<OneBotApiResult<GetGroupMemberInfoResult>> =
            OneBotApiResult.serializer(GetGroupMemberInfoResult.serializer())

        /**
         * 构建一个 [GetGroupMemberInfoApi].
         *
         * @param groupId 群号
         * @param userId QQ 号
         * @param noCache 是否不使用缓存（使用缓存可能更新不及时，但响应更快）
         */
        @JvmStatic
        @JvmOverloads
        public fun create(
            groupId: ID,
            userId: ID,
            noCache: Boolean? = null,
        ): GetGroupMemberInfoApi = GetGroupMemberInfoApi(Body(groupId, userId, noCache))
    }

    /**
     * @param groupId 群号
     * @param userId QQ 号
     * @param noCache 是否不使用缓存（使用缓存可能更新不及时，但响应更快）
     */
    @Serializable
    internal data class Body(
        @SerialName("group_id")
        internal val groupId: ID,
        @SerialName("user_id")
        internal val userId: ID,
        @SerialName("no_cache")
        internal val noCache: Boolean? = null,
    )
}

/**
 * [GetGroupMemberInfoApi] 的响应体。
 *
 * @param groupId 群号
 * @param userId QQ 号
 * @param nickname 昵称
 * @param card 群名片／备注
 * @param sex 性别，`male` 或 `female` 或 `unknown`
 * @param age 年龄
 * @param area 地区
 * @param joinTime 加群时间戳
 * @param lastSentTime 最后发言时间戳
 * @param level 成员等级
 * @param role 角色，`owner` 或 `admin` 或 `member`
 * @param unfriendly 是否不良记录成员
 * @param title 专属头衔
 * @param titleExpireTime 专属头衔过期时间戳
 * @param cardChangeable 是否允许修改群名片
 */
@Serializable
public data class GetGroupMemberInfoResult @ApiResultType internal constructor(
    @SerialName("group_id")
    public val groupId: ID,
    @SerialName("user_id")
    public val userId: ID,
    public val nickname: String,
    public val card: String,
    public val sex: String,
    public val age: Int,
    public val area: String,
    @SerialName("join_time")
    public val joinTime: Int,
    @SerialName("last_sent_time")
    public val lastSentTime: Int,
    public val level: String,
    public val role: String,
    public val unfriendly: Boolean,
    public val title: String,
    @SerialName("title_expire_time")
    public val titleExpireTime: Int,
    @SerialName("card_changeable")
    public val cardChangeable: Boolean,
)
