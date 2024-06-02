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

@file:JvmName("MessageElementResolvers")

package love.forte.simbot.component.onebot.v11.message

import love.forte.simbot.annotations.InternalSimbotAPI
import love.forte.simbot.common.id.StringID.Companion.ID
import love.forte.simbot.component.onebot.v11.message.segment.OneBotAt
import love.forte.simbot.component.onebot.v11.message.segment.OneBotFace
import love.forte.simbot.component.onebot.v11.message.segment.OneBotMessageSegment
import love.forte.simbot.component.onebot.v11.message.segment.OneBotMessageSegmentElement
import love.forte.simbot.component.onebot.v11.message.segment.OneBotText
import love.forte.simbot.component.onebot.v11.message.segment.toElement
import love.forte.simbot.message.At
import love.forte.simbot.message.AtAll
import love.forte.simbot.message.Face
import love.forte.simbot.message.Image
import love.forte.simbot.message.Message
import love.forte.simbot.message.OfflineImage
import love.forte.simbot.message.RemoteImage
import love.forte.simbot.message.Text
import kotlin.jvm.JvmName

/**
 * 将事件中接收到的 [OneBotMessageSegment] 解析为 [Message.Element]。
 *
 * | 原类型 | 转化目标 |
 * | ---- | ---- |
 * | [OneBotAt] | [At] 或 [AtAll] |
 * | [OneBotFace] | [Face] |
 * | 其他 | 使用 [toElement] 转化 |
 */
@InternalSimbotAPI
public fun OneBotMessageSegment.resolveToMessageElement(): Message.Element {
    return when (this) {
        is OneBotAt -> if (isAll) AtAll else At(data.qq.ID)
        is OneBotFace -> Face(data.id)
        else -> toElement()
    }
}

/**
 * 将一个 [Message.Element] 转化为用于API请求的 [OneBotMessageSegment]。
 */
@InternalSimbotAPI
public fun Message.Element.resolveToOneBotSegment(): OneBotMessageSegment? {
    return when (this) {
        is OneBotMessageSegmentElement -> segment
        // stand messages
        is Text -> OneBotText.create(text)
        is Face -> OneBotFace.create(id)
        is At -> OneBotAt.create(target)
        is AtAll -> OneBotAt.createAtAll()
        is Image -> {
            when (this) {
                // offline image
                is OfflineImage -> TODO()
                // remote images
                is RemoteImage -> TODO()

                else -> null
            }
        }


        else -> null
    }
}
