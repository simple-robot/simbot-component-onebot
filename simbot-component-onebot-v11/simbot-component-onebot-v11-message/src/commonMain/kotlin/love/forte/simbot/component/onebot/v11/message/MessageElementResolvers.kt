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
import love.forte.simbot.common.id.StringID.Companion.ID
import love.forte.simbot.common.id.literal
import love.forte.simbot.component.onebot.common.annotations.InternalOneBotAPI
import love.forte.simbot.component.onebot.v11.message.segment.*
import love.forte.simbot.message.*
import love.forte.simbot.message.OfflineImageResolver.Companion.resolve
import love.forte.simbot.resource.Resource
import love.forte.simbot.resource.toResource
import love.forte.simbot.resource.toStringResource
import kotlin.coroutines.Continuation
import kotlin.coroutines.resume
import kotlin.jvm.JvmName
import kotlin.jvm.JvmSynthetic

/**
 * 将事件中接收到的 [OneBotMessageSegment] 解析为 [Message.Element]。
 *
 * | 原类型 | 转化目标 |
 * | ---- | ---- |
 * | [OneBotAt] | [At] 或 [AtAll] |
 * | [OneBotFace] | [Face] |
 * | 其他 | 使用 [toElement] 转化 |
 */
@InternalOneBotAPI
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
@InternalOneBotAPI
@JvmSynthetic
public suspend fun Message.resolveToOneBotSegmentList(
    defaultImageAdditionalParams: ((Resource) -> OneBotImage.AdditionalParams?)? = null,
): List<OneBotMessageSegment> {
    return when (this) {
        is Message.Element -> resolveToOneBotSegment(defaultImageAdditionalParams)
            ?.let { listOf(it) }
            ?: emptyList()

        is Messages -> mapNotNull { it.resolveToOneBotSegment(defaultImageAdditionalParams) }
    }
}

/**
 * 将一个 [Message.Element] 转化为用于API请求的 [OneBotMessageSegment]。
 */
@InternalOneBotAPI
@JvmSynthetic
public suspend fun Message.Element.resolveToOneBotSegment(
    defaultImageAdditionalParams: ((Resource) -> OneBotImage.AdditionalParams?)? = null,
): OneBotMessageSegment? {
    return when (this) {
        // OB组件的 segment 类型，直接使用
        is OneBotMessageSegmentElement -> segment
        // stand messages
        is Text -> OneBotText.create(text)
        is Face -> OneBotFace.create(id)
        is At -> OneBotAt.create(target)
        is AtAll -> OneBotAt.createAtAll()
        is Image -> when (this) {
            // offline image
            is OfflineImage -> suspendCancellableCoroutine<OneBotMessageSegment?> { continuation ->
                offlineImageResolver(defaultImageAdditionalParams)
                    .resolve(this, continuation)
            }

            // remote images, OneBot组件中实际上没有此类型的实现
            // 将它的 id 直接视为 file
            is RemoteImage -> OneBotImage.create(id.literal)

            // 其他未知类型，不管
            else -> null
        }

        // since 1.2.0
        is MessageReference -> OneBotReply.create(id)

        // 其他未知类型，不管
        else -> null
    }
}

/**
 * 解析一个 [OfflineImage] 中的内容为 [OneBotMessageSegment]。
 */
internal expect fun offlineImageResolver(
    defaultImageAdditionalParams: ((Resource) -> OneBotImage.AdditionalParams?)?,
): OfflineImageValueResolver<Continuation<OneBotMessageSegment?>>

/**
 * 给非JVM平台目标使用的共享代码
 */
internal fun commonOfflineImageResolver(
    defaultImageAdditionalParams: ((Resource) -> OneBotImage.AdditionalParams?)?,
): OfflineImageValueResolver<Continuation<OneBotMessageSegment?>> =
    object : OfflineImageValueResolver<Continuation<OneBotMessageSegment?>> {
        override fun resolveUnknown(image: OfflineImage, context: Continuation<OneBotMessageSegment?>) {
            resolveUnknown0(context)
        }

        override fun resolveByteArray(byteArray: ByteArray, context: Continuation<OneBotMessageSegment?>) {
            resolveByteArray0(defaultImageAdditionalParams, byteArray, context)
        }

        override fun resolveString(string: String, context: Continuation<OneBotMessageSegment?>) {
            resolveString0(defaultImageAdditionalParams, string, context)
        }

        override fun resolveUnknown(resource: Resource, context: Continuation<OneBotMessageSegment?>) {
            resolveByteArray(resource.data(), context)
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
internal fun resolveString0(
    defaultImageAdditionalParams: ((Resource) -> OneBotImage.AdditionalParams?)?,
    string: String,
    context: Continuation<OneBotMessageSegment?>
) {
    val resource = string.toStringResource()
    val additional = defaultImageAdditionalParams?.invoke(resource)
    context.resume(OneBotImage.create(resource, additional))
}

internal fun resolveByteArray0(
    defaultImageAdditionalParams: ((Resource) -> OneBotImage.AdditionalParams?)?,
    byteArray: ByteArray,
    context: Continuation<OneBotMessageSegment?>
) {
    val resource = byteArray.toResource()
    val additional = defaultImageAdditionalParams?.invoke(resource)
    context.resume(OneBotImage.create(resource, additional))
}
