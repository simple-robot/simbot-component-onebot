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

import love.forte.simbot.ability.DeleteOption
import love.forte.simbot.ability.DeleteSupport
import love.forte.simbot.ability.StandardDeleteOption
import love.forte.simbot.common.id.ID
import love.forte.simbot.common.id.literal
import love.forte.simbot.component.onebot.v11.common.utils.qqAvatar640
import love.forte.simbot.component.onebot.v11.core.api.SetGroupKickApi
import love.forte.simbot.component.onebot.v11.core.bot.OneBotBot
import love.forte.simbot.component.onebot.v11.message.OneBotMessageReceipt
import love.forte.simbot.definition.Member
import love.forte.simbot.message.Message
import love.forte.simbot.message.MessageContent
import love.forte.simbot.suspendrunner.ST
import kotlin.coroutines.CoroutineContext
import kotlin.jvm.JvmName
import kotlin.jvm.JvmStatic
import kotlin.jvm.JvmSynthetic


/**
 * OneBot中的 `member` 的 [Member] 实现。
 *
 * 群成员通常的来源：
 * - 来自事件
 * - 来自 Group API
 * ([OneBotGroup.members])
 *
 * ## DeleteSupport
 *
 * [OneBotMember] 实现 [DeleteSupport]，
 * [delete] 代表试着踢出这个群成员。
 *
 * @author ForteScarlet
 */
public interface OneBotMember : Member, DeleteSupport {
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

    /**
     * 向此成员发送消息。
     *
     * @throws Throwable 任何可能在请求API时得到的异常
     */
    @ST
    override suspend fun send(text: String): OneBotMessageReceipt

    /**
     * 向此成员发送消息。
     *
     * @throws Throwable 任何可能在请求API时得到的异常
     */
    @ST
    override suspend fun send(message: Message): OneBotMessageReceipt

    /**
     * 向此成员发送消息。
     *
     * @throws Throwable 任何可能在请求API时得到的异常
     */
    @ST
    override suspend fun send(messageContent: MessageContent): OneBotMessageReceipt

    /**
     * 尝试踢出此群成员，类似于 `kick` 行为。
     *
     * @param options 删除时可提供的额外选项。
     * 可用：
     * - [StandardDeleteOption.IGNORE_ON_FAILURE]
     * - [OneBotMemberDeleteOption] 的所有实现
     *
     * @throws Throwable 任何可能在请求API时得到的异常
     */
    @JvmSynthetic
    override suspend fun delete(vararg options: DeleteOption) {
        StandardDeleteOption
        SetGroupKickApi
    }
}

/**
 * 在 [OneBotMember.delete] 中可以使用的更多额外属性，
 * 大多与 [SetGroupKickApi] 中的属性相关。
 *
 * @see OneBotMember.delete
 */
public sealed class OneBotMemberDeleteOption : DeleteOption {

    /**
     * 拒绝此人的加群请求，也就是踢出后将其屏蔽。
     *
     * @see SetGroupKickApi
     */
    public data object RejectRequest : OneBotMemberDeleteOption()

    public companion object {
        /**
         * 拒绝此人的加群请求，也就是踢出后将其屏蔽。
         *
         * @see SetGroupKickApi
         * @see RejectRequest
         */
        @get:JvmStatic
        @get:JvmName("rejectRequest")
        public val rejectRequest: RejectRequest
            get() = RejectRequest

    }
}
