package love.forte.simbot.component.onebot.v11.core.api

import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.builtins.ListSerializer
import kotlin.jvm.JvmStatic

/**
 * [`get_group_list`-获取群列表](https://github.com/botuniverse/onebot-11/blob/master/api/public.md#get_group_list-获取群列表)
 *
 * @author ForteScarlet
 */
public class GetGroupListApi private constructor() : OneBotApi<List<GetGroupInfoResult>> {
    override val action: String
        get() = ACTION

    override val resultDeserializer: DeserializationStrategy<List<GetGroupInfoResult>>
        get() = SER

    override val apiResultDeserializer: DeserializationStrategy<OneBotApiResult<List<GetGroupInfoResult>>>
        get() = RES_SER

    override val body: Any?
        get() = null

    public companion object Factory {
        private const val ACTION: String = "get_group_list"

        private val SER = ListSerializer(GetGroupInfoResult.serializer())

        private val RES_SER = OneBotApiResult.serializer(SER)

        private val INSTANCE: GetGroupListApi = GetGroupListApi()

        /**
         * 构建一个 [GetGroupListApi].
         */
        @JvmStatic
        public fun create(): GetGroupListApi = INSTANCE
    }
}
