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
 * [`set_group_special_title`-设置群组专属头衔](https://github.com/botuniverse/onebot-11/blob/master/api/public.md##set_group_special_title-设置群组专属头衔)
 *
 * @author ForteScarlet
 */
public class SetGroupSpecialTitleApi private constructor(
    override val body: Any,
) : OneBotApi<Unit> {
    override val action: String
        get() = ACTION

    override val resultDeserializer: DeserializationStrategy<Unit>
        get() = Unit.serializer()

    override val apiResultDeserializer: DeserializationStrategy<OneBotApiResult<Unit>>
        get() = OneBotApiResult.emptySerializer()

    public companion object Factory {
        private const val ACTION: String = "set_group_special_title"

        /**
         * 构建一个 [SetGroupSpecialTitleApi].
         *
         * @param groupId 群号
         * @param userId 要设置的 QQ 号
         * @param specialTitle 专属头衔，不填或空字符串表示删除专属头衔
         * @param duration 专属头衔有效期，单位秒，-1 表示永久，不过此项似乎没有效果，可能是只有某些特殊的时间长度有效，有待测试
         */
        @JvmStatic
        @JvmOverloads
        public fun create(
            groupId: ID,
            userId: ID,
            specialTitle: String? = null,
            duration: Long? = null,
        ): SetGroupSpecialTitleApi = SetGroupSpecialTitleApi(
            Body(
                groupId,
                userId,
                specialTitle,
                duration
            )
        )
    }

    /**
     * @param groupId 群号
     * @param userId 要设置的 QQ 号
     * @param specialTitle 专属头衔，不填或空字符串表示删除专属头衔
     * @param duration 专属头衔有效期，单位秒，-1 表示永久，不过此项似乎没有效果，可能是只有某些特殊的时间长度有效，有待测试
     */
    @Serializable
    internal data class Body(
        @SerialName("group_id")
        internal val groupId: ID,
        @SerialName("user_id")
        internal val userId: ID,
        @SerialName("special_title")
        internal val specialTitle: String? = null,
        internal val duration: Long? = null,
    )
}
