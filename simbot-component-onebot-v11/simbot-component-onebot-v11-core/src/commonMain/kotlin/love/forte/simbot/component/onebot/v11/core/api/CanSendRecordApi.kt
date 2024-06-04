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
import kotlin.Boolean
import kotlin.String
import kotlin.jvm.JvmStatic
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import love.forte.simbot.component.onebot.common.annotations.ApiResultConstructor

/**
 * [`can_send_record`-检查是否可以发送语音](https://github.com/botuniverse/onebot-11/blob/master/api/public.md#can_send_record-检查是否可以发送语音)
 *
 * @author ForteScarlet
 */
public class CanSendRecordApi private constructor() : OneBotApi<CanSendRecordResult> {
    override val action: String
        get() = ACTION

    override val resultDeserializer: DeserializationStrategy<CanSendRecordResult>
        get() = CanSendRecordResult.serializer()

    override val apiResultDeserializer:
        DeserializationStrategy<OneBotApiResult<CanSendRecordResult>>
        get() = RES_SER

    override val body: Any?
        get() = null

    public companion object Factory {
        private const val ACTION: String = "can_send_record"

        private val RES_SER: KSerializer<OneBotApiResult<CanSendRecordResult>> =
            OneBotApiResult.serializer(CanSendRecordResult.serializer())

        private val INSTANCE: CanSendRecordApi = CanSendRecordApi()

        /**
         * 构建一个 [CanSendRecordApi].
         */
        @JvmStatic
        public fun create(): CanSendRecordApi = INSTANCE
    }
}

/**
 * [CanSendRecordApi] 的响应体。
 *
 * @property yes 是或否
 */
@Serializable
public data class CanSendRecordResult @ApiResultConstructor constructor(
    public val yes: Boolean,
)
