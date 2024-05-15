package love.forte.simbot.component.onebot.v11.core.api

import kotlin.Any
import kotlin.Boolean
import kotlin.String
import kotlin.Unit
import kotlin.jvm.JvmOverloads
import kotlin.jvm.JvmStatic
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.serializer

/**
 * [`set_friend_add_request`-处理加好友请求](https://github.com/botuniverse/onebot-11/blob/master/api/public.md##set_friend_add_request-处理加好友请求)
 *
 * @author ForteScarlet
 */
public class SetFriendAddRequestApi private constructor(
    override val body: Any,
) : OneBotApi<Unit> {
    override val action: String
        get() = ACTION

    override val resultDeserializer: DeserializationStrategy<Unit>
        get() = Unit.serializer()

    override val apiResultDeserializer: DeserializationStrategy<OneBotApiResult<Unit>>
        get() = OneBotApiResult.emptySerializer()

    public companion object Factory {
        private const val ACTION: String = "set_friend_add_request"

        /**
         * 构建一个 [SetFriendAddRequestApi].
         *
         * @param flag 加好友请求的 flag（需从上报的数据中获得）
         * @param approve 是否同意请求
         * @param remark 添加后的好友备注（仅在同意时有效）
         */
        @JvmStatic
        @JvmOverloads
        public fun create(
            flag: String,
            approve: Boolean? = null,
            remark: String? = null,
        ): SetFriendAddRequestApi = SetFriendAddRequestApi(Body(flag, approve, remark))
    }

    /**
     * @param flag 加好友请求的 flag（需从上报的数据中获得）
     * @param approve 是否同意请求
     * @param remark 添加后的好友备注（仅在同意时有效）
     */
    @Serializable
    internal data class Body(
        internal val flag: String,
        internal val approve: Boolean? = null,
        internal val remark: String? = null,
    )
}
