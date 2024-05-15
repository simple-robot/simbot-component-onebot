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

package love.forte.simbot.component.onebot.v11.core.api

import io.ktor.http.*

/**
 * 与 OneBot 的 Api 相关的异常。
 */
public open class OneBotApiException : RuntimeException {
    public constructor() : super()
    public constructor(message: String?) : super(message)
    public constructor(message: String?, cause: Throwable?) : super(message, cause)
    public constructor(cause: Throwable?) : super(cause)
}

/**
 * OneBotApi 请求的响应结果状态码不是成功状态 (即不是2xx)
 */
@Suppress("MemberVisibilityCanBePrivate")
public open class OneBotApiResponseNotSuccessException(
    public val status: HttpStatusCode,
    message: String? = "status: $status", cause: Throwable? = null,
) : OneBotApiException(message, cause)
