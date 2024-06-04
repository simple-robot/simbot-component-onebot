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

import kotlin.Any
import kotlin.Int
import kotlin.String
import kotlin.jvm.JvmStatic
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import love.forte.simbot.component.onebot.common.annotations.ApiResultConstructor

/**
 * [`get_csrf_token`-获取 CSRF Token](https://github.com/botuniverse/onebot-11/blob/master/api/public.md#get_csrf_token-获取-csrf-token)
 *
 * @author ForteScarlet
 */
public class GetCsrfTokenApi private constructor() : OneBotApi<GetCsrfTokenResult> {
    override val action: String
        get() = ACTION

    override val resultDeserializer: DeserializationStrategy<GetCsrfTokenResult>
        get() = GetCsrfTokenResult.serializer()

    override val apiResultDeserializer: DeserializationStrategy<OneBotApiResult<GetCsrfTokenResult>>
        get() = RES_SER

    override val body: Any?
        get() = null

    public companion object Factory {
        private const val ACTION: String = "get_csrf_token"

        private val RES_SER: KSerializer<OneBotApiResult<GetCsrfTokenResult>> =
            OneBotApiResult.serializer(GetCsrfTokenResult.serializer())

        private val INSTANCE: GetCsrfTokenApi = GetCsrfTokenApi()

        /**
         * 构建一个 [GetCsrfTokenApi].
         */
        @JvmStatic
        public fun create(): GetCsrfTokenApi = INSTANCE
    }
}

/**
 * [GetCsrfTokenApi] 的响应体。
 *
 * @property token CSRF Token
 */
@Serializable
public data class GetCsrfTokenResult @ApiResultConstructor constructor(
    public val token: Int,
)
