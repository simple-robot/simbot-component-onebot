package love.forte.simbot.component.onebot.v11.core.api

import kotlin.Any
import kotlin.Boolean
import kotlin.String
import kotlin.jvm.JvmOverloads
import kotlin.jvm.JvmStatic
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import love.forte.simbot.common.id.ID
import love.forte.simbot.common.id.IntID

/**
 * [`send_group_msg`-发送群消息](https://github.com/botuniverse/onebot-11/blob/master/api/public.md#send_group_msg-发送群消息)
 *
 * @author ForteScarlet
 */
public class SendGroupMsgApi private constructor(
    override val body: Any,
) : OneBotApi<SendGroupMsgResult> {
    override val action: String
        get() = ACTION

    override val resultDeserializer: DeserializationStrategy<SendGroupMsgResult>
        get() = SendGroupMsgResult.serializer()

    override val apiResultDeserializer: DeserializationStrategy<OneBotApiResult<SendGroupMsgResult>>
        get() = RES_SER

    public companion object Factory {
        private const val ACTION: String = "send_group_msg"

        private val RES_SER: KSerializer<OneBotApiResult<SendGroupMsgResult>> =
                OneBotApiResult.serializer(SendGroupMsgResult.serializer())

        /**
         * 构建一个 [SendGroupMsgApi].
         *
         * @param groupId 群号
         * @param message 要发送的内容
         * @param autoEscape 消息内容是否作为纯文本发送（即不解析 CQ 码），只在 `message` 字段是字符串时有效
         */
        @JvmStatic
        @JvmOverloads
        public fun create(
            groupId: ID,
            message: Any = TODO("message?"),
            autoEscape: Boolean? = null,
        ): SendGroupMsgApi = SendGroupMsgApi(Body(groupId, message, autoEscape))
    }

    /**
     * @param groupId 群号
     * @param message 要发送的内容
     * @param autoEscape 消息内容是否作为纯文本发送（即不解析 CQ 码），只在 `message` 字段是字符串时有效
     */
    @Serializable
    internal data class Body(
        @SerialName("group_id")
        internal val groupId: ID,
        internal val message: Any = TODO("message?"),
        @SerialName("auto_escape")
        internal val autoEscape: Boolean? = null,
    )
}

/**
 * [SendGroupMsgApi] 的响应体。
 *
 * @param messageId 消息 ID
 */
@Serializable
public data class SendGroupMsgResult @ApiResultType internal constructor(
    @SerialName("message_id")
    public val messageId: IntID,
)
