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

package love.forte.simbot.component.onebot.v11.core.message.segment

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.jvm.JvmOverloads
import kotlin.jvm.JvmStatic

/**
 * [匿名发消息](https://github.com/botuniverse/onebot-11/blob/master/message/segment.md#%E5%8C%BF%E5%90%8D%E5%8F%91%E6%B6%88%E6%81%AF-)
 */
@Serializable
@SerialName(OneBotAnonymous.TYPE)
public class OneBotAnonymous private constructor(
    override val data: Data
) : OneBotMessageSegment {
    /**
     * Data of [OneBotAnonymous].
     *
     * @property ignore 可选，表示无法匿名时是否继续发送
     */
    @Serializable
    public data class Data(val ignore: Boolean?)

    public companion object Factory {
        public const val TYPE: String = "anonymous"

        private val NULL = OneBotAnonymous(Data(null))
        private val TRUE = OneBotAnonymous(Data(true))
        private val FALSE = OneBotAnonymous(Data(false))

        /**
         * 构建一个 [OneBotAnonymous].
         * @param ignore 可选，表示无法匿名时是否继续发送
         */
        @JvmStatic
        @JvmOverloads
        public fun create(ignore: Boolean? = null): OneBotAnonymous =
            when (ignore) {
                null -> NULL
                true -> TRUE
                false -> FALSE
            }
    }


    override fun toString(): String {
        return "OneBotAnonymous(ignore=${data.ignore})"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is OneBotAnonymous) return false

        if (data != other.data) return false

        return true
    }

    override fun hashCode(): Int {
        return data.hashCode()
    }
}
