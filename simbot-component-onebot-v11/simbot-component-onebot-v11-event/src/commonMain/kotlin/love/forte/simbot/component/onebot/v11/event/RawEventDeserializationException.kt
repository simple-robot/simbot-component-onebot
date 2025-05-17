package love.forte.simbot.component.onebot.v11.event

/**
 *
 * [RawEvent] 的反序列化异常。
 * @since 1.8.0
 * @author ForteScarlet
 */
public open class RawEventDeserializationException : RuntimeException {
    public constructor() : super()
    public constructor(cause: Throwable?) : super(cause)
    public constructor(message: String?) : super(message)
    public constructor(message: String?, cause: Throwable?) : super(message, cause)
}
