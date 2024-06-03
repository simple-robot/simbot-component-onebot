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

import love.forte.simbot.component.onebot.v11.message.segment.OneBotImage
import love.forte.simbot.component.onebot.v11.message.segment.OneBotMessageSegment
import love.forte.simbot.message.JvmOfflineImageValueResolver
import love.forte.simbot.message.OfflineImage
import love.forte.simbot.message.OfflineImageValueResolver
import love.forte.simbot.resource.Resource
import love.forte.simbot.resource.URIResource
import love.forte.simbot.resource.toResource
import java.io.File
import java.net.URI
import java.nio.file.Path
import kotlin.coroutines.Continuation
import kotlin.coroutines.resume

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
