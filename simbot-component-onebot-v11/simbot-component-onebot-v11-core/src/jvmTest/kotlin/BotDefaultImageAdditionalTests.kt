import kotlinx.coroutines.test.runTest
import love.forte.simbot.component.onebot.v11.core.bot.firstOneBotBotManager
import love.forte.simbot.component.onebot.v11.core.bot.register
import love.forte.simbot.component.onebot.v11.core.useOneBot11
import love.forte.simbot.component.onebot.v11.message.resolveToOneBotSegment
import love.forte.simbot.component.onebot.v11.message.segment.OneBotImage
import love.forte.simbot.core.application.launchSimpleApplication
import love.forte.simbot.message.OfflineImage.Companion.toOfflineImage
import love.forte.simbot.resource.toResource
import java.nio.file.Files
import kotlin.io.path.Path
import kotlin.io.path.absolutePathString
import kotlin.io.path.writeText
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertTrue


/**
 *
 * @author ForteScarlet
 */
class BotDefaultImageAdditionalTests {

    @Test
    fun botDefaultImageAdditionalTest() = runTest {
        val app = launchSimpleApplication {
            useOneBot11()
        }

        val obm = app.botManagers.firstOneBotBotManager()
        val bot1 = obm.register {
            botUniqueId = "123"
            defaultImageAdditionalParams(
                OneBotImage.AdditionalParams().apply {
                    localFileToBase64 = true
                }
            )
        }
        val bot2 = obm.register {
            botUniqueId = "456"
        }

        val f = Files.createTempFile(
            Path("."),
            "temp",
            "botDefaultImageAdditionalTestText",
        )
        f.toFile().deleteOnExit()
        f.writeText("Hello")

        val img = f.toResource().toOfflineImage()

        val seg1 = img.resolveToOneBotSegment(
            bot1.configuration.defaultImageAdditionalParamsProvider
        )

        assertIs<OneBotImage>(seg1)
        assertTrue(seg1.data.file.startsWith("base64"))

        val seg2 = img.resolveToOneBotSegment(
            bot2.configuration.defaultImageAdditionalParamsProvider
        )

        assertIs<OneBotImage>(seg2)
        assertEquals(f.absolutePathString(), seg2.data.file)

    }

}
