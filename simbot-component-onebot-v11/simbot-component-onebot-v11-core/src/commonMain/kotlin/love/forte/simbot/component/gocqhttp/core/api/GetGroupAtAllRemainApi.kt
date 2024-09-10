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

package love.forte.simbot.component.gocqhttp.core.api

import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import love.forte.simbot.common.id.ID
import love.forte.simbot.component.onebot.common.annotations.ApiResultConstructor
import love.forte.simbot.component.onebot.v11.core.api.OneBotApi
import love.forte.simbot.component.onebot.v11.core.api.OneBotApiResult
import kotlin.jvm.JvmStatic

/**
 * [`get_group_at_all_remain`-获取群 @全体成员 剩余次数](https://docs.go-cqhttp.org/api/#获取群-全体成员-剩余次数)
 *
 * @author kuku
 */
public class GetGroupAtAllRemainApi private constructor(
    override val body: Any,
): OneBotApi<GetGroupAtAllRemainResult>{

    override val action: String
        get() = ACTION

    override val resultDeserializer: DeserializationStrategy<GetGroupAtAllRemainResult>
        get() = GetGroupAtAllRemainResult.serializer()

    override val apiResultDeserializer: DeserializationStrategy<OneBotApiResult<GetGroupAtAllRemainResult>>
        get() = RES_SER

    public companion object Factory {
        private const val ACTION: String = "get_group_at_all_remain"

        private val RES_SER: KSerializer<OneBotApiResult<GetGroupAtAllRemainResult>> =
            OneBotApiResult.serializer(GetGroupAtAllRemainResult.serializer())

        /**
         * 构建一个 [GetGroupAtAllRemainApi].
         *
         * @param groupId 群号
         */
        @JvmStatic
        public fun create(groupId: ID): GetGroupAtAllRemainApi =
            GetGroupAtAllRemainApi(Body(groupId))
    }

    /**
     * @property groupId 群号
     */
    @Serializable
    internal data class Body(
        @SerialName("group_id")
        internal val groupId: ID
    )

}

/**
 * [GetGroupAtAllRemainApi] 的响应体。
 *
 * @property canAtAll 是否可以 @全体成员
 * @property remainAtAllCountForGroup 群内所有管理当天剩余 @全体成员 次数
 * @property remainAtAllCountForUin Bot 当天剩余 @全体成员 次数
 */
@Serializable
public data class GetGroupAtAllRemainResult @ApiResultConstructor constructor(
    @SerialName("can_at_all")
    val canAtAll: Boolean,
    @SerialName("remain_at_all_count_for_group")
    val remainAtAllCountForGroup: Int,
    @SerialName("remain_at_all_count_for_uin")
    val remainAtAllCountForUin: Int
)
