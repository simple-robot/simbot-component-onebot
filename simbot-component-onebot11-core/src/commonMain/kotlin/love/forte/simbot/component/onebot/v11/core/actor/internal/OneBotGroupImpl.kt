/*
 * Copyright (c) 2024. ForteScarlet.
 *
 * This file is part of simbot-component-onebot.
 *
 * simbot-component-onebot is free software: you can redistribute it and/or modify it under the terms
 * of the GNU Lesser General Public License as published by the Free Software Foundation,
 * either version 3 of the License, or (at your option) any later version.
 *
 * simbot-component-onebot is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with simbot-component-onebot.
 * If not, see <https://www.gnu.org/licenses/>.
 */

package love.forte.simbot.component.onebot.v11.core.actor.internal

import love.forte.simbot.component.onebot.v11.core.actor.OneBotGroup
import love.forte.simbot.component.onebot.v11.core.api.SendMsgApi
import love.forte.simbot.component.onebot.v11.core.bot.internal.OneBotBotImpl
import love.forte.simbot.component.onebot.v11.core.bot.requestDataBy
import love.forte.simbot.component.onebot.v11.core.internal.message.toReceipt
import love.forte.simbot.component.onebot.v11.core.utils.sendMsgApi
import love.forte.simbot.component.onebot.v11.core.utils.sendTextMsgApi
import love.forte.simbot.component.onebot.v11.message.OneBotMessageContent
import love.forte.simbot.component.onebot.v11.message.OneBotMessageReceipt
import love.forte.simbot.message.Message
import love.forte.simbot.message.MessageContent


internal abstract class OneBotGroupImpl : OneBotGroup {
    protected abstract val bot: OneBotBotImpl

    override suspend fun send(text: String): OneBotMessageReceipt {
        return sendTextMsgApi(
            messageType = SendMsgApi.MESSAGE_TYPE_GROUP,
            target = id,
            text = text,
        ).requestDataBy(bot).toReceipt()
    }

    override suspend fun send(messageContent: MessageContent): OneBotMessageReceipt {
        if (messageContent is OneBotMessageContent) {
            return sendMsgApi(
                messageType = SendMsgApi.MESSAGE_TYPE_GROUP,
                target = id,
                message = messageContent.sourceSegments,
            ).requestDataBy(bot).toReceipt()
        }

        return send(messageContent.messages)
    }

    override suspend fun send(message: Message): OneBotMessageReceipt {
        TODO("Not yet implemented")
    }
}
