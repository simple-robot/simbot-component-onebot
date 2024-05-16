package love.forte.simbot.component.onebot.v11.core.api

import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.ListSerializer
import love.forte.simbot.common.id.LongID
import kotlin.jvm.JvmStatic

/**
 * [`get_friend_list`-获取好友列表](https://github.com/botuniverse/onebot-11/blob/master/api/public.md#get_friend_list-获取好友列表)
 *
 * @author ForteScarlet
 */
public class GetFriendListApi private constructor() : OneBotApi<List<GetFriendListResult>> {
    override val action: String
        get() = ACTION

    override val resultDeserializer: DeserializationStrategy<List<GetFriendListResult>>
        get() = SER

    override val apiResultDeserializer:
        DeserializationStrategy<OneBotApiResult<List<GetFriendListResult>>>
        get() = RES_SER

    override val body: Any?
        get() = null

    public companion object Factory {
        private const val ACTION: String = "get_friend_list"

        private val SER = ListSerializer(GetFriendListResult.serializer())

        private val RES_SER = OneBotApiResult.serializer(SER)

        private val INSTANCE: GetFriendListApi = GetFriendListApi()

        /**
         * 构建一个 [GetFriendListApi].
         */
        @JvmStatic
        public fun create(): GetFriendListApi = INSTANCE
    }
}

/**
 * [GetFriendListApi] 的响应体。
 *
 * @param userId QQ 号
 * @param nickname 昵称
 * @param remark 备注名
 */
@Serializable
public data class GetFriendListResult @ApiResultType internal constructor(
    @SerialName("user_id")
    public val userId: LongID,
    public val nickname: String,
    public val remark: String,
)
