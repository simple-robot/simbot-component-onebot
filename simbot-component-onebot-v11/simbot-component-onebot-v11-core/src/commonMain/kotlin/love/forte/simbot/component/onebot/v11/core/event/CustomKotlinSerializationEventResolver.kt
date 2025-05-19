package love.forte.simbot.component.onebot.v11.core.event

import kotlinx.serialization.DeserializationStrategy
import love.forte.simbot.event.Event

/**
 * 基于 Kotlin Serialization 反序列化器的 [CustomEventResolver]。
 * @since 1.8.0
 * @author ForteScarlet
 */
@ExperimentalCustomEventResolverApi
public fun interface CustomKotlinSerializationEventResolver : CustomEventResolver {

    /**
     * 根据 [context] 得到一个 [DeserializationStrategy]。
     */
    public fun serializer(context: CustomEventResolver.Context): DeserializationStrategy<Event>?

    override fun resolve(context: CustomEventResolver.Context): Event? {
        return serializer(context)?.let {
            return context.json.decodeFromJsonElement(it, context.rawEventResolveResult.json)
        }
    }
}
