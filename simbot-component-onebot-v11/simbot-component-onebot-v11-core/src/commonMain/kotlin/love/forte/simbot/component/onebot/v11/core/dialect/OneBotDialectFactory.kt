package love.forte.simbot.component.onebot.v11.core.dialect

import love.forte.simbot.component.onebot.v11.core.bot.OneBotBot

/**
 *
 * @author ForteScarlet
 */
@ExperimentalOneBotDialect
public interface OneBotDialectFactory {
    public fun createDialect(bot: OneBotBot): OneBotDialect


    public companion object Default : OneBotDialectFactory {
        override fun createDialect(bot: OneBotBot): OneBotDialect {
            return OneBotDialect(bot)
        }
    }
}
