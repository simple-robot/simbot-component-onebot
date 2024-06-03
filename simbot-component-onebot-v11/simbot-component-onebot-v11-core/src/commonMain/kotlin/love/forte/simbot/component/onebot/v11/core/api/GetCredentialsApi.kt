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
import kotlin.jvm.JvmOverloads
import kotlin.jvm.JvmStatic
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import love.forte.simbot.component.onebot.v11.common.api.ApiResultConstructor

/**
 * [`get_credentials`-获取 QQ 相关接口凭证](https://github.com/botuniverse/onebot-11/blob/master/api/public.md#get_credentials-获取-qq-相关接口凭证)
 *
 * @author ForteScarlet
 */
public class GetCredentialsApi private constructor(
    override val body: Any,
) : OneBotApi<GetCredentialsResult> {
    override val action: String
        get() = ACTION

    override val resultDeserializer: DeserializationStrategy<GetCredentialsResult>
        get() = GetCredentialsResult.serializer()

    override val apiResultDeserializer:
        DeserializationStrategy<OneBotApiResult<GetCredentialsResult>>
        get() = RES_SER

    public companion object Factory {
        private const val ACTION: String = "get_credentials"

        private val RES_SER: KSerializer<OneBotApiResult<GetCredentialsResult>> =
            OneBotApiResult.serializer(GetCredentialsResult.serializer())

        /**
         * 构建一个 [GetCredentialsApi].
         *
         * @param domain 需要获取 cookies 的域名
         */
        @JvmStatic
        @JvmOverloads
        public fun create(domain: String? = null): GetCredentialsApi =
            GetCredentialsApi(Body(domain))
    }

    /**
     * @property domain 需要获取 cookies 的域名
     */
    @Serializable
    internal data class Body(
        internal val domain: String? = null,
    )
}

/**
 * [GetCredentialsApi] 的响应体。
 *
 * @property cookies Cookies
 * @property csrfToken CSRF Token
 */
@Serializable
public data class GetCredentialsResult @ApiResultConstructor constructor(
    public val cookies: String,
    @SerialName("csrf_token")
    public val csrfToken: Int,
)
