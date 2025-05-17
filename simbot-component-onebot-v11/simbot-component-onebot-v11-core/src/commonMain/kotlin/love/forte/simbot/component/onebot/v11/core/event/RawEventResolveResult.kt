package love.forte.simbot.component.onebot.v11.core.event

import kotlinx.serialization.json.JsonObject
import love.forte.simbot.common.id.LongID
import love.forte.simbot.component.onebot.v11.event.RawEvent

/**
 * 一个原始事件文本被解析后的基本结果。
 *
 * @since 1.8.0
 * @author ForteScarlet
 */
@ExperimentalCustomEventResolverApi
public interface RawEventResolveResult {
    /**
     * 原始的事件JSON字符串文本。
     */
    public val text: String

    /**
     * 经过解析后的 [JsonObject]。
     */
    public val json: JsonObject

    /**
     * [json] 的 `post_type` 属性。
     * 在OneBot协议中这个属性的必须的，用于对事件进行首层分类。
     */
    public val postType: String

    /**
     * [json] 的 `sub_type` 属性。
     * 在OneBot标准协议中这个属性始终存在，且获取它的 JSON KEY
     * 等同于 `$postType_type`。
     * 以标准事件为例子，
     * ```json
     * {
     *   "post_type": "message",
     *   "message_type": "private"
     * }
     * ```
     * 当 `post_type` 为 `message` 时，`sub_type` 即代表 `message_type`，则其值为 `private`。
     */
    public val subType: String?

    /**
     * jsonObject 的 `time` 属性。
     */
    public val time: Long?

    /**
     * jsonObject 的 `self_id` 属性。是一个长整型ID。
     */
    public val selfId: LongID?

    /**
     * 如果能被标准事件类型成功解析，则此处为被解析出来的标准事件，否则为 `null`。
     */
    public val rawEvent: RawEvent?

    /**
     * 如果 [rawEvent] 是因为某些异常（例如序列化异常）才导致无法解析得到 `null` 的，
     * 则此处为解析时的异常。
     */
    public val reason: Throwable?
}
