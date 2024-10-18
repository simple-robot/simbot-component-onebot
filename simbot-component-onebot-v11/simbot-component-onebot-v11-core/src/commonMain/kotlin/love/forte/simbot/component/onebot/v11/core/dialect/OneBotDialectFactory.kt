package love.forte.simbot.component.onebot.v11.core.dialect

import love.forte.simbot.component.onebot.v11.core.bot.OneBotBot

/**
 *
 * @author ForteScarlet
 */
public interface OneBotDialectFactory {
    // TODO

    public fun createDialect(bot: OneBotBot): OneBotDialect

}
