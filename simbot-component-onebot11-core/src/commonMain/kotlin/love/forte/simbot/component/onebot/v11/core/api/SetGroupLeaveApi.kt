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
 * [`set_group_leave`-退出群组](https://github.com/botuniverse/onebot-11/blob/master/api/public.md#set_group_leave-退出群组)
 *
 * @author ForteScarlet
 */
public class SetGroupLeaveApi private constructor(
    override val body: Any,
) : OneBotApi<Unit> {
    override val action: String
        get() = ACTION

    override val resultDeserializer: DeserializationStrategy<Unit>
        get() = Unit.serializer()

    override val apiResultDeserializer: DeserializationStrategy<OneBotApiResult<Unit>>
        get() = OneBotApiResult.emptySerializer()

    public companion object Factory {
        private const val ACTION: String = "set_group_leave"

        /**
         * 构建一个 [SetGroupLeaveApi].
         *
         * @param groupId 群号
         * @param isDismiss 是否解散，如果登录号是群主，则仅在此项为 true 时能够解散
         */
        @JvmStatic
        @JvmOverloads
        public fun create(groupId: ID, isDismiss: Boolean? = null): SetGroupLeaveApi =
            SetGroupLeaveApi(Body(groupId, isDismiss))
    }

    /**
     * @property groupId 群号
     * @property isDismiss 是否解散，如果登录号是群主，则仅在此项为 true 时能够解散
     */
    @Serializable
    internal data class Body(
        @SerialName("group_id")
        internal val groupId: ID,
        @SerialName("is_dismiss")
        internal val isDismiss: Boolean? = null,
    )
}
