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
 * [`get_friend_list`-获取好友列表](https://github.com/botuniverse/onebot-11/blob/master/api/public.md#get_friend_list-获取好友列表)
 *
 * @author ForteScarlet
 */
public class GetFriendListApi private constructor() : OneBotApi<GetFriendListResult> {
    override val action: String
        get() = ACTION

    override val resultDeserializer: DeserializationStrategy<GetFriendListResult>
        get() = GetFriendListResult.serializer()

    override val apiResultDeserializer:
            DeserializationStrategy<OneBotApiResult<GetFriendListResult>>
        get() = RES_SER

    override val body: Any?
        get() = null

    public companion object Factory {
        private const val ACTION: String = "get_friend_list"

        private val RES_SER: KSerializer<OneBotApiResult<GetFriendListResult>> =
                OneBotApiResult.serializer(GetFriendListResult.serializer())

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
public data class GetFriendListResult @ApiResultConstructor internal constructor(
    @SerialName("user_id")
    public val userId: LongID,
    public val nickname: String,
    public val remark: String,
)
