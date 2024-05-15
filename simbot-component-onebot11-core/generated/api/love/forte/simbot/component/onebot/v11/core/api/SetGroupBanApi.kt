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
 * [`set_group_ban`-群组单人禁言](https://github.com/botuniverse/onebot-11/blob/master/api/public.md##set_group_ban-群组单人禁言)
 *
 * @author ForteScarlet
 */
public class SetGroupBanApi private constructor(
    override val body: Any,
) : OneBotApi<Unit> {
    override val action: String
        get() = ACTION

    override val resultDeserializer: DeserializationStrategy<Unit>
        get() = Unit.serializer()

    override val apiResultDeserializer: DeserializationStrategy<OneBotApiResult<Unit>>
        get() = OneBotApiResult.emptySerializer()

    public companion object Factory {
        private const val ACTION: String = "set_group_ban"

        /**
         * 构建一个 [SetGroupBanApi].
         *
         * @param groupId 群号
         * @param userId 要禁言的 QQ 号
         * @param duration 禁言时长，单位秒，0 表示取消禁言
         */
        @JvmStatic
        @JvmOverloads
        public fun create(
            groupId: ID,
            userId: ID,
            duration: Long? = null,
        ): SetGroupBanApi = SetGroupBanApi(Body(groupId, userId, duration))
    }

    /**
     * @param groupId 群号
     * @param userId 要禁言的 QQ 号
     * @param duration 禁言时长，单位秒，0 表示取消禁言
     */
    @Serializable
    internal data class Body(
        @SerialName("group_id")
        internal val groupId: ID,
        @SerialName("user_id")
        internal val userId: ID,
        internal val duration: Long? = null,
    )
}
