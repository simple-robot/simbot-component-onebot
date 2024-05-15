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
 * [`set_group_anonymous_ban`-群组匿名用户禁言](https://github.com/botuniverse/onebot-11/blob/master/api/public.md##set_group_anonymous_ban-群组匿名用户禁言)
 *
 * @author ForteScarlet
 */
public class SetGroupAnonymousBanApi private constructor(
    override val body: Any,
) : OneBotApi<Unit> {
    override val action: String
        get() = ACTION

    override val resultDeserializer: DeserializationStrategy<Unit>
        get() = Unit.serializer()

    override val apiResultDeserializer: DeserializationStrategy<OneBotApiResult<Unit>>
        get() = OneBotApiResult.emptySerializer()

    public companion object Factory {
        private const val ACTION: String = "set_group_anonymous_ban"

        /**
         * 构建一个 [SetGroupAnonymousBanApi].
         *
         * @param groupId 群号
         * @param anonymous 可选，要禁言的匿名用户对象（群消息上报的 `anonymous` 字段）
         * @param anonymousFlag 可选，要禁言的匿名用户的 flag（需从群消息上报的数据中获得）
         * @param duration 禁言时长，单位秒，无法取消匿名用户禁言
         */
        @JvmStatic
        @JvmOverloads
        public fun create(
            groupId: ID,
            anonymous: Any = TODO("anonymous?"),
            anonymousFlag: String,
            duration: Long? = null,
        ): SetGroupAnonymousBanApi = SetGroupAnonymousBanApi(
            Body(
                groupId,
                anonymous,
                anonymousFlag,
                duration
            )
        )
    }

    /**
     * @param groupId 群号
     * @param anonymous 可选，要禁言的匿名用户对象（群消息上报的 `anonymous` 字段）
     * @param anonymousFlag 可选，要禁言的匿名用户的 flag（需从群消息上报的数据中获得）
     * @param duration 禁言时长，单位秒，无法取消匿名用户禁言
     */
    @Serializable
    internal data class Body(
        @SerialName("group_id")
        internal val groupId: ID,
        internal val anonymous: Any = TODO("anonymous?"),
        @SerialName("anonymous_flag")
        internal val anonymousFlag: String,
        internal val duration: Long? = null,
    )
}
