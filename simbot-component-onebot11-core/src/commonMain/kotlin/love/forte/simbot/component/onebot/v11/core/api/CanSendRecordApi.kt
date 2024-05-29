package love.forte.simbot.component.onebot.v11.core.api

import kotlin.Any
import kotlin.Boolean
import kotlin.String
import kotlin.jvm.JvmStatic
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import love.forte.simbot.component.onebot.v11.common.api.ApiResultConstructor

/**
 * [`can_send_record`-检查是否可以发送语音](https://github.com/botuniverse/onebot-11/blob/master/api/public.md#can_send_record-检查是否可以发送语音)
 *
 * @author ForteScarlet
 */
public class CanSendRecordApi private constructor() : OneBotApi<CanSendRecordResult> {
    override val action: String
        get() = ACTION

    override val resultDeserializer: DeserializationStrategy<CanSendRecordResult>
        get() = CanSendRecordResult.serializer()

    override val apiResultDeserializer:
        DeserializationStrategy<OneBotApiResult<CanSendRecordResult>>
        get() = RES_SER

    override val body: Any?
        get() = null

    public companion object Factory {
        private const val ACTION: String = "can_send_record"

        private val RES_SER: KSerializer<OneBotApiResult<CanSendRecordResult>> =
            OneBotApiResult.serializer(CanSendRecordResult.serializer())

        private val INSTANCE: CanSendRecordApi = CanSendRecordApi()

        /**
         * 构建一个 [CanSendRecordApi].
         */
        @JvmStatic
        public fun create(): CanSendRecordApi = INSTANCE
    }
}

/**
 * [CanSendRecordApi] 的响应体。
 *
 * @property yes 是或否
 */
@Serializable
public data class CanSendRecordResult @ApiResultConstructor internal constructor(
    public val yes: Boolean,
)
