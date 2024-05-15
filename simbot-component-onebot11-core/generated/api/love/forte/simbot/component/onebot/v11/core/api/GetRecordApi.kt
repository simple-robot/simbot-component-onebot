package love.forte.simbot.component.onebot.v11.core.api

import kotlin.Any
import kotlin.String
import kotlin.jvm.JvmStatic
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * [`get_record`-获取语音](https://github.com/botuniverse/onebot-11/blob/master/api/public.md##get_record-获取语音)
 *
 * @author ForteScarlet
 */
public class GetRecordApi private constructor(
    override val body: Any,
) : OneBotApi<GetRecordResult> {
    override val action: String
        get() = ACTION

    override val resultDeserializer: DeserializationStrategy<GetRecordResult>
        get() = GetRecordResult.serializer()

    override val apiResultDeserializer: DeserializationStrategy<OneBotApiResult<GetRecordResult>>
        get() = RES_SER

    public companion object Factory {
        private const val ACTION: String = "get_record"

        private val RES_SER: KSerializer<OneBotApiResult<GetRecordResult>> =
            OneBotApiResult.serializer(GetRecordResult.serializer())

        /**
         * 构建一个 [GetRecordApi].
         *
         * @param file 收到的语音文件名（消息段的 `file` 参数），如 `0B38145AA44505000B38145AA4450500.silk`
         * @param outFormat 要转换到的格式，目前支持 `mp3`、`amr`、`wma`、`m4a`、`spx`、`ogg`、`wav`、`flac`
         */
        @JvmStatic
        public fun create(`file`: String, outFormat: String): GetRecordApi = GetRecordApi(
            Body(
                file,
                outFormat
            )
        )
    }

    /**
     * @param file 收到的语音文件名（消息段的 `file` 参数），如 `0B38145AA44505000B38145AA4450500.silk`
     * @param outFormat 要转换到的格式，目前支持 `mp3`、`amr`、`wma`、`m4a`、`spx`、`ogg`、`wav`、`flac`
     */
    @Serializable
    internal data class Body(
        internal val `file`: String,
        @SerialName("out_format")
        internal val outFormat: String,
    )
}

/**
 * [GetRecordApi] 的响应体。
 *
 * @param file 转换后的语音文件路径，如 `/home/somebody/cqhttp/data/record/0B38145AA44505000B38145AA4450500.mp3`
 */
@Serializable
public data class GetRecordResult @ApiResultType internal constructor(
    public val `file`: String,
)
