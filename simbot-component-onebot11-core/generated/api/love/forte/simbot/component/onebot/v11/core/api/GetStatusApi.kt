package love.forte.simbot.component.onebot.v11.core.api

import kotlin.Any
import kotlin.Boolean
import kotlin.Nothing
import kotlin.String
import kotlin.jvm.JvmStatic
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable

/**
 * [`get_status`-获取运行状态](https://github.com/botuniverse/onebot-11/blob/master/api/public.md##get_status-获取运行状态)
 *
 * @author ForteScarlet
 */
public class GetStatusApi private constructor() : OneBotApi<GetStatusResult> {
    override val action: String
        get() = ACTION

    override val resultDeserializer: DeserializationStrategy<GetStatusResult>
        get() = GetStatusResult.serializer()

    override val apiResultDeserializer: DeserializationStrategy<OneBotApiResult<GetStatusResult>>
        get() = RES_SER

    override val body: Any?
        get() = null

    public companion object Factory {
        private const val ACTION: String = "get_status"

        private val RES_SER: KSerializer<OneBotApiResult<GetStatusResult>> =
            OneBotApiResult.serializer(GetStatusResult.serializer())

        private val INSTANCE: GetStatusApi = GetStatusApi()

        /**
         * 构建一个 [GetStatusApi].
         */
        @JvmStatic
        public fun create(): GetStatusApi = INSTANCE
    }
}

/**
 * [GetStatusApi] 的响应体。
 *
 * @param online 当前 QQ 在线，`null` 表示无法查询到在线状态
 * @param good 状态符合预期，意味着各模块正常运行、功能正常，且 QQ 在线
 * @param …… OneBot 实现自行添加的其它内容
 */
@Serializable
public data class GetStatusResult @ApiResultType internal constructor(
    public val online: Boolean,
    public val good: Boolean,
    public val `……`: Nothing = TODO("……?"),
)
