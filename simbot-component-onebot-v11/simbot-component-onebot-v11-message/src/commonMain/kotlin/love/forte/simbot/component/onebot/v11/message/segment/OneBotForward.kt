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

package love.forte.simbot.component.onebot.v11.message.segment

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import love.forte.simbot.common.id.ID
import kotlin.jvm.JvmStatic


/**
 * [合并转发](https://github.com/botuniverse/onebot-11/blob/master/message/segment.md#%E5%90%88%E5%B9%B6%E8%BD%AC%E5%8F%91-)
 *
 * @author ForteScarlet
 */
@Serializable
@SerialName(OneBotForward.TYPE)
public class OneBotForward private constructor(
    override val data: Data
) : OneBotMessageSegment {

    /**
     * Get the [Data.id].
     */
    public val id: ID
        get() = data.id

    public companion object Factory {
        public const val TYPE: String = "forward"

        /**
         * Create [OneBotForward].
         *
         * @param id 合并转发 ID, see [Data.id]
         */
        @JvmStatic
        public fun create(id: ID): OneBotForward =
            OneBotForward(Data(id))
    }

    /**
     * Data of [OneBotForward]
     *
     * @property id 合并转发 ID，需通过
     * [get_forward_msg API](https://github.com/botuniverse/onebot-11/blob/master/api/public.md#get_forward_msg-%E8%8E%B7%E5%8F%96%E5%90%88%E5%B9%B6%E8%BD%AC%E5%8F%91%E6%B6%88%E6%81%AF)
     * 获取具体内容
     */
    @Serializable
    public data class Data(val id: ID)
}


/**
 * - [合并转发节点](https://github.com/botuniverse/onebot-11/blob/master/message/segment.md#%E5%90%88%E5%B9%B6%E8%BD%AC%E5%8F%91%E8%8A%82%E7%82%B9-)
 * - [合并转发自定义节点](https://github.com/botuniverse/onebot-11/blob/master/message/segment.md#%E5%90%88%E5%B9%B6%E8%BD%AC%E5%8F%91%E8%87%AA%E5%AE%9A%E4%B9%89%E8%8A%82%E7%82%B9)
 * @author ForteScarlet
 */
@Serializable
@SerialName(OneBotForwardNode.TYPE)
public class OneBotForwardNode private constructor(
    override val data: Data
) : OneBotMessageSegment {

    override fun toString(): String {
        return "OneBotForwardNode(data=$data)"
    }

    public companion object Factory {
        public const val TYPE: String = "node"

        /**
         * Create [OneBotForwardNode].
         *
         */
        @JvmStatic
        public fun create(id: ID): OneBotForwardNode =
            OneBotForwardNode(Data(id))

        /**
         * Create a `custom` [OneBotForwardNode].
         *
         */
        @JvmStatic
        public fun create(
            userId: ID,
            nickname: String,
            content: List<OneBotMessageSegment>
        ): OneBotForwardNode =
            OneBotForwardNode(
                Data(
                    id = null,
                    userId = userId,
                    nickname = nickname,
                    content = content,
                )
            )
    }

    /**
     * 一个普通的节点或一个自定义节点.
     * 如果没有 [id]，则应当存在 [userId]、[nickname]、[content]。
     *
     * @author ForteScarlet
     */
    @Serializable
    public data class Data(
        val id: ID? = null,
        @SerialName("user_id")
        val userId: ID? = null,
        val nickname: String? = null,
        val content: List<OneBotMessageSegment>? = null,
    )
}

