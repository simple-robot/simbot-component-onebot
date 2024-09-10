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
import love.forte.simbot.common.id.LongID
import love.forte.simbot.component.onebot.common.annotations.ApiResultConstructor
import love.forte.simbot.component.onebot.v11.core.api.OneBotApi
import love.forte.simbot.component.onebot.v11.core.api.OneBotApiResult
import kotlin.jvm.JvmStatic

/**
 * [`get_group_root_files`-获取群根目录文件列表](https://docs.go-cqhttp.org/api/#获取群根目录文件列表)
 *
 * @author kuku
 */
public class GetGroupRootFilesApi private constructor(
    override val body: Any,
): OneBotApi<GetGroupRootFilesResult> {

    override val action: String
        get() = ACTION

    override val resultDeserializer: DeserializationStrategy<GetGroupRootFilesResult>
        get() = GetGroupRootFilesResult.serializer()

    override val apiResultDeserializer: DeserializationStrategy<OneBotApiResult<GetGroupRootFilesResult>>
        get() = RES_SER

    public companion object Factory {
        private const val ACTION: String = "get_group_root_files"

        private val RES_SER: KSerializer<OneBotApiResult<GetGroupRootFilesResult>> =
            OneBotApiResult.serializer(GetGroupRootFilesResult.serializer())

        /**
         * 构建一个 [GetGroupRootFilesApi].
         *
         * @param groupId 群号
         */
        @JvmStatic
        public fun create(groupId: ID): GetGroupRootFilesApi =
            GetGroupRootFilesApi(Body(groupId))
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
 * [GetGroupRootFilesApi] 的响应体。
 *
 * @property files 文件列表
 * @property folders 文件夹列表
 */
@Serializable
public data class GetGroupRootFilesResult @ApiResultConstructor constructor(
    val files: List<GoCqHttpFile> = emptyList(),
    val folders: List<GoCqHttpFolder> = emptyList()
)

/**
 * go-cqhttp 文件信息
 *
 * @property groupId 群号
 * @property fileId 文件ID
 * @property fileName 文件名
 * @property busid 文件类型
 * @property fileSize 文件大小
 * @property uploadTime 上传时间
 * @property deadTime 过期时间,永久文件恒为0
 * @property modifyTime 最后修改时间
 * @property downloadTimes 下载次数
 * @property uploader 上传者ID
 * @property uploaderName 上传者名字
 */
@Serializable
public data class GoCqHttpFile(
    @SerialName("group_id")
    val groupId: LongID,
    @SerialName("file_id")
    val fileId: String,
    @SerialName("file_name")
    val fileName: String,
    @SerialName("busid")
    val busid: Int,
    @SerialName("file_size")
    val fileSize: Long,
    @SerialName("upload_time")
    val uploadTime: Long,
    @SerialName("dead_time")
    val deadTime: Long,
    @SerialName("modify_time")
    val modifyTime: Long,
    @SerialName("download_times")
    val downloadTimes: Int,
    @SerialName("uploader")
    val uploader: LongID,
    @SerialName("uploader_name")
    val uploaderName: String
)

/**
 * go-cqhttp 文件夹信息
 *
 * @property groupId 群号
 * @property folderId 文件夹ID
 * @property folderName 文件名
 * @property createTime 创建时间
 * @property creator 创建者
 * @property creatorName 创建者名字
 * @property totalFileCount 子文件数量
 */
@Serializable
public data class GoCqHttpFolder(
    @SerialName("group_id")
    val groupId: LongID,
    @SerialName("folder_id")
    val folderId: String,
    @SerialName("folder_name")
    val folderName: String,
    @SerialName("create_time")
    val createTime: Long,
    @SerialName("creator")
    val creator: LongID,
    @SerialName("creator_name")
    val creatorName: String,
    @SerialName("total_file_count")
    val totalFileCount: Int
)
