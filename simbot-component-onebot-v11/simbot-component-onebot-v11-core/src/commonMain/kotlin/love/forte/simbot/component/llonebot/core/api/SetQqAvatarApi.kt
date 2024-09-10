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

package love.forte.simbot.component.llonebot.core.api

import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.serializer
import love.forte.simbot.component.onebot.v11.core.api.OneBotApi
import love.forte.simbot.component.onebot.v11.core.api.OneBotApiResult
import kotlin.jvm.JvmStatic

/**
 * [`set_qq_avatar`-设置头像](https://llonebot.github.io/zh-CN/develop/extends_api)
 *
 * @author kuku
 */
public class SetQqAvatarApi private constructor(
    override val body: Any,
): OneBotApi<Unit>{

    override val action: String
        get() = ACTION

    override val resultDeserializer: DeserializationStrategy<Unit>
        get() = Unit.serializer()

    override val apiResultDeserializer: DeserializationStrategy<OneBotApiResult<Unit>>
        get() = OneBotApiResult.emptySerializer()


    public companion object Factory {
        private const val ACTION: String = "set_qq_avatar"

        /**
         * 构建一个 [SetQqAvatarApi].
         *
         * @param file 文件路径 支持http://, base64://, file://
         */
        @JvmStatic
        public fun create(file: String): SetQqAvatarApi =
            SetQqAvatarApi(Body(file))

    }

    /**
     * @property file 文件路径 支持http://, base64://, file://
     */
    @Serializable
    internal data class Body(
        @SerialName("file")
        internal val file: String
    )

}
