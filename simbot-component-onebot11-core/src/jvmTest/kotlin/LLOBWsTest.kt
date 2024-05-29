import io.ktor.client.*
import io.ktor.client.engine.java.*
import io.ktor.client.plugins.websocket.*
import io.ktor.http.*
import io.ktor.websocket.*
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonPrimitive
import kotlinx.serialization.json.long
import love.forte.simbot.annotations.FragileSimbotAPI
import love.forte.simbot.common.id.LongID.Companion.ID
import love.forte.simbot.component.onebot.v11.core.OneBot11
import love.forte.simbot.component.onebot.v11.event.UnknownEvent
import love.forte.simbot.component.onebot.v11.event.resolveEventSerializer
import love.forte.simbot.component.onebot.v11.event.resolveEventSubTypeFieldName

@OptIn(FragileSimbotAPI::class)
suspend fun main() {
    val host = Url("ws://localhost:3001")
    val client = HttpClient(Java) {
        WebSockets {}
    }

    val json = OneBot11.DefaultJson

    client.ws(host.toString()) {
        incoming.receiveAsFlow().collect { frame ->
            when (frame) {
                is Frame.Text -> {
                    val text = frame.readText()
                    val obj = json.decodeFromString(JsonObject.serializer(), text)

                    val postType = obj["post_type"]!!.jsonPrimitive.content
                    val subTypeFieldName = resolveEventSubTypeFieldName(postType) ?: "${postType}_type"
                    val subType = obj[subTypeFieldName]!!.jsonPrimitive.content
                    val event = resolveEventSerializer(postType, subType)?.let {
                        json.decodeFromJsonElement(it, obj)
                    } ?: run {
                        val time = obj["time"]?.jsonPrimitive?.long ?: -1L
                        val selfId = obj["self_id"]?.jsonPrimitive?.long?.ID ?: 0L.ID
                        UnknownEvent(time, selfId, postType, text)
                    }

                    println("Event raw: $text")
                    println("Event:     $event")
                    println()
                }

                else -> {
                    println("Unknown event: $frame")
                }
            }

        }
    }

}
