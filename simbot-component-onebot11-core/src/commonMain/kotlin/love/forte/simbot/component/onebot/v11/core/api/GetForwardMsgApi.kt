package love.forte.simbot.component.onebot.v11.core.api

import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import love.forte.simbot.common.id.ID
import love.forte.simbot.component.onebot.v11.common.api.ApiResultConstructor
import love.forte.simbot.component.onebot.v11.message.segment.OneBotMessageSegment
import kotlin.jvm.JvmStatic

/**
 * [`get_forward_msg`-获取合并转发消息](https://github.com/botuniverse/onebot-11/blob/master/api/public.md#get_forward_msg-获取合并转发消息)
 *
 * @author ForteScarlet
 */
public class GetForwardMsgApi private constructor(
    override val body: Any,
) : OneBotApi<GetForwardMsgResult> {
    override val action: String
        get() = ACTION

    override val resultDeserializer: DeserializationStrategy<GetForwardMsgResult>
        get() = GetForwardMsgResult.serializer()

    override val apiResultDeserializer:
        DeserializationStrategy<OneBotApiResult<GetForwardMsgResult>>
        get() = RES_SER

    public companion object Factory {
        private const val ACTION: String = "get_forward_msg"

        private val RES_SER: KSerializer<OneBotApiResult<GetForwardMsgResult>> =
            OneBotApiResult.serializer(GetForwardMsgResult.serializer())

        /**
         * 构建一个 [GetForwardMsgApi].
         *
         * @param id 合并转发 ID
         */
        @JvmStatic
        public fun create(id: ID): GetForwardMsgApi = GetForwardMsgApi(Body(id))
    }

    /**
     * @property id 合并转发 ID
     */
    @Serializable
    internal data class Body(
        internal val id: ID,
    )
}

/**
 * [GetForwardMsgApi] 的响应体。
 *
 * @property message 消息内容，使用 [消息的数组格式](https://github.com/botuniverse/onebot-11/blob/master/message/array.md) 表示，
 * 数组中的消息段全部为 [`node` 消息段](https://github.com/botuniverse/onebot-11/blob/master/message/segment.md#合并转发自定义节点)
 */
@Serializable
public data class GetForwardMsgResult @ApiResultConstructor internal constructor(
    public val message: List<OneBotMessageSegment> = emptyList(),
)
