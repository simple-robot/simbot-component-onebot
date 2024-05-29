package love.forte.simbot.component.onebot.v11.core.api

import kotlin.Any
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
 * [`set_group_card`-设置群名片（群备注）](https://github.com/botuniverse/onebot-11/blob/master/api/public.md#set_group_card-设置群名片群备注)
 *
 * @author ForteScarlet
 */
public class SetGroupCardApi private constructor(
    override val body: Any,
) : OneBotApi<Unit> {
    override val action: String
        get() = ACTION

    override val resultDeserializer: DeserializationStrategy<Unit>
        get() = Unit.serializer()

    override val apiResultDeserializer: DeserializationStrategy<OneBotApiResult<Unit>>
        get() = OneBotApiResult.emptySerializer()

    public companion object Factory {
        private const val ACTION: String = "set_group_card"

        /**
         * 构建一个 [SetGroupCardApi].
         *
         * @param groupId 群号
         * @param userId 要设置的 QQ 号
         * @param card 群名片内容，不填或空字符串表示删除群名片
         */
        @JvmStatic
        @JvmOverloads
        public fun create(
            groupId: ID,
            userId: ID,
            card: String? = null,
        ): SetGroupCardApi = SetGroupCardApi(Body(groupId, userId, card))
    }

    /**
     * @property groupId 群号
     * @property userId 要设置的 QQ 号
     * @property card 群名片内容，不填或空字符串表示删除群名片
     */
    @Serializable
    internal data class Body(
        @SerialName("group_id")
        internal val groupId: ID,
        @SerialName("user_id")
        internal val userId: ID,
        internal val card: String? = null,
    )
}
