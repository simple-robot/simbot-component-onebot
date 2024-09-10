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
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.serializer
import love.forte.simbot.common.id.ID
import love.forte.simbot.component.onebot.v11.core.api.OneBotApi
import love.forte.simbot.component.onebot.v11.core.api.OneBotApiResult
import kotlin.jvm.JvmStatic

/**
 * [`create_group_file_folder`-创建群文件文件夹](https://docs.go-cqhttp.org/api/#创建群文件文件夹)
 *
 * @author kuku
 */
public class CreateGroupFileFolderApi private constructor(
    override val body: Any,
): OneBotApi<Unit> {

    override val action: String
        get() = ACTION

    override val resultDeserializer: DeserializationStrategy<Unit>
        get() = Unit.serializer()

    override val apiResultDeserializer: DeserializationStrategy<OneBotApiResult<Unit>>
        get() = OneBotApiResult.emptySerializer()


    public companion object Factory {
        private const val ACTION: String = "create_group_file_folder"

        /**
         * 构建一个 [CreateGroupFileFolderApi].
         *
         * @param groupId 群号
         * @param name 文件夹名称
         */
        @JvmStatic
        public fun create(groupId: ID, name: String): CreateGroupFileFolderApi =
            CreateGroupFileFolderApi(Body(groupId, name))

    }

    /**
     * @property groupId 群号
     * @property name 文件夹名称
     * @property parentId 仅能为 /
     */
    @Serializable
    internal data class Body(
        @SerialName("group_id")
        internal val groupId: ID,
        @SerialName("name")
        internal val name: String,
        @SerialName("parent_id")
        internal val parentId: String = "/"
    )

}
