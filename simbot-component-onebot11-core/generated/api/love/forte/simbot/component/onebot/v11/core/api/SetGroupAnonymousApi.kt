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
 * [`set_group_anonymous`-群组匿名](https://github.com/botuniverse/onebot-11/blob/master/api/public.md#set_group_anonymous-群组匿名)
 *
 * @author ForteScarlet
 */
public class SetGroupAnonymousApi private constructor(
    override val body: Any,
) : OneBotApi<Unit> {
    override val action: String
        get() = ACTION

    override val resultDeserializer: DeserializationStrategy<Unit>
        get() = Unit.serializer()

    override val apiResultDeserializer: DeserializationStrategy<OneBotApiResult<Unit>>
        get() = OneBotApiResult.emptySerializer()

    public companion object Factory {
        private const val ACTION: String = "set_group_anonymous"

        /**
         * 构建一个 [SetGroupAnonymousApi].
         *
         * @param groupId 群号
         * @param enable 是否允许匿名聊天
         */
        @JvmStatic
        @JvmOverloads
        public fun create(groupId: ID, enable: Boolean? = null): SetGroupAnonymousApi =
                SetGroupAnonymousApi(Body(groupId, enable))
    }

    /**
     * @param groupId 群号
     * @param enable 是否允许匿名聊天
     */
    @Serializable
    internal data class Body(
        @SerialName("group_id")
        internal val groupId: ID,
        internal val enable: Boolean? = null,
    )
}
