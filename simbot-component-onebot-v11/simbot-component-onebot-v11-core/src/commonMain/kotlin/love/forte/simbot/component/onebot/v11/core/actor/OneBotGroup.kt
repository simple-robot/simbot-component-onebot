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

import love.forte.simbot.common.collectable.Collectable
import love.forte.simbot.common.collectable.asCollectable
import love.forte.simbot.common.id.ID
import love.forte.simbot.component.onebot.v11.core.bot.OneBotBot
import love.forte.simbot.component.onebot.v11.message.OneBotMessageReceipt
import love.forte.simbot.definition.ChatGroup
import love.forte.simbot.message.Message
import love.forte.simbot.message.MessageContent
import love.forte.simbot.suspendrunner.ST
import love.forte.simbot.suspendrunner.STP
import kotlin.coroutines.CoroutineContext


/**
 * OneBot中的 `group` 的 [ChatGroup] 实现。
 *
 * 群通常的来源：
 * - 来自事件
 * - 来自 Bot relation API
 * ([OneBotBot.groupRelation][love.forte.simbot.component.onebot.v11.core.bot.OneBotBot.groupRelation])
 *
 * @author ForteScarlet
 */
public interface OneBotGroup : ChatGroup {
    /**
     * 协程上下文。源自 [OneBotBot], 但是不含 [Job][kotlinx.coroutines.Job]。
     */
    override val coroutineContext: CoroutineContext

    /**
     * 群号
     */
    override val id: ID

    /**
     * 群内的全部角色权限。
     * 即 [OneBotMemberRole]
     * 的枚举元素。
     *
     * @see OneBotMemberRole
     */
    override val roles: Collectable<OneBotMemberRole>
        get() = OneBotMemberRole.entries.asCollectable()

    /**
     * 获取群内所有成员集合。
     */
    override val members: Collectable<OneBotMember>

    /**
     * 根据ID寻找指定的成员。
     */
    @ST(blockingBaseName = "getMember", blockingSuffix = "", asyncBaseName = "getMember", reserveBaseName = "getMember")
    override suspend fun member(id: ID): OneBotMember?

    /**
     * 将当前所属Bot作为一个 [OneBotMember] 获取。
     *
     * @throws IllegalStateException 如果最终没有找到bot的user id的用户信息，
     * 例如可能bot已经离开了这个群
     * @throws Exception 请求API过程中可能产生的任何异常
     */
    @STP
    override suspend fun botAsMember(): OneBotMember


    @ST
    override suspend fun send(message: Message): OneBotMessageReceipt

    @ST
    override suspend fun send(messageContent: MessageContent): OneBotMessageReceipt

    @ST
    override suspend fun send(text: String): OneBotMessageReceipt

}
