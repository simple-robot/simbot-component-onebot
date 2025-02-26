package love.forte.simbot.component.onebot.v11.core.event

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.spyk
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.overwriteWith
import love.forte.simbot.common.id.IntID.Companion.ID
import love.forte.simbot.common.id.UUID
import love.forte.simbot.component.onebot.common.annotations.ApiResultConstructor
import love.forte.simbot.component.onebot.v11.core.OneBot11
import love.forte.simbot.component.onebot.v11.core.actor.internal.OneBotGroupApiResultImpl
import love.forte.simbot.component.onebot.v11.core.api.SendMsgResult
import love.forte.simbot.component.onebot.v11.core.bot.OneBotBotConfiguration
import love.forte.simbot.component.onebot.v11.core.bot.internal.OneBotBotImpl
import love.forte.simbot.component.onebot.v11.core.component.OneBot11Component
import love.forte.simbot.component.onebot.v11.core.event.messageinteraction.OneBotGroupPostSendEvent
import love.forte.simbot.component.onebot.v11.core.event.messageinteraction.OneBotSegmentsInteractionMessage
import love.forte.simbot.core.application.launchSimpleApplication
import love.forte.simbot.event.EventDispatcher
import love.forte.simbot.event.EventListener
import love.forte.simbot.event.InteractionMessage
import love.forte.simbot.event.process
import love.forte.simbot.message.MessageReceipt
import kotlin.coroutines.EmptyCoroutineContext
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertIs
import kotlin.test.assertSame


/**
 *
 * @author ForteScarlet
 */
@OptIn(ApiResultConstructor::class)
class GroupSendInteractionTests {
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

    @Test
    fun testSendTextInterception() {
        val bot: OneBotBotImpl = spykBot(mockk())
        val group = spyk(
            OneBotGroupApiResultImpl(
                source = mockk(relaxed = true),
                bot = bot,
                ownerId = UUID.random()
            ),
        )

        coEvery { bot.executeData<SendMsgResult>(any()) } returns SendMsgResult(114.ID)

        runBlocking { group.send("Hello World") }

        coVerify { group.send("Hello World") }
        coVerify {
            bot.emitMessagePreSendEvent(
                match {
                    it.message is OneBotSegmentsInteractionMessage &&
                        (it.message as? OneBotSegmentsInteractionMessage)?.message is InteractionMessage.Text &&
                        (it.message as? OneBotSegmentsInteractionMessage)?.segments == null
                }
            )
        }
    }

    @Test
    fun testSendTextInterceptionExceptionCollection() {
        val app = runBlocking { launchSimpleApplication() }

        val l1 = EventListener { throw IllegalStateException("ERROR1") }
        val l2 = EventListener { throw IllegalArgumentException("ERROR2") }

        app.eventDispatcher.register(l1)
        app.eventDispatcher.register(l2)

        val dispatcher = spyk(app.eventDispatcher, recordPrivateCalls = true)

        val bot = spykBot(dispatcher)

        val group = spyk(
            OneBotGroupApiResultImpl(
                source = mockk(relaxed = true),
                bot = bot,
                ownerId = UUID.random()
            ),
        )
        coEvery { bot.executeData<SendMsgResult>(any()) } returns SendMsgResult(114.ID)

        val error = assertFailsWith<OneBotInternalInterceptionException> {
            runBlocking { group.send("Hello World") }
        }

        assertEquals(2, error.suppressed.size)
        assertIs<IllegalStateException>(error.suppressed[0])
        assertEquals("ERROR1", error.suppressed[0].message)
        assertIs<IllegalArgumentException>(error.suppressed[1])
        assertEquals("ERROR2", error.suppressed[1].message)

        coVerify { group.send("Hello World") }
        coVerify {
            bot.emitMessagePreSendEvent(
                match {
                    it.message is OneBotSegmentsInteractionMessage &&
                        (it.message as? OneBotSegmentsInteractionMessage)?.message is InteractionMessage.Text &&
                        (it.message as? OneBotSegmentsInteractionMessage)?.segments == null
                }
            )
        }
    }

    @Test
    fun testGroupPostSend() = runTest {
        val app = launchSimpleApplication()

        val receiptFromEvent = CompletableDeferred<MessageReceipt>()

        app.eventDispatcher.process<OneBotGroupPostSendEvent> { event ->
            receiptFromEvent.complete(event.receipt)
        }

        val dispatcher = spyk(app.eventDispatcher, recordPrivateCalls = true)

        val bot = spykBot(dispatcher)

        val group = spyk(
            OneBotGroupApiResultImpl(
                source = mockk(relaxed = true),
                bot = bot,
                ownerId = UUID.random()
            ),
        )

        coEvery { bot.executeData<SendMsgResult>(any()) } returns SendMsgResult(114.ID)
        val receiptFromSend = group.send("Hello World")

        coVerify { group.send("Hello World") }

        assertSame(receiptFromSend, receiptFromEvent.await())
    }
}
