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

import love.forte.simbot.resource.ByteArrayResource
import love.forte.simbot.resource.Resource
import love.forte.simbot.resource.toResource
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi


internal fun resolveUrlOrFileToResource(url: String?, file: String): Resource {
    return if (url != null) {
        uriResource(url)
    } else {
        when {
            file.startsWith("file://") -> {
                pathResource(file.substring(7))
            }

            file.startsWith("base64://") -> {
                base64Resource(file.substring(9))
            }

            else -> {
                uriResource(file)
            }
        }
    }
}

internal expect fun uriResource(uri: String): Resource
internal expect fun pathResource(path: String): Resource

@OptIn(ExperimentalEncodingApi::class)
internal fun base64Resource(data: String): Resource {
    return Base64.UrlSafe.decode(data).toResource()
}

internal expect fun resolveResourceToFileValuePlatform(
    resource: Resource,
    localFileToBase64: Boolean,
): String?

internal fun resolveResourceToFileValue(
    resource: Resource,
    localFileToBase64: Boolean,
): String {
    return when (resource) {
        is ByteArrayResource -> computeBase64FileValue(resource.data())
        else -> {
            resolveResourceToFileValuePlatform(resource, localFileToBase64)
                ?: computeBase64FileValue(resource.data())
        }
    }
}

@OptIn(ExperimentalEncodingApi::class)
internal fun computeBase64FileValue(data: ByteArray): String {
    return buildString {
        append("base64://")
        Base64.UrlSafe.encodeToAppendable(data, this)
    }
}
