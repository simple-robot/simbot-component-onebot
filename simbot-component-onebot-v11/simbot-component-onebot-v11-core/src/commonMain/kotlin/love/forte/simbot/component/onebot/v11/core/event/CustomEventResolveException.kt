package love.forte.simbot.component.onebot.v11.core.event

/**
 * 当 [CustomEventResolver] 发生异常时，
 * 异常会被收集到 [CustomEventResolveException.suppressedExceptions] 中，
 * 并被最终输出。
 *
 * @since 1.8.0
 *
 * @author ForteScarlet
 */
public open class CustomEventResolveException : RuntimeException {
    public constructor() : super()
    public constructor(cause: Throwable?) : super(cause)
    public constructor(message: String?) : super(message)
    public constructor(message: String?, cause: Throwable?) : super(message, cause)
}
