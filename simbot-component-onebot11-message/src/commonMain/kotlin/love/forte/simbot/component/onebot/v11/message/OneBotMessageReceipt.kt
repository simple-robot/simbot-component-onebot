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

package love.forte.simbot.component.onebot.v11.message

import love.forte.simbot.ability.DeleteOption
import love.forte.simbot.common.id.ID
import love.forte.simbot.message.MessageReceipt


/**
 * OneBot组件中，消息发送成功后得到的回执。
 *
 * @author ForteScarlet
 */
public interface OneBotMessageReceipt : MessageReceipt {
    /**
     * 消息发送后的结果id。
     */
    public val messageId: ID

    /**
     * 删除此消息。
     *
     * @throws Exception 任何请求API过程中可能会产生的异常，
     * 例如因权限不足或消息不存在得到的请求错误
     */
    override suspend fun delete(vararg options: DeleteOption)
}

// 实现在core模块：因为要使用到API
