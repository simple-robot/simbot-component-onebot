package love.forte.simbot.component.onebot.v11.core.api

import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import love.forte.simbot.common.id.ID
import love.forte.simbot.common.id.LongID
import love.forte.simbot.component.onebot.v11.common.api.ApiResultConstructor
import kotlin.jvm.JvmStatic

/**
 * [`get_group_honor_info`-获取群荣誉信息](https://github.com/botuniverse/onebot-11/blob/master/api/public.md#get_group_honor_info-获取群荣誉信息)
 *
 * @author ForteScarlet
 */
public class GetGroupHonorInfoApi private constructor(
    override val body: Any,
) : OneBotApi<GetGroupHonorInfoResult> {
    override val action: String
        get() = ACTION

    override val resultDeserializer: DeserializationStrategy<GetGroupHonorInfoResult>
        get() = GetGroupHonorInfoResult.serializer()

    override val apiResultDeserializer:
        DeserializationStrategy<OneBotApiResult<GetGroupHonorInfoResult>>
        get() = RES_SER

    public companion object Factory {
        private const val ACTION: String = "get_group_honor_info"

        private val RES_SER: KSerializer<OneBotApiResult<GetGroupHonorInfoResult>> =
            OneBotApiResult.serializer(GetGroupHonorInfoResult.serializer())

        /**
         * 构建一个 [GetGroupHonorInfoApi].
         *
         * @param groupId 群号
         * @param type 要获取的群荣誉类型，可传入 `talkative` `performer` `legend` `strong_newbie` `emotion`
         * 以分别获取单个类型的群荣誉数据，或传入 `all` 获取所有数据
         */
        @JvmStatic
        public fun create(groupId: ID, type: String): GetGroupHonorInfoApi =
            GetGroupHonorInfoApi(Body(groupId, type))
    }

    /**
     * @param groupId 群号
     * @param type 要获取的群荣誉类型，可传入 `talkative` `performer` `legend` `strong_newbie` `emotion`
     * 以分别获取单个类型的群荣誉数据，或传入 `all` 获取所有数据
     */
    @Serializable
    internal data class Body(
        @SerialName("group_id")
        internal val groupId: ID,
        internal val type: String,
    )
}

/**
 * [GetGroupHonorInfoApi] 的响应体。
 *
 * @param groupId 群号
 * @param currentTalkative 当前龙王，仅 `type` 为 `talkative` 或 `all` 时有数据
 * @param talkativeList 历史龙王，仅 `type` 为 `talkative` 或 `all` 时有数据
 * @param performerList 群聊之火，仅 `type` 为 `performer` 或 `all` 时有数据
 * @param legendList 群聊炽焰，仅 `type` 为 `legend` 或 `all` 时有数据
 * @param strongNewbieList 冒尖小春笋，仅 `type` 为 `strong_newbie` 或 `all` 时有数据
 * @param emotionList 快乐之源，仅 `type` 为 `emotion` 或 `all` 时有数据
 */
@Serializable
public data class GetGroupHonorInfoResult @ApiResultConstructor internal constructor(
    @SerialName("group_id")
    public val groupId: LongID,
    @SerialName("current_talkative")
    public val currentTalkative: CurrentTalkative? = null,
    @SerialName("talkative_list")
    public val talkativeList: List<HonorInfo>? = null,
    @SerialName("performer_list")
    public val performerList: List<HonorInfo>? = null,
    @SerialName("legend_list")
    public val legendList: List<HonorInfo>? = null,
    @SerialName("strong_newbie_list")
    public val strongNewbieList: List<HonorInfo>? = null,
    @SerialName("emotion_list")
    public val emotionList: List<HonorInfo>? = null,
) {

    /**
     * 当前龙王
     * @param userId QQ 号
     * @param nickname 昵称
     * @param avatar 头像 URL
     * @param dayCount 持续天数
     */
    @Serializable
    public data class CurrentTalkative(
        @SerialName("user_id")
        val userId: LongID,
        val nickname: String,
        val avatar: String,
        @SerialName("day_count")
        val dayCount: Int,
    )

    /**
     * 龙王以外的荣耀信息内容。
     *
     * @param userId QQ 号
     * @param nickname 昵称
     * @param avatar 头像 URL
     * @param description 荣誉描述
     */
    @Serializable
    public data class HonorInfo(
        @SerialName("user_id")
        public val userId: LongID,
        public val nickname: String,
        public val avatar: String,
        public val description: String,
    )
}
