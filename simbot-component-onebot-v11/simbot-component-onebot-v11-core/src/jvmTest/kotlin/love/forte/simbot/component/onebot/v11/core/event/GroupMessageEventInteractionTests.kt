package love.forte.simbot.component.onebot.v11.core.event

import io.mockk.coEvery
import io.mockk.coVerifyOrder
import io.mockk.mockk
import io.mockk.spyk
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.overwriteWith
import love.forte.simbot.common.id.IntID.Companion.ID
import love.forte.simbot.component.onebot.common.annotations.ApiResultConstructor
import love.forte.simbot.component.onebot.v11.core.OneBot11
import love.forte.simbot.component.onebot.v11.core.api.SendMsgResult
import love.forte.simbot.component.onebot.v11.core.bot.OneBotBotConfiguration
import love.forte.simbot.component.onebot.v11.core.bot.internal.OneBotBotImpl
import love.forte.simbot.component.onebot.v11.core.component.OneBot11Component
import love.forte.simbot.component.onebot.v11.core.event.internal.message.OneBotNormalGroupMessageEventImpl
import love.forte.simbot.component.onebot.v11.core.event.message.OneBotNormalGroupMessageEvent
import love.forte.simbot.component.onebot.v11.core.event.messageinteraction.OneBotNormalGroupMessageEventPreReplyEvent
import love.forte.simbot.component.onebot.v11.core.useOneBot11
import love.forte.simbot.core.application.launchSimpleApplication
import love.forte.simbot.event.EventDispatcher
import love.forte.simbot.event.InteractionMessage
import love.forte.simbot.event.process
import love.forte.simbot.event.throwIfError
import kotlin.coroutines.EmptyCoroutineContext
import kotlin.test.Test


/**
 *
 * @author ForteScarlet
 */
class GroupMessageEventInteractionTests {
    private fun spykBot(dispatcher: EventDispatcher): OneBotBotImpl {
        return spyk(
            OneBotBotImpl(
                "1234",
                EmptyCoroutineContext,
                SupervisorJob(),
                OneBotBotConfiguration(),
                OneBot11Component(),
                dispatcher,
                Json(OneBot11.DefaultJson) {
                    serializersModule = serializersModule overwriteWith OneBot11.serializersModule
                }
            ),
            recordPrivateCalls = true
        )
    }

    @OptIn(ApiResultConstructor::class)
    @Test
    fun testNormalGroupInteractionEvent() = runTest {
        val app = launchSimpleApplication {
            useOneBot11()
        }

        app.eventDispatcher.process<OneBotNormalGroupMessageEvent> { event ->
            event.reply("Hello")
        }

        app.eventDispatcher.process<OneBotNormalGroupMessageEventPreReplyEvent> { event ->
            event.currentMessage = InteractionMessage.valueOf("Hello, World")
        }

        val bot = spykBot(app.eventDispatcher)
        coEvery { bot.executeData<SendMsgResult>(any()) } returns SendMsgResult(114.ID)

        val event = spyk(
            OneBotNormalGroupMessageEventImpl(
                null,
                mockk(relaxed = true),
                bot
            ),
            recordPrivateCalls = true
        )

        app.eventDispatcher.push(event).throwIfError().collect()

        coVerifyOrder {
            event.reply("Hello")
            event invoke "replyText" withArguments listOf("Hello, World")
        }
    }

}
