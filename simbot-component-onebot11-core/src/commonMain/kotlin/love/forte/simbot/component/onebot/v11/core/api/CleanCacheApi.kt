package love.forte.simbot.component.onebot.v11.core.api

import kotlin.Any
import kotlin.String
import kotlin.Unit
import kotlin.jvm.JvmStatic
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.builtins.serializer

/**
 * [`clean_cache`-清理缓存](https://github.com/botuniverse/onebot-11/blob/master/api/public.md##clean_cache-清理缓存)
 *
 * @author ForteScarlet
 */
public class CleanCacheApi private constructor() : OneBotApi<Unit> {
    override val action: String
        get() = ACTION

    override val resultDeserializer: DeserializationStrategy<Unit>
        get() = Unit.serializer()

    override val apiResultDeserializer: DeserializationStrategy<OneBotApiResult<Unit>>
        get() = OneBotApiResult.emptySerializer()

    override val body: Any?
        get() = null

    public companion object Factory {
        private const val ACTION: String = "clean_cache"

        private val INSTANCE: CleanCacheApi = CleanCacheApi()

        /**
         * 构建一个 [CleanCacheApi].
         */
        @JvmStatic
        public fun create(): CleanCacheApi = INSTANCE
    }
}
