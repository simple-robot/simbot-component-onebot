package love.forte.simbot.component.onebot.v11.core

import io.ktor.client.engine.java.Java
import io.ktor.http.Url
import love.forte.simbot.application.listeners
import love.forte.simbot.bot.get
import love.forte.simbot.component.onebot.v11.core.bot.OneBotBotConfiguration
import love.forte.simbot.component.onebot.v11.core.bot.OneBotBotManager
import love.forte.simbot.component.onebot.v11.core.component.OneBot11Component
import love.forte.simbot.component.onebot.v11.core.event.message.OneBotPrivateMessageEvent
import love.forte.simbot.core.application.launchSimpleApplication
import love.forte.simbot.event.process

suspend fun main() {
    val app = launchSimpleApplication {
        install(OneBot11Component)
        install(OneBotBotManager)
    }

    app.listeners {
        process<OneBotPrivateMessageEvent> { event ->
            println("Event: $event")
            println("Event.plainText: ${event.messageContent.plainText}")
            println("Event.messages: ${event.messageContent.messages}")

            event.reply("喵")
        }

    }

    app.botManagers.get<OneBotBotManager>().apply {
        val bot = register(
            OneBotBotConfiguration().apply {
                botUniqueId = "2240189254"
                apiServerHost = Url("http://localhost:3000")
                eventServerHost = Url("ws://localhost:3001")
                wsClientEngineFactory = Java
                apiClientEngineFactory = Java
                accessToken("test")
            }
        )

        bot.start()
    }

    app.join()
}
