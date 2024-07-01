package love.forte.simbot.component.onebot.v11.core.utils

import love.forte.simbot.common.id.IntID.Companion.ID
import love.forte.simbot.component.onebot.v11.message.segment.OneBotAt
import love.forte.simbot.component.onebot.v11.message.segment.OneBotDice
import love.forte.simbot.component.onebot.v11.message.segment.OneBotReply
import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import kotlin.test.assertIs


/**
 *
 * @author ForteScarlet
 */
class ReplyMessageSegmentListTests {

    @Test
    fun resolveReplyMessageSegmentListWithoutReplyTest() {
        val list = listOf(
            OneBotAt.create("1"),
            OneBotAt.createAtAll(),
            OneBotDice,
        )

        val newList = resolveReplyMessageSegmentList(list, 0.ID)

        assertEquals(4, newList.size)
        assertIs<OneBotReply>(newList.first())
        assertContentEquals(list, newList.subList(1, newList.size))
    }

    @Test
    fun resolveReplyMessageSegmentListWithReplyTest() {
        val list = listOf(
            OneBotAt.create("1"),
            OneBotReply.create(10.ID),
            OneBotDice,
        )

        val newList = resolveReplyMessageSegmentList(list, 0.ID)

        println(newList)

        assertEquals(3, newList.size)
        assertContentEquals(list, newList)
    }

}
