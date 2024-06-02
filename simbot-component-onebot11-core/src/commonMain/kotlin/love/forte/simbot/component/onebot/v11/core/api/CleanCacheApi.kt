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
import kotlin.String
import kotlin.Unit
import kotlin.jvm.JvmStatic
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.builtins.serializer

/**
 * [`clean_cache`-清理缓存](https://github.com/botuniverse/onebot-11/blob/master/api/public.md#clean_cache-清理缓存)
 *
 * @author ForteScarlet
 */
public class CleanCacheApi private constructor() : OneBotApi<Unit> {
    override val action: String
        get() = ACTION

    override val resultDeserializer: DeserializationStrategy<Unit>
        get() = Unit.serializer()

    override val apiResultDeserializer: DeserializationStrategy<OneBotApiResult<Unit>>
        get() = OneBotApiResult.emptySerializer()

    override val body: Any?
        get() = null

    public companion object Factory {
        private const val ACTION: String = "clean_cache"

        private val INSTANCE: CleanCacheApi = CleanCacheApi()

        /**
         * 构建一个 [CleanCacheApi].
         */
        @JvmStatic
        public fun create(): CleanCacheApi = INSTANCE
    }
}
