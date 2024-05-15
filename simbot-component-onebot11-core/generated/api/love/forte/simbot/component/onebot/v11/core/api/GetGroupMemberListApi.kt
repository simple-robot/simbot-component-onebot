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
 * [`get_group_member_list`-获取群成员列表](https://github.com/botuniverse/onebot-11/blob/master/api/public.md##get_group_member_list-获取群成员列表)
 *
 * @author ForteScarlet
 */
public class GetGroupMemberListApi private constructor(
    override val body: Any,
) : OneBotApi<Unit> {
    override val action: String
        get() = ACTION

    override val resultDeserializer: DeserializationStrategy<Unit>
        get() = Unit.serializer()

    override val apiResultDeserializer: DeserializationStrategy<OneBotApiResult<Unit>>
        get() = OneBotApiResult.emptySerializer()

    public companion object Factory {
        private const val ACTION: String = "get_group_member_list"

        /**
         * 构建一个 [GetGroupMemberListApi].
         *
         * @param groupId 群号
         */
        @JvmStatic
        public fun create(groupId: ID): GetGroupMemberListApi = GetGroupMemberListApi(Body(groupId))
    }

    /**
     * @param groupId 群号
     */
    @Serializable
    internal data class Body(
        @SerialName("group_id")
        internal val groupId: ID,
    )
}
