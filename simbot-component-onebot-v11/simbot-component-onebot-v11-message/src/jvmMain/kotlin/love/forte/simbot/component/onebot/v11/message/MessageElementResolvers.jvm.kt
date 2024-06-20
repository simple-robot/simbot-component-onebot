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

import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import love.forte.simbot.annotations.Api4J
import love.forte.simbot.annotations.InternalSimbotAPI
import love.forte.simbot.component.onebot.common.annotations.InternalOneBotAPI
import love.forte.simbot.component.onebot.v11.message.segment.OneBotImage
import love.forte.simbot.component.onebot.v11.message.segment.OneBotMessageSegment
import love.forte.simbot.message.JvmOfflineImageValueResolver
import love.forte.simbot.message.Message
import love.forte.simbot.message.OfflineImage
import love.forte.simbot.message.OfflineImageValueResolver
import love.forte.simbot.resource.Resource
import love.forte.simbot.resource.URIResource
import love.forte.simbot.resource.toResource
import love.forte.simbot.suspendrunner.reserve.SuspendReserve
import love.forte.simbot.suspendrunner.reserve.suspendReserve
import love.forte.simbot.suspendrunner.runInAsync
import love.forte.simbot.suspendrunner.runInNoScopeBlocking
import java.io.File
import java.net.URI
import java.nio.file.Path
import java.util.concurrent.CompletableFuture
import kotlin.coroutines.Continuation
import kotlin.coroutines.EmptyCoroutineContext
import kotlin.coroutines.resume

/**
 * 将 [Message] 解析为一用于API请求的 [OneBotMessageSegment] 列表。
 *
 * @see resolveToOneBotSegmentList
 */
@InternalOneBotAPI
@Api4J
public fun Message.resolveToOneBotSegmentListBlocking(): List<OneBotMessageSegment> =
    runInNoScopeBlocking { resolveToOneBotSegmentList() }

/**
 * 将 [Message] 解析为一用于API请求的 [OneBotMessageSegment] 列表。
 *
 * @see resolveToOneBotSegmentList
 */
@OptIn(InternalSimbotAPI::class)
@InternalOneBotAPI
@Api4J
public fun Message.resolveToOneBotSegmentListAsync(): CompletableFuture<out List<OneBotMessageSegment>> =
    runInAsync { resolveToOneBotSegmentList() }

/**
 * 将 [Message] 解析为一用于API请求的 [OneBotMessageSegment] 列表。
 *
 * @see resolveToOneBotSegmentList
 */
@OptIn(InternalSimbotAPI::class, DelicateCoroutinesApi::class)
@InternalOneBotAPI
@Api4J
public fun Message.resolveToOneBotSegmentListReserve(): SuspendReserve<List<OneBotMessageSegment>> =
    suspendReserve(
        GlobalScope,
        EmptyCoroutineContext
    ) { resolveToOneBotSegmentList() }

/**
 * 将一个 [Message.Element] 转化为用于API请求的 [OneBotMessageSegment]。
 * @see resolveToOneBotSegment
 */
@InternalOneBotAPI
@Api4J
public fun Message.Element.resolveToOneBotSegmentBlocking(): OneBotMessageSegment? =
    runInNoScopeBlocking { resolveToOneBotSegment() }

/**
 * 将一个 [Message.Element] 转化为用于API请求的 [OneBotMessageSegment]。
 * @see resolveToOneBotSegment
 */
@OptIn(InternalSimbotAPI::class)
@InternalOneBotAPI
@Api4J
public fun Message.Element.resolveToOneBotSegmentAsync(): CompletableFuture<out OneBotMessageSegment?> =
    runInAsync { resolveToOneBotSegment() }

/**
 * 将一个 [Message.Element] 转化为用于API请求的 [OneBotMessageSegment]。
 * @see resolveToOneBotSegment
 */
@OptIn(DelicateCoroutinesApi::class, InternalSimbotAPI::class)
@InternalOneBotAPI
@Api4J
public fun Message.Element.resolveToOneBotSegmentReserve(): SuspendReserve<OneBotMessageSegment?> =
    suspendReserve(
        GlobalScope,
        EmptyCoroutineContext
    ) { resolveToOneBotSegment() }


internal actual fun offlineImageResolver(): OfflineImageValueResolver<Continuation<OneBotMessageSegment?>> =
    object : JvmOfflineImageValueResolver<Continuation<OneBotMessageSegment?>>() {
        override fun resolveUnknownInternal(image: OfflineImage, context: Continuation<OneBotMessageSegment?>) {
            resolveUnknown0(context)
        }

        override fun resolveByteArray(byteArray: ByteArray, context: Continuation<OneBotMessageSegment?>) {
            resolveByteArray0(byteArray, context)
        }

        /**
         * 文件类型，转为 Resource，[OneBotImage] 会进行处理，转为文件的路径地址。
         * `file:xxx`
         */
        override fun resolveFile(file: File, context: Continuation<OneBotMessageSegment?>) {
            context.resume(
                OneBotImage.create(
                    file.toResource()
                )
            )
        }

        /**
         * 文件类型，转为 Resource，[OneBotImage] 会进行处理，转为文件的路径地址。
         * `file:xxx`
         */
        override fun resolvePath(path: Path, context: Continuation<OneBotMessageSegment?>) {
            context.resume(
                OneBotImage.create(
                    path.toResource()
                )
            )
        }

        override fun resolveString(string: String, context: Continuation<OneBotMessageSegment?>) {
            resolveString0(string, context)
        }

        /**
         * 一个不是本地文件的 [URI],
         * 则视其为一个网络链接。
         * 直接提供 [URIResource], [OneBotImage]
         * 会进行处理，直接使用它的链接地址。
         */
        override fun resolveURINotFileScheme(uri: URI, context: Continuation<OneBotMessageSegment?>) {
            context.resume(
                OneBotImage.create(
                    uri.toResource()
                )
            )
        }

        override fun resolveUnknownInternal(resource: Resource, context: Continuation<OneBotMessageSegment?>) {
            resolveUnknown0(context)
        }
    }
