package love.forte.simbot.component.onebot.v11.core.api

import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonObject
import love.forte.simbot.annotations.ExperimentalSimbotAPI
import love.forte.simbot.common.id.ID
import love.forte.simbot.common.id.IntID
import love.forte.simbot.component.onebot.v11.common.api.ApiResultConstructor
import love.forte.simbot.component.onebot.v11.message.segment.OneBotMessageSegment
import kotlin.jvm.JvmStatic

/**
 * [`get_msg`-获取消息](https://github.com/botuniverse/onebot-11/blob/master/api/public.md#get_msg-获取消息)
 *
 * @author ForteScarlet
 */
public class GetMsgApi private constructor(
    override val body: Any,
) : OneBotApi<GetMsgResult> {
    override val action: String
        get() = ACTION

    override val resultDeserializer: DeserializationStrategy<GetMsgResult>
        get() = GetMsgResult.serializer()

    override val apiResultDeserializer: DeserializationStrategy<OneBotApiResult<GetMsgResult>>
        get() = RES_SER

    public companion object Factory {
        private const val ACTION: String = "get_msg"

        private val RES_SER: KSerializer<OneBotApiResult<GetMsgResult>> =
            OneBotApiResult.serializer(GetMsgResult.serializer())

        /**
         * 构建一个 [GetMsgApi].
         *
         * @param messageId 消息 ID
         */
        @JvmStatic
        public fun create(messageId: ID): GetMsgApi = GetMsgApi(Body(messageId))
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

/**
 * [GetMsgApi] 的响应体。
 *
 * @param time 发送时间
 * @param messageType 消息类型，同 [消息事件](https://github.com/botuniverse/onebot-11/blob/master/event/message.md)
 * @param messageId 消息 ID
 * @param realId 消息真实 ID
 * @param sender 发送人信息，同 [消息事件](https://github.com/botuniverse/onebot-11/blob/master/event/message.md)
 * @param message 消息内容
 */
@Serializable
public data class GetMsgResult @ApiResultConstructor internal constructor(
    public val time: Int,
    @SerialName("message_type")
    public val messageType: String,
    @SerialName("message_id")
    public val messageId: IntID,
    @SerialName("real_id")
    public val realId: IntID,
    @property:ExperimentalSimbotAPI
    public val sender: JsonObject, // Any = TODO("sender?"),
    public val message: List<OneBotMessageSegment> = emptyList(), // Any = ("message?"),
)

