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
import love.forte.simbot.common.id.ID
import love.forte.simbot.common.id.LongID
import love.forte.simbot.component.onebot.v11.core.api.OneBotApi
import love.forte.simbot.component.onebot.v11.core.api.OneBotApiResult
import kotlin.jvm.JvmStatic

/**
 * [`forward_group_single_msg`-转发单条消息到群](https://llonebot.github.io/zh-CN/develop/extends_api)
 *
 * @author kuku
 */
public class ForwardGroupSingleMsgApi private constructor(
    override val body: Any,
): OneBotApi<Unit> {
    override val action: String
        get() = ACTION

    override val resultDeserializer: DeserializationStrategy<Unit>
        get() = Unit.serializer()

    override val apiResultDeserializer: DeserializationStrategy<OneBotApiResult<Unit>>
        get() = OneBotApiResult.emptySerializer()


    public companion object Factory {
        private const val ACTION: String = "forward_group_single_msg"

        /**
         * 构建一个 [ForwardGroupSingleMsgApi].
         *
         * @param groupId 群号
         * @param messageId 消息id
         */
        @JvmStatic
        public fun create(groupId: LongID, messageId: ID): ForwardGroupSingleMsgApi =
            ForwardGroupSingleMsgApi(Body(groupId, messageId))

    }

    /**
     * @property groupId 群号
     * @property messageId 消息id
     */
    @Serializable
    internal data class Body(
        @SerialName("group_id")
        internal val groupId: LongID,
        @SerialName("message_id")
        internal val messageId: ID
    )
}

