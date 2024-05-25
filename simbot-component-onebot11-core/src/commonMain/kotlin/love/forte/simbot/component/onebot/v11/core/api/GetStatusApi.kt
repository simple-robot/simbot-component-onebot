package love.forte.simbot.component.onebot.v11.core.api

import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.KSerializer
import love.forte.simbot.component.onebot.v11.common.api.StatusResult
import kotlin.jvm.JvmStatic

/**
 * [`get_status`-获取运行状态](https://github.com/botuniverse/onebot-11/blob/master/api/public.md#get_status-获取运行状态)
 *
 * @author ForteScarlet
 */
public class GetStatusApi private constructor() : OneBotApi<StatusResult> {
    override val action: String
        get() = ACTION

    override val resultDeserializer: DeserializationStrategy<StatusResult>
        get() = StatusResult.serializer()

    override val apiResultDeserializer: DeserializationStrategy<OneBotApiResult<StatusResult>>
        get() = RES_SER

    override val body: Any?
        get() = null

    public companion object Factory {
        private const val ACTION: String = "get_status"

        private val RES_SER: KSerializer<OneBotApiResult<StatusResult>> =
            OneBotApiResult.serializer(StatusResult.serializer())

        private val INSTANCE: GetStatusApi = GetStatusApi()

        /**
         * 构建一个 [GetStatusApi].
         */
        @JvmStatic
        public fun create(): GetStatusApi = INSTANCE
    }
}
