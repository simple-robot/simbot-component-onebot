package love.forte.simbot.component.onebot.v11.core.bot.internal

import kotlinx.serialization.json.JsonObject
import love.forte.simbot.common.id.LongID
import love.forte.simbot.component.onebot.v11.core.event.ExperimentalCustomEventResolverApi
import love.forte.simbot.component.onebot.v11.core.event.RawEventResolveResult
import love.forte.simbot.component.onebot.v11.event.RawEvent

/**
 * 一个事件文本被进行解析后的主要内容。
 *
 * @author ForteScarlet
 */
@ExperimentalCustomEventResolverApi
internal data class RawEventResolveResultImpl(
    override val text: String,
    override val json: JsonObject,
    override val postType: String,
    override val subType: String?,
    override val time: Long?,
    override val selfId: LongID?,
    override val rawEvent: RawEvent?,
    override val reason: Throwable?
) : RawEventResolveResult
