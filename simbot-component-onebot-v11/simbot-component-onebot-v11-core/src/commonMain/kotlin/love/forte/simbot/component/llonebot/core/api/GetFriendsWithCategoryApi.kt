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
import kotlinx.serialization.builtins.ListSerializer
import love.forte.simbot.common.id.LongID
import love.forte.simbot.component.onebot.common.annotations.ApiResultConstructor
import love.forte.simbot.component.onebot.v11.core.api.OneBotApi
import love.forte.simbot.component.onebot.v11.core.api.OneBotApiResult
import kotlin.jvm.JvmStatic

/**
 * [`get_friends_with_category`-获取带分组信息好友列表](https://llonebot.github.io/zh-CN/develop/extends_api)
 *
 * @author kuku
 */
public class GetFriendsWithCategoryApi private constructor(): OneBotApi<List<GetFriendsWithCategoryResult>> {

    override val action: String
        get() = ACTION

    override val resultDeserializer: DeserializationStrategy<List<GetFriendsWithCategoryResult>>
        get() = SER

    override val apiResultDeserializer: DeserializationStrategy<OneBotApiResult<List<GetFriendsWithCategoryResult>>>
        get() = RES_SER

    override val body: Any?
        get() = null

    public companion object Factory {
        private const val ACTION: String = "get_friends_with_category"

        private val SER = ListSerializer(GetFriendsWithCategoryResult.serializer())

        private val RES_SER = OneBotApiResult.serializer(SER)

        private val INSTANCE: GetFriendsWithCategoryApi = GetFriendsWithCategoryApi()

        /**
         * 构建一个 [GetFriendsWithCategoryApi].
         */
        @JvmStatic
        public fun create(): GetFriendsWithCategoryApi = INSTANCE
    }

}

/**
 * [GetFriendsWithCategoryApi] 的响应体。
 *
 */
@Serializable
public data class GetFriendsWithCategoryResult @ApiResultConstructor constructor(
    val qid: String,
    val longNick: String,
    @SerialName("birthday_year")
    val birthdayYear: Int,
    @SerialName("birthday_month")
    val birthdayMonth: Int,
    @SerialName("birthday_day")
    val birthdayDay: Int,
    val age: Int,
    val sex: String,
    val eMail: String,
    val phoneNum: String,
    val categoryId: Int,
    val richTime: Long,
    val uid: String,
    val uin: String,
    val nick: String,
    val remark: String,
    @SerialName("user_id")
    val userId: LongID,
    val nickname: String,
    val level: Int,
    @SerialName("categroyName")
    val categoryName: String
)
