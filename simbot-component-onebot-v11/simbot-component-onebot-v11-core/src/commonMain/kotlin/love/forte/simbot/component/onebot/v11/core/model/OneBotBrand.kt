package love.forte.simbot.component.onebot.v11.core.model

/**
 * 协议端实现品牌
 */
public enum class OneBotBrand(public val displayName: String) {
    LLONEBOT("LLOneBot"),
    LAGRANGE("Lagrange"),
    UNKNOWN("Unknown");

    override fun toString(): String = displayName
}
