package love.forte.simbot.component.onebot.v11.core.api

import kotlin.Any
import kotlin.String
import kotlin.Unit
import kotlin.jvm.JvmStatic
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.builtins.serializer

/**
 * [`get_group_list`-获取群列表](https://github.com/botuniverse/onebot-11/blob/master/api/public.md#get_group_list-获取群列表)
 *
 * @author ForteScarlet
 */
public class GetGroupListApi private constructor() : OneBotApi<Unit> {
    override val action: String
        get() = ACTION

    override val resultDeserializer: DeserializationStrategy<Unit>
        get() = Unit.serializer()

    override val apiResultDeserializer: DeserializationStrategy<OneBotApiResult<Unit>>
        get() = OneBotApiResult.emptySerializer()

    override val body: Any?
        get() = null

    public companion object Factory {
        private const val ACTION: String = "get_group_list"

        private val INSTANCE: GetGroupListApi = GetGroupListApi()

        /**
         * 构建一个 [GetGroupListApi].
         */
        @JvmStatic
        public fun create(): GetGroupListApi = INSTANCE
    }
}
