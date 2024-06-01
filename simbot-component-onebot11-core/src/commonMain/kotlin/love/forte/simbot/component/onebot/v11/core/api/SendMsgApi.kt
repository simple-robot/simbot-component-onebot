package love.forte.simbot.component.onebot.v11.core.api

import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import love.forte.simbot.common.id.ID
import love.forte.simbot.common.id.IntID
import love.forte.simbot.component.onebot.v11.common.api.ApiResultConstructor
import kotlin.jvm.JvmOverloads
import kotlin.jvm.JvmStatic

/**
 * [`send_msg`-发送消息](https://github.com/botuniverse/onebot-11/blob/master/api/public.md#send_msg-发送消息)
 *
 * @author ForteScarlet
 */
public class SendMsgApi private constructor(
    override val body: Any,
) : OneBotApi<SendMsgResult> {
    override val action: String
        get() = ACTION

    override val resultDeserializer: DeserializationStrategy<SendMsgResult>
        get() = SendMsgResult.serializer()

    override val apiResultDeserializer: DeserializationStrategy<OneBotApiResult<SendMsgResult>>
        get() = RES_SER

    public companion object Factory {
        public const val MESSAGE_TYPE_PRIVATE: String = "private"
        public const val MESSAGE_TYPE_GROUP: String = "group"

        private const val ACTION: String = "send_msg"

        private val RES_SER: KSerializer<OneBotApiResult<SendMsgResult>> =
            OneBotApiResult.serializer(SendMsgResult.serializer())

        /**
         * 构建一个 [SendMsgApi].
         *
         * @param messageType 消息类型，支持 `private`、`group`，分别对应私聊、群组，如不传入，则根据传入的 `*_id` 参数判断
         * @param userId 对方 QQ 号（消息类型为 `private` 时需要）
         * @param groupId 群号（消息类型为 `group` 时需要）
         * @param message 要发送的内容
         * @param autoEscape 消息内容是否作为纯文本发送（即不解析 CQ 码），只在 `message` 字段是字符串时有效
         */
        @JvmStatic
        @JvmOverloads
        public fun create(
            messageType: String,
            userId: ID?,
            groupId: ID?,
            message: OneBotMessageOutgoing,
            autoEscape: Boolean? = null,
        ): SendMsgApi = SendMsgApi(Body(messageType, userId, groupId, message, autoEscape))

        /**
         * 构建一个发送到私聊的 [SendMsgApi].
         *
         * @param userId 对方 QQ 号（消息类型为 `private` 时需要）
         * @param message 要发送的内容
         * @param autoEscape 消息内容是否作为纯文本发送（即不解析 CQ 码），只在 `message` 字段是字符串时有效
         */
        @JvmStatic
        @JvmOverloads
        public fun createPrivate(
            userId: ID,
            message: OneBotMessageOutgoing,
            autoEscape: Boolean? = null,
        ): SendMsgApi = create(
            messageType = MESSAGE_TYPE_PRIVATE,
            userId = userId,
            groupId = null,
            message = message,
            autoEscape = autoEscape
        )

        /**
         * 构建一个发送到群聊的 [SendMsgApi].
         *
         * @param groupId 群号（消息类型为 `group` 时需要）
         * @param message 要发送的内容
         * @param autoEscape 消息内容是否作为纯文本发送（即不解析 CQ 码），只在 `message` 字段是字符串时有效
         */
        @JvmStatic
        @JvmOverloads
        public fun createGroup(
            groupId: ID,
            message: OneBotMessageOutgoing,
            autoEscape: Boolean? = null,
        ): SendMsgApi = create(
            messageType = MESSAGE_TYPE_GROUP,
            userId = null,
            groupId = groupId,
            message = message,
            autoEscape = autoEscape
        )
    }

    /**
     * @property messageType 消息类型，支持 `private`、`group`，分别对应私聊、群组，如不传入，则根据传入的 `*_id` 参数判断
     * @property userId 对方 QQ 号（消息类型为 `private` 时需要）
     * @property groupId 群号（消息类型为 `group` 时需要）
     * @property message 要发送的内容
     * @property autoEscape 消息内容是否作为纯文本发送（即不解析 CQ 码），只在 `message` 字段是字符串时有效
     */
    @Serializable
    internal data class Body(
        @SerialName("message_type")
        internal val messageType: String,
        @SerialName("user_id")
        internal val userId: ID? = null,
        @SerialName("group_id")
        internal val groupId: ID? = null,
        internal val message: OneBotMessageOutgoing,
        @SerialName("auto_escape")
        internal val autoEscape: Boolean? = null,
    )
}

/**
 * [SendMsgApi] 的响应体。
 *
 * @property messageId 消息 ID
 */
@Serializable
public data class SendMsgResult @ApiResultConstructor internal constructor(
    @SerialName("message_id")
    public val messageId: IntID,
)
