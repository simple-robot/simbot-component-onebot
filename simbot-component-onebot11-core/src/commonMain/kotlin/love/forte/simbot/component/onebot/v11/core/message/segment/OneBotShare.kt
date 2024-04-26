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
 * [链接分享](https://github.com/botuniverse/onebot-11/blob/master/message/segment.md#%E9%93%BE%E6%8E%A5%E5%88%86%E4%BA%AB)
 */
@Serializable
@SerialName(OneBotShare.TYPE)
public class OneBotShare private constructor(
    override val data: Data
) : OneBotMessageSegment {

    /**
     * Data of [OneBotShare].
     */
    @Serializable
    public data class Data(
        val url: String,
        val title: String,
        val content: String?,
        val image: String?
    )

    public companion object Factory {
        public const val TYPE: String = "share"

        /**
         * 构建一个 [OneBotShare].
         */
        @JvmStatic
        public fun create(data: Data): OneBotShare =
            OneBotShare(data)

        /**
         * 构建一个 [OneBotShare].
         */
        @JvmStatic
        @JvmOverloads
        public fun create(
            url: String,
            title: String,
            content: String? = null,
            image: String? = null
        ): OneBotShare = create(Data(url, title, content, image))
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is OneBotShare) return false

        if (data != other.data) return false

        return true
    }

    override fun hashCode(): Int {
        return data.hashCode()
    }

    override fun toString(): String {
        return "OneBotShare(data=$data)"
    }

}
