package love.forte.simbot.component.onebot.v11.core.api

import kotlin.Any
import kotlin.Long
import kotlin.String
import kotlin.Unit
import kotlin.jvm.JvmOverloads
import kotlin.jvm.JvmStatic
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.serializer

/**
 * [`set_restart`-重启 OneBot
 * 实现](https://github.com/botuniverse/onebot-11/blob/master/api/public.md##set_restart-重启-onebot-实现)
 *
 * @author ForteScarlet
 */
public class SetRestartApi private constructor(
    override val body: Any,
) : OneBotApi<Unit> {
    override val action: String
        get() = ACTION

    override val resultDeserializer: DeserializationStrategy<Unit>
        get() = Unit.serializer()

    override val apiResultDeserializer: DeserializationStrategy<OneBotApiResult<Unit>>
        get() = OneBotApiResult.emptySerializer()

    public companion object Factory {
        private const val ACTION: String = "set_restart"

        /**
         * 构建一个 [SetRestartApi].
         *
         * @param delay 要延迟的毫秒数，如果默认情况下无法重启，可以尝试设置延迟为 2000 左右
         */
        @JvmStatic
        @JvmOverloads
        public fun create(delay: Long? = null): SetRestartApi = SetRestartApi(Body(delay))
    }

    /**
     * @param delay 要延迟的毫秒数，如果默认情况下无法重启，可以尝试设置延迟为 2000 左右
     */
    @Serializable
    internal data class Body(
        internal val delay: Long? = null,
    )
}
