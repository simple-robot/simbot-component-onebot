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

import love.forte.simbot.component.onebot.v11.core.actor.OneBotMember
import love.forte.simbot.component.onebot.v11.message.OneBotMessageReceipt
import love.forte.simbot.message.Message
import love.forte.simbot.message.MessageContent
import love.forte.simbot.message.MessageReceipt


internal abstract class OneBotMemberImpl : OneBotMember {
    override suspend fun send(text: String): OneBotMessageReceipt {
        TODO("Not yet implemented")
    }

    override suspend fun send(message: Message): OneBotMessageReceipt {
        TODO("Not yet implemented")
    }

    override suspend fun send(messageContent: MessageContent): OneBotMessageReceipt {
        TODO("Not yet implemented")
    }
}
