package love.forte.simbot.component.onebot.v11.core.api

import kotlin.Any
import kotlin.Boolean
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
 * [`set_group_kick`-群组踢人](https://github.com/botuniverse/onebot-11/blob/master/api/public.md##set_group_kick-群组踢人)
 *
 * @author ForteScarlet
 */
public class SetGroupKickApi private constructor(
    override val body: Any,
) : OneBotApi<Unit> {
    override val action: String
        get() = ACTION

    override val resultDeserializer: DeserializationStrategy<Unit>
        get() = Unit.serializer()

    override val apiResultDeserializer: DeserializationStrategy<OneBotApiResult<Unit>>
        get() = OneBotApiResult.emptySerializer()

    public companion object Factory {
        private const val ACTION: String = "set_group_kick"

        /**
         * 构建一个 [SetGroupKickApi].
         *
         * @param groupId 群号
         * @param userId 要踢的 QQ 号
         * @param rejectAddRequest 拒绝此人的加群请求
         */
        @JvmStatic
        @JvmOverloads
        public fun create(
            groupId: ID,
            userId: ID,
            rejectAddRequest: Boolean? = null,
        ): SetGroupKickApi = SetGroupKickApi(Body(groupId, userId, rejectAddRequest))
    }

    /**
     * @param groupId 群号
     * @param userId 要踢的 QQ 号
     * @param rejectAddRequest 拒绝此人的加群请求
     */
    @Serializable
    internal data class Body(
        @SerialName("group_id")
        internal val groupId: ID,
        @SerialName("user_id")
        internal val userId: ID,
        @SerialName("reject_add_request")
        internal val rejectAddRequest: Boolean? = null,
    )
}
