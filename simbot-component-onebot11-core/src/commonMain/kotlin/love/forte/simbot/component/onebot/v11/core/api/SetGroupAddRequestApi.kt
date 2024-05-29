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

/**
 * [`set_group_add_request`-处理加群请求／邀请](https://github.com/botuniverse/onebot-11/blob/master/api/public.md#set_group_add_request-处理加群请求邀请)
 *
 * @author ForteScarlet
 */
public class SetGroupAddRequestApi private constructor(
    override val body: Any,
) : OneBotApi<Unit> {
    override val action: String
        get() = ACTION

    override val resultDeserializer: DeserializationStrategy<Unit>
        get() = Unit.serializer()

    override val apiResultDeserializer: DeserializationStrategy<OneBotApiResult<Unit>>
        get() = OneBotApiResult.emptySerializer()

    public companion object Factory {
        private const val ACTION: String = "set_group_add_request"

        /**
         * 构建一个 [SetGroupAddRequestApi].
         *
         * @param flag 加群请求的 flag（需从上报的数据中获得）
         * @param subType `add` 或 `invite`，请求类型（需要和上报消息中的 `sub_type` 字段相符）
         * @param approve 是否同意请求／邀请
         * @param reason 拒绝理由（仅在拒绝时有效）
         */
        @JvmStatic
        @JvmOverloads
        public fun create(
            flag: String,
            subType: String,
            approve: Boolean? = null,
            reason: String? = null,
        ): SetGroupAddRequestApi = SetGroupAddRequestApi(Body(flag, subType, approve, reason))
    }

    /**
     * @property flag 加群请求的 flag（需从上报的数据中获得）
     * @property subType `add` 或 `invite`，请求类型（需要和上报消息中的 `sub_type` 字段相符）
     * @property approve 是否同意请求／邀请
     * @property reason 拒绝理由（仅在拒绝时有效）
     */
    @Serializable
    internal data class Body(
        internal val flag: String,
        @SerialName("sub_type")
        internal val subType: String,
        internal val approve: Boolean? = null,
        internal val reason: String? = null,
    )
}
