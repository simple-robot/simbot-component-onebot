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
 * [`delete_msg`-撤回消息](https://github.com/botuniverse/onebot-11/blob/master/api/public.md##delete_msg-撤回消息)
 *
 * @author ForteScarlet
 */
public class DeleteMsgApi private constructor(
    override val body: Any,
) : OneBotApi<Unit> {
    override val action: String
        get() = ACTION

    override val resultDeserializer: DeserializationStrategy<Unit>
        get() = Unit.serializer()

    override val apiResultDeserializer: DeserializationStrategy<OneBotApiResult<Unit>>
        get() = OneBotApiResult.emptySerializer()

    public companion object Factory {
        private const val ACTION: String = "delete_msg"

        /**
         * 构建一个 [DeleteMsgApi].
         *
         * @param messageId 消息 ID
         */
        @JvmStatic
        public fun create(messageId: ID): DeleteMsgApi = DeleteMsgApi(Body(messageId))
    }

    /**
     * @param messageId 消息 ID
     */
    @Serializable
    internal data class Body(
        @SerialName("message_id")
        internal val messageId: ID,
    )
}
