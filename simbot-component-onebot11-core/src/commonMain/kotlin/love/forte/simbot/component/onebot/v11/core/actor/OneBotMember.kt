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

package love.forte.simbot.component.onebot.v11.core.actor

import love.forte.simbot.common.id.ID
import love.forte.simbot.common.id.literal
import love.forte.simbot.component.onebot.v11.common.utils.qqAvatar640
import love.forte.simbot.component.onebot.v11.core.bot.OneBotBot
import love.forte.simbot.component.onebot.v11.message.OneBotMessageReceipt
import love.forte.simbot.definition.Member
import love.forte.simbot.message.Message
import love.forte.simbot.message.MessageContent
import love.forte.simbot.suspendrunner.ST
import kotlin.coroutines.CoroutineContext


/**
 * OneBot中的 `member` 的 [Member] 实现。
 *
 * 群成员通常的来源：
 * - 来自事件
 * - 来自 Group API
 * ([OneBotGroup.members])
 *
 * @author ForteScarlet
 */
public interface OneBotMember : Member {
    /**
     * 协程上下文。源自 [OneBotBot], 但是不含 [Job][kotlinx.coroutines.Job]。
     */
    override val coroutineContext: CoroutineContext

    /**
     * 成员群号
     */
    override val id: ID

    /**
     * 此成员所属角色。
     * 如果无法获取（例如是在群临时会话的私聊中）则得到 `null`
     */
    public val role: OneBotMemberRole?

    /**
     * 成员QQ头像
     */
    override val avatar: String
        get() = qqAvatar640(id.literal)

    @ST
    override suspend fun send(text: String): OneBotMessageReceipt

    @ST
    override suspend fun send(message: Message): OneBotMessageReceipt

    @ST
    override suspend fun send(messageContent: MessageContent): OneBotMessageReceipt

}
