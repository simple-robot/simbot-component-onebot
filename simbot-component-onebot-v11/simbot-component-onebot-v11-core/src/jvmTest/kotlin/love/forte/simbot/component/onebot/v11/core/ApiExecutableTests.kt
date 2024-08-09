package love.forte.simbot.component.onebot.v11.core

import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import love.forte.simbot.component.onebot.v11.core.api.OneBotApi
import love.forte.simbot.component.onebot.v11.core.api.OneBotApiExecutable
import love.forte.simbot.component.onebot.v11.core.api.inExecutableScope

/**
 *
 * @author ForteScarlet
 */
class ApiExecutableTests {
    // see https://github.com/mockk/mockk/issues/1282

    // @Test
    fun executableScopeTest() = runTest {
        val executable = mockk<OneBotApiExecutable>(relaxed = true)
        val api = mockk<OneBotApi<*>>(relaxed = true)

        executable.inExecutableScope {
            api.execute()
        }

        coVerify { executable.execute(any()) }
    }

}
