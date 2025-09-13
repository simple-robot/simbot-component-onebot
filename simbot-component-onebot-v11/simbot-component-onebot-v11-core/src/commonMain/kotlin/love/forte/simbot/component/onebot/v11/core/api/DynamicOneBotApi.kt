package love.forte.simbot.component.onebot.v11.core.api

import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.http.content.*


public interface DynamicOneBotApi<T : Any> : OneBotApi<T>


/**
 * 一个用于动态构建 OneBot API 的构建器，
 * 可以在不直接实现 [OneBotApi] 的情况下以 DSL 或链式 API 的形式构建一个 [OneBotApi]。
 *
 * @since 1.9.0
 *
 * @author ForteScarlet
 */
public interface DynamicOneBotApiBuilder<T : Any> {
    /**
     * API 请求方式。默认为 [HttpMethod.Post].
     */
    public val method: HttpMethod

    /**
     * OneBot API 的动作/行为名称。
     */
    public val action: String

    /**
     * action 之后的额外附加行为。例如 `_async`。
     */
    public val actionSuffixes: MutableList<String>

    /**
     * 请求体。在非 Get 请求时使用。
     * body 的类型可以是：
     *
     * - null
     * - 一个 JSON 字符串
     * - 一个支持通过 kotlinx-serialization 序列化的任意对象
     * - 一个可以被 ktor 的 [HttpRequestBuilder.setBody][setBody] 识别的其他类型
     *   - 例如 [OutgoingContent]
     *
     * 如果 [method] 是 [HttpMethod.Get]，body 必须为 null，否则会抛出 [IllegalArgumentException]。
     *
     * @throws IllegalArgumentException 如果在 method 为 [HttpMethod.Get] 时 设置非 null 值
     */
    public var body: Any?


    public fun build(): DynamicOneBotApi<T>
}
