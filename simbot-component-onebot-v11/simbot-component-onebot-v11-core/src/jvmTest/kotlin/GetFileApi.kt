import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import love.forte.simbot.component.onebot.v11.core.api.OneBotApi
import love.forte.simbot.component.onebot.v11.core.api.OneBotApiResult

// LLOneBot GetFile API

class GetFileApi private constructor(
    override val body: Any
) : OneBotApi<GetFileResult> {

    override val action: String
        get() = ACTION

    override val resultDeserializer: DeserializationStrategy<GetFileResult>
        get() = GetFileResult.serializer()

    override val apiResultDeserializer: DeserializationStrategy<OneBotApiResult<GetFileResult>>
        get() = RESULT_RES

    companion object {
        private const val ACTION: String = "get_file"
        private val RESULT_RES = OneBotApiResult.serializer(GetFileResult.serializer())

        @JvmStatic
        fun create(fileId: String): GetFileApi {
            return GetFileApi(Body(fileId))
        }
    }

    @Serializable
    internal data class Body(
        @SerialName("file_id")
        internal val fileId: String,
    )
}

@Serializable
data class GetFileResult(
    /**
     * 文件的绝对路径
     */
    val file: String,
    @SerialName("file_name")
    val fileName: String = "",
    @SerialName("file_size")
    val fileSize: Long = -1L,
    val base64: String = "",
)
