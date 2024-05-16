package love.forte.simbot.component.onebot.v11.core.api

import kotlin.Any
import kotlin.String
import kotlin.Unit
import kotlin.jvm.JvmStatic
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.serializer
import love.forte.simbot.common.id.ID

/**
 * [`set_group_name`-设置群名](https://github.com/botuniverse/onebot-11/blob/master/api/public.md#set_group_name-设置群名)
 *
 * @author ForteScarlet
 */
public class SetGroupNameApi private constructor(
    override val body: Any,
) : OneBotApi<Unit> {
    override val action: String
        get() = ACTION

    override val resultDeserializer: DeserializationStrategy<Unit>
        get() = Unit.serializer()

    override val apiResultDeserializer: DeserializationStrategy<OneBotApiResult<Unit>>
        get() = OneBotApiResult.emptySerializer()

    public companion object Factory {
        private const val ACTION: String = "set_group_name"

        /**
         * 构建一个 [SetGroupNameApi].
         *
         * @param groupId 群号
         * @param groupName 新群名
         */
        @JvmStatic
        public fun create(groupId: ID, groupName: String): SetGroupNameApi =
                SetGroupNameApi(Body(groupId, groupName))
    }

    /**
     * @param groupId 群号
     * @param groupName 新群名
     */
    @Serializable
    internal data class Body(
        @SerialName("group_id")
        internal val groupId: ID,
        @SerialName("group_name")
        internal val groupName: String,
    )
}
