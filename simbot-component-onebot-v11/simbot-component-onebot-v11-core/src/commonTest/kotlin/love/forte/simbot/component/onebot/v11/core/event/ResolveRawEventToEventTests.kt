package love.forte.simbot.component.onebot.v11.core.event

import kotlinx.serialization.KSerializer
import love.forte.simbot.component.onebot.v11.core.OneBot11
import love.forte.simbot.component.onebot.v11.event.RawEvent
import love.forte.simbot.component.onebot.v11.event.message.RawGroupMessageEvent


/**
 *
 * @author ForteScarlet
 */
class ResolveRawEventToEventTests {

    // @Test // TODO
    fun resolveGroupMessageEventTest() {
        val event = decodeEvent(
            RawGroupMessageEvent.serializer(),
            ""
        )

    }
}

private fun <T : RawEvent> decodeEvent(
    serializer: KSerializer<out T>,
    raw: String
): T {
    return OneBot11.DefaultJson.decodeFromString(
        serializer,
        raw
    )
}

// GroupMessageEvent
// PrivateMessageEvent
// HeartbeatEvent
// LifecycleEvent
// FriendAddEvent
// FriendRecallEvent
// GroupAdminEvent
// GroupBanEvent
// GroupDecreaseEvent
// GroupIncreaseEvent
// GroupRecallEvent
// GroupUploadEvent
// NotifyEvent
// FriendRequestEvent
// GroupRequestEvent
