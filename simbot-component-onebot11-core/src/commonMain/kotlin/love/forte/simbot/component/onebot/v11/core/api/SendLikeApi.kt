package love.forte.simbot.component.onebot.v11.core.api

import kotlin.Any
import kotlin.Long
import kotlin.String
import kotlin.Unit
import kotlin.jvm.JvmOverloads
import kotlin.jvm.JvmStatic
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.serializer
import love.forte.simbot.common.id.ID

/**
 * [`send_like`-发送好友赞](https://github.com/botuniverse/onebot-11/blob/master/api/public.md#send_like-发送好友赞)
 *
 * @author ForteScarlet
 */
public class SendLikeApi private constructor(
    override val body: Any,
) : OneBotApi<Unit> {
    override val action: String
        get() = ACTION

    override val resultDeserializer: DeserializationStrategy<Unit>
        get() = Unit.serializer()

    override val apiResultDeserializer: DeserializationStrategy<OneBotApiResult<Unit>>
        get() = OneBotApiResult.emptySerializer()

    public companion object Factory {
        private const val ACTION: String = "send_like"

        /**
         * 构建一个 [SendLikeApi].
         *
         * @param userId 对方 QQ 号
         * @param times 赞的次数，每个好友每天最多 10 次
         */
        @JvmStatic
        @JvmOverloads
        public fun create(userId: ID, times: Long? = null): SendLikeApi = SendLikeApi(
            Body(
                userId,
                times
            )
        )
    }

    /**
     * @property userId 对方 QQ 号
     * @property times 赞的次数，每个好友每天最多 10 次
     */
    @Serializable
    internal data class Body(
        @SerialName("user_id")
        internal val userId: ID,
        internal val times: Long? = null,
    )
}
