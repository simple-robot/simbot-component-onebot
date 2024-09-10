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
import kotlin.jvm.JvmOverloads
import kotlin.jvm.JvmStatic

/**
 * [`upload_group_file`-上传群文件](https://docs.go-cqhttp.org/api/#上传群文件)
 *
 * @author kuku
 */
public class UploadGroupFileApi private constructor(
    override val body: Any,
) : OneBotApi<Unit> {
    override val action: String
        get() = ACTION

    override val resultDeserializer: DeserializationStrategy<Unit>
        get() = Unit.serializer()

    override val apiResultDeserializer: DeserializationStrategy<OneBotApiResult<Unit>>
        get() = OneBotApiResult.emptySerializer()


    public companion object Factory {
        private const val ACTION: String = "upload_group_file"

        /**
         * 构建一个 [UploadGroupFileApi].
         *
         * @param groupId 群号
         * @param file 本地文件路径
         * @param name 储存名称
         * @param folder 父目录ID
         */
        @JvmStatic
        @JvmOverloads
        public fun create(groupId: ID, file: String, name: String, folder: String? = null): UploadGroupFileApi =
            UploadGroupFileApi(Body(groupId, file, name, folder))

    }

    /**
     * @property groupId 群号
     * @property file 本地文件路径
     * @property name 储存名称
     * @property folder 父目录ID
     */
    @Serializable
    internal data class Body(
        @SerialName("group_id")
        internal val groupId: ID,
        @SerialName("file")
        internal val file: String,
        @SerialName("name")
        internal val name: String,
        @SerialName("folder")
        internal val folder: String? = null
    )

}
