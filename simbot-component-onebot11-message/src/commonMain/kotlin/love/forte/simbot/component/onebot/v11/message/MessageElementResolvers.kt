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

import kotlinx.coroutines.suspendCancellableCoroutine
import love.forte.simbot.annotations.InternalSimbotAPI
import love.forte.simbot.common.id.StringID.Companion.ID
import love.forte.simbot.common.id.literal
import love.forte.simbot.component.onebot.v11.message.segment.OneBotAt
import love.forte.simbot.component.onebot.v11.message.segment.OneBotFace
import love.forte.simbot.component.onebot.v11.message.segment.OneBotImage
import love.forte.simbot.component.onebot.v11.message.segment.OneBotMessageSegment
import love.forte.simbot.component.onebot.v11.message.segment.OneBotMessageSegmentElement
import love.forte.simbot.component.onebot.v11.message.segment.OneBotText
import love.forte.simbot.component.onebot.v11.message.segment.toElement
import love.forte.simbot.message.At
import love.forte.simbot.message.AtAll
import love.forte.simbot.message.Face
import love.forte.simbot.message.Image
import love.forte.simbot.message.Message
import love.forte.simbot.message.Messages
import love.forte.simbot.message.OfflineImage
import love.forte.simbot.message.OfflineImageResolver.Companion.resolve
import love.forte.simbot.message.OfflineImageValueResolver
import love.forte.simbot.message.RemoteImage
import love.forte.simbot.message.Text
import love.forte.simbot.resource.Resource
import love.forte.simbot.resource.toResource
import love.forte.simbot.resource.toStringResource
import kotlin.coroutines.Continuation
import kotlin.coroutines.resume
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
 * 将 [Message] 解析为一用于API请求的 [OneBotMessageSegment] 列表。
 *
 * @see resolveToOneBotSegment
 */
@InternalSimbotAPI
public suspend fun Message.resolveToOneBotSegmentList(): List<OneBotMessageSegment> {
    return when (this) {
        is Message.Element -> resolveToOneBotSegment()
            ?.let { listOf(it) }
            ?: emptyList()

        is Messages -> mapNotNull { it.resolveToOneBotSegment() }
    }
}

/**
 * 将一个 [Message.Element] 转化为用于API请求的 [OneBotMessageSegment]。
 */
@InternalSimbotAPI
public suspend fun Message.Element.resolveToOneBotSegment(): OneBotMessageSegment? {
    return when (this) {
        // OB组件的 segment 类型，直接使用
        is OneBotMessageSegmentElement -> segment
        // stand messages
        is Text -> OneBotText.create(text)
        is Face -> OneBotFace.create(id)
        is At -> OneBotAt.create(target)
        is AtAll -> OneBotAt.createAtAll()
        is Image -> {
            when (this) {
                // offline image
                is OfflineImage -> suspendCancellableCoroutine<OneBotMessageSegment?> { continuation ->
                    offlineImageResolver().resolve(this, continuation)
                }

                // remote images, OneBot组件中实际上没有此类型的实现
                // 将它的 id 直接视为 file
                is RemoteImage -> OneBotImage.create(id.literal)

                // 其他未知类型，不管
                else -> null
            }
        }

        // 其他未知类型，不管
        else -> null
    }
}

/**
 * 解析一个 [OfflineImage] 中的内容为 [OneBotMessageSegment]。
 */
internal expect fun offlineImageResolver(): OfflineImageValueResolver<Continuation<OneBotMessageSegment?>>

/**
 * 给非JVM平台目标使用的共享代码
 */
internal fun commonOfflineImageResolver(): OfflineImageValueResolver<Continuation<OneBotMessageSegment?>> =
    object : OfflineImageValueResolver<Continuation<OneBotMessageSegment?>> {
        override fun resolveUnknown(image: OfflineImage, context: Continuation<OneBotMessageSegment?>) {
            resolveUnknown0(context)
        }

        override fun resolveByteArray(byteArray: ByteArray, context: Continuation<OneBotMessageSegment?>) {
            resolveByteArray0(byteArray, context)
        }

        override fun resolveString(string: String, context: Continuation<OneBotMessageSegment?>) {
            resolveString0(string, context)
        }

        override fun resolveUnknown(resource: Resource, context: Continuation<OneBotMessageSegment?>) {
            resolveUnknown0(context)
        }
    }

/**
 * 对平台而言未知的类型，直接得到 `null`。
 */
internal fun resolveUnknown0(context: Continuation<OneBotMessageSegment?>) {
    context.resume(null)
}

/**
 * 处理‘字符串’格式的图片文件。
 * 直接交给 [OneBotImage] 处理，
 * 应当会被视为 base64 字符串。
 */
internal fun resolveString0(string: String, context: Continuation<OneBotMessageSegment?>) {
    context.resume(
        OneBotImage.create(string.toStringResource())
    )
}

internal fun resolveByteArray0(byteArray: ByteArray, context: Continuation<OneBotMessageSegment?>) {
    context.resume(
        OneBotImage.create(
            byteArray.toResource()
        )
    )
}
