package love.forte.simbot.component.onebot.v11.core.event

/**
 * 事件解析异常
 *
 * @since 1.8.0
 * @author ForteScarlet
 */
public open class EventResolveException : RuntimeException {
    public constructor() : super()
    public constructor(cause: Throwable?) : super(cause)
    public constructor(message: String?) : super(message)
    public constructor(message: String?, cause: Throwable?) : super(message, cause)
}
