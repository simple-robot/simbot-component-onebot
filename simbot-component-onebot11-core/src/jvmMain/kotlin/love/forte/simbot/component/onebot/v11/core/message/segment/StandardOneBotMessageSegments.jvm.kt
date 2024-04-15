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

import love.forte.simbot.resource.*
import java.net.URI
import java.nio.file.Path
import kotlin.io.path.Path
import kotlin.io.path.absolutePathString
import kotlin.io.path.readBytes
import kotlin.io.path.toPath

internal actual fun resolveResourceToFileValuePlatform(
    resource: Resource,
    localFileToBase64: Boolean,
): String? {
    fun Path.resolvePath(): String {
        return if (localFileToBase64) {
            computeBase64FileValue(readBytes())
        } else {
            absolutePathString()
        }
    }

    return when (resource) {
        is URIResource -> {
            val uri = resource.uri
            when (uri.scheme) {
                "file" -> uri.toPath().resolvePath()
                "base64" -> uri.toASCIIString().substring(9) // "base64://"
                else -> uri.toASCIIString()
            }
        }

        is PathResource -> resource.path.resolvePath()
        is FileResource -> {
            val file = resource.file
            if (localFileToBase64) {
                computeBase64FileValue(file.readBytes())
            } else {
                file.absolutePath
            }
        }

        else -> null
    }
}


internal actual fun uriResource(uri: String): Resource {
    return URI.create(uri).toResource()
}

internal actual fun pathResource(path: String): Resource {
    return Path(path).toResource()
}
