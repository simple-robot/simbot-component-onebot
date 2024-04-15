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

package love.forte.simbot.component.onebot.v11.core.message.segment

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import love.forte.simbot.common.id.ID
import love.forte.simbot.common.id.StringID.Companion.ID
import love.forte.simbot.common.id.literal
import love.forte.simbot.message.*
import love.forte.simbot.resource.ByteArrayResource
import love.forte.simbot.resource.Resource
import love.forte.simbot.resource.toResource
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi
import kotlin.jvm.JvmOverloads
import kotlin.jvm.JvmStatic

/**
 * [纯文本](https://github.com/botuniverse/onebot-11/blob/master/message/segment.md#%E7%BA%AF%E6%96%87%E6%9C%AC)
 *
 * 可用于发送，与直接使用 [Text] 无区别。
 * 在接收消息时不会被使用，而是始终被解析为 [Text]。
 *
 * @author ForteScarlet
 */
@Serializable
@SerialName(OneBotText.TYPE)
public class OneBotText private constructor(override val data: Data) :
    OneBotMessageSegment<OneBotText.Data>,
    PlainText {
    override val text: String
        get() = data.text

    public companion object Factory {
        public const val TYPE: String = "text"

        @JvmStatic
        public fun create(text: String): OneBotText =
            OneBotText(Data(text))
    }

    @Serializable
    public data class Data internal constructor(val text: String)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is OneBotText) return false

        if (data != other.data) return false

        return true
    }

    override fun hashCode(): Int {
        return data.hashCode()
    }

    override fun toString(): String {
        return "OneBotText(text=$text)"
    }
}

/**
 * [QQ 表情](https://github.com/botuniverse/onebot-11/blob/master/message/segment.md#qq-%E8%A1%A8%E6%83%85)
 *
 * 可用于发送，与直接使用 [Face] 无区别。
 * 接收时不会使用，而是始终解析为 [Face]。
 *
 * @author ForteScarlet
 */
@Serializable
@SerialName(OneBotFace.TYPE)
public class OneBotFace private constructor(override val data: Data) :
    OneBotMessageSegment<OneBotFace.Data>,
    EmoticonMessage {
    public val id: ID
        get() = data.id

    public companion object Factory {
        public const val TYPE: String = "face"

        @JvmStatic
        public fun create(id: ID): OneBotFace =
            OneBotFace(Data(id))
    }

    @Serializable
    public data class Data(val id: ID)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is OneBotFace) return false

        if (data != other.data) return false

        return true
    }

    override fun hashCode(): Int {
        return data.hashCode()
    }

    override fun toString(): String {
        return "OneBotFace(id=$id)"
    }
}

/**
 * [图片](https://github.com/botuniverse/onebot-11/blob/master/message/segment.md#%E5%9B%BE%E7%89%87)
 *
 * 可用于发送。
 *
 * @author ForteScarlet
 */
@Serializable
@SerialName(OneBotImage.TYPE)
public class OneBotImage private constructor(
    override val data: Data,
    private val resource0: Resource?,
) : OneBotMessageSegment<OneBotImage.Data>, Image {
    /**
     * 当前 [OneBotImage] 中的资源信息。
     * 如果 [Data.url] 不为 `null`(即此消息是接收到的)
     * 则 [resource] 代表接收到的资源信息，否则代表用于发送的
     * [Data.file] 的资源信息。
     */
    public val resource: Resource by lazy {
        resource0 ?: data.resolveToResource()
    }

    public companion object Factory {
        public const val TYPE: String = "image"

        private fun Data.resolveToResource(): Resource {
            return resolveUrlOrFileToResource(url, file)
        }

        /**
         * 直接使用 [Data] 和 [Resource] 构建 [OneBotImage].
         * - 如果 [Resource] 为 `null`，则会尝试解析 [data]:
         * - 如果 [Data.url] 不为 `null`，使用它（仅支持 JVM 平台）
         * - 如果 [Data.file] 是 base64 格式，解析为 [ByteArrayResource]。
         * - 如果 [Data.file] 是链接或者文件路径，
         * 解析为 `URIResource` 或 `PathResource` （仅支持 JVM 平台）
         *
         * @throws UnsupportedOperationException 如果解析 [resource] 时平台不支持
         */
        @JvmStatic
        @JvmOverloads
        public fun create(data: Data, resource: Resource? = null): OneBotImage =
            OneBotImage(data, resource)

        /**
         * 只通过 [Data.file] 属性构建 [OneBotImage].
         */
        @JvmStatic
        public fun create(file: String): OneBotImage =
            create(Data(file))

        /**
         * 直接使用 [Resource] 构建一个**用于发送**的 [OneBotImage]。
         * 其中，[Data.file] 会根据 [Resource] 的类型计算为一个对应的值，
         * 大概规则为：
         * - 如果为 (JVM平台的) `URIResource`, 且代表的是一个网络路径，则直接使用此网络路径。
         * - 如果可以解析为本地文件（例如JVM平台的 `FileResource`，`PathResource`
         * 或 `URI.scheme` == `"file"` 的 `URIResource`，
         * 则会根据 [AdditionalParams.localFileToBase64] 的与否解析为 base64
         * 或本地文件路径。
         * （注意！一些尚且不支持读取本地文件的平台，可能会直接抛出 [UnsupportedOperationException]）。
         * - 如果为可以直接读取字节数据的类型，例如 [ByteArrayResource]，
         * 则计算并使用 `base64` 格式。
         * - 其他无法判明类型的情况，尝试使用 `base64` 格式。
         *
         * @throws UnsupportedOperationException 如果解析 [resource] 时平台不支持
         */
        @JvmStatic
        @JvmOverloads
        public fun create(resource: Resource, additional: AdditionalParams? = null): OneBotImage {
            val file = resolveResourceToFileValue(
                resource,
                additional?.localFileToBase64 == true
            )

            val data = Data(
                file = file,
                type = additional?.type,
                cache = additional?.cache,
                proxy = additional?.proxy,
                timeout = additional?.timeout,
            )

            return OneBotImage(data, resource)
        }
    }

    /**
     * @property file 图片文件名
     * @property type 图片类型，`flash` 表示闪照，无此参数表示普通图片
     * @property url (receive only) 图片 URL
     * @property cache (send only) 只在通过网络 URL 发送时有效，表示是否使用已缓存的文件，默认 1
     * @property proxy (send only) 只在通过网络 URL 发送时有效，
     * 表示是否通过代理下载文件（需通过环境变量或配置文件配置代理），默认 1
     * @property timeout (send only) 只在通过网络 URL 发送时有效，单位秒，表示下载网络文件的超时时间，默认不超时
     */
    @Serializable
    public data class Data internal constructor(
        val file: String,
        val type: String? = null,
        val url: String? = null, // receive only
        val cache: Boolean? = null, // send only
        val proxy: Boolean? = null, // send only
        val timeout: Int? = null, // send only
    )

    /**
     * 用于 [OneBotImage] 提供的部分工厂函数中，
     * 指定 [Data] 中部分属性。
     */
    public class AdditionalParams {
        public var localFileToBase64: Boolean = false
        public var type: String? = null
        public var cache: Boolean? = null
        public var proxy: Boolean? = null
        public var timeout: Int? = null
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is OneBotImage) return false

        if (data != other.data) return false
        if (resource0 != other.resource0) return false

        return true
    }

    override fun hashCode(): Int {
        var result = data.hashCode()
        result = 31 * result + (resource0?.hashCode() ?: 0)
        return result
    }

    override fun toString(): String {
        return "OneBotImage(data=$data)"
    }
}


/**
 * [语音](https://github.com/botuniverse/onebot-11/blob/master/message/segment.md#%E8%AF%AD%E9%9F%B3)
 *
 * 可用于发送和接收。
 *
 * @author ForteScarlet
 */
@Serializable
@SerialName(OneBotRecord.TYPE)
public class OneBotRecord private constructor(
    override val data: Data,
    private val resource0: Resource?,
) : OneBotMessageSegment<OneBotRecord.Data> {
    /**
     * 当前 [OneBotRecord] 中的资源信息。
     * 如果 [Data.url] 不为 `null`(即此消息是接收到的)
     * 则 [resource] 代表接收到的资源信息，否则代表用于发送的
     * [Data.file] 的资源信息。
     */
    public val resource: Resource by lazy {
        resource0 ?: data.resolveToResource()
    }

    public companion object Factory {
        public const val TYPE: String = "record"

        private fun Data.resolveToResource(): Resource {
            return resolveUrlOrFileToResource(url, file)
        }

        /**
         * 直接使用 [Data] 和 [Resource] 构建 [OneBotRecord].
         * - 如果 [Resource] 为 `null`，则会尝试解析 [data]:
         * - 如果 [Data.url] 不为 `null`，使用它（仅支持 JVM 平台）
         * - 如果 [Data.file] 是 base64 格式，解析为 [ByteArrayResource]。
         * - 如果 [Data.file] 是链接或者文件路径，
         * 解析为 `URIResource` 或 `PathResource` （仅支持 JVM 平台）
         *
         * @throws UnsupportedOperationException 如果解析 [resource] 时平台不支持
         */
        @JvmStatic
        @JvmOverloads
        public fun create(data: Data, resource: Resource? = null): OneBotRecord =
            OneBotRecord(data, resource)

        /**
         * 只通过 [Data.file] 属性构建 [OneBotRecord].
         */
        @JvmStatic
        public fun create(file: String): OneBotRecord =
            create(Data(file))

        /**
         * 直接使用 [Resource] 构建一个**用于发送**的 [OneBotRecord]。
         * 其中，[Data.file] 会根据 [Resource] 的类型计算为一个对应的值，
         * 大概规则为：
         * - 如果为 (JVM平台的) `URIResource`, 且代表的是一个网络路径，则直接使用此网络路径。
         * - 如果可以解析为本地文件（例如JVM平台的 `FileResource`，`PathResource`
         * 或 `URI.scheme` == `"file"` 的 `URIResource`，
         * 则会根据 [AdditionalParams.localFileToBase64] 的与否解析为 base64
         * 或本地文件路径。
         * （注意！一些尚且不支持读取本地文件的平台，可能会直接抛出 [UnsupportedOperationException]）。
         * - 如果为可以直接读取字节数据的类型，例如 [ByteArrayResource]，
         * 则计算并使用 `base64` 格式。
         * - 其他无法判明类型的情况，尝试使用 `base64` 格式。
         *
         * @throws UnsupportedOperationException 如果解析 [resource] 时平台不支持
         */
        @JvmStatic
        @JvmOverloads
        public fun create(resource: Resource, additional: AdditionalParams? = null): OneBotRecord {
            val file = resolveResourceToFileValue(
                resource,
                additional?.localFileToBase64 == true
            )

            val data = Data(
                file = file,
                magic = additional?.magic,
                cache = additional?.cache,
                proxy = additional?.proxy,
                timeout = additional?.timeout,
            )

            return OneBotRecord(data, resource)
        }
    }

    /**
     * @property file 语音文件名
     * @property magic 发送时可选，默认 0，设置为 1 表示变声
     * @property url (receive only) 语音 URL
     * @property cache (send only) 只在通过网络 URL 发送时有效，表示是否使用已缓存的文件，默认 1
     * @property proxy (send only) 只在通过网络 URL 发送时有效，
     * 表示是否通过代理下载文件（需通过环境变量或配置文件配置代理），默认 1
     * @property timeout (send only) 只在通过网络 URL 发送时有效，单位秒，表示下载网络文件的超时时间，默认不超时
     */
    @Serializable
    public data class Data internal constructor(
        val file: String,
        val magic: String? = null,
        val url: String? = null, // receive only
        val cache: Boolean? = null, // send only
        val proxy: Boolean? = null, // send only
        val timeout: Int? = null, // send only
    )

    /**
     * 用于 [OneBotRecord] 提供的部分工厂函数中，
     * 指定 [Data] 中部分属性。
     */
    public class AdditionalParams {
        public var localFileToBase64: Boolean = false
        public var magic: String? = null
        public var cache: Boolean? = null
        public var proxy: Boolean? = null
        public var timeout: Int? = null
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is OneBotRecord) return false

        if (data != other.data) return false
        if (resource0 != other.resource0) return false

        return true
    }

    override fun hashCode(): Int {
        var result = data.hashCode()
        result = 31 * result + (resource0?.hashCode() ?: 0)
        return result
    }

    override fun toString(): String {
        return "OneBotRecord(data=$data)"
    }
}

/**
 * [短视频](https://github.com/botuniverse/onebot-11/blob/master/message/segment.md#%E7%9F%AD%E8%A7%86%E9%A2%91)
 *
 * 可用于发送和接收。
 *
 * @author ForteScarlet
 */
@Serializable
@SerialName(OneBotRecord.TYPE)
public class OneBotVideo private constructor(
    override val data: Data,
    private val resource0: Resource?,
) : OneBotMessageSegment<OneBotVideo.Data> {
    /**
     * 当前 [OneBotVideo] 中的资源信息。
     * 如果 [Data.url] 不为 `null`(即此消息是接收到的)
     * 则 [resource] 代表接收到的资源信息，否则代表用于发送的
     * [Data.file] 的资源信息。
     */
    public val resource: Resource by lazy {
        resource0 ?: data.resolveToResource()
    }

    public companion object Factory {
        public const val TYPE: String = "video"

        private fun Data.resolveToResource(): Resource {
            return resolveUrlOrFileToResource(url, file)
        }

        /**
         * 直接使用 [Data] 和 [Resource] 构建 [OneBotVideo].
         * - 如果 [Resource] 为 `null`，则会尝试解析 [data]:
         * - 如果 [Data.url] 不为 `null`，使用它（仅支持 JVM 平台）
         * - 如果 [Data.file] 是 base64 格式，解析为 [ByteArrayResource]。
         * - 如果 [Data.file] 是链接或者文件路径，
         * 解析为 `URIResource` 或 `PathResource` （仅支持 JVM 平台）
         *
         * @throws UnsupportedOperationException 如果解析 [resource] 时平台不支持
         */
        @JvmStatic
        @JvmOverloads
        public fun create(data: Data, resource: Resource? = null): OneBotVideo =
            OneBotVideo(data, resource)

        /**
         * 只通过 [Data.file] 属性构建 [OneBotVideo].
         */
        @JvmStatic
        public fun create(file: String): OneBotVideo =
            create(Data(file))

        /**
         * 直接使用 [Resource] 构建一个**用于发送**的 [OneBotVideo]。
         * 其中，[Data.file] 会根据 [Resource] 的类型计算为一个对应的值，
         * 大概规则为：
         * - 如果为 (JVM平台的) `URIResource`, 且代表的是一个网络路径，则直接使用此网络路径。
         * - 如果可以解析为本地文件（例如JVM平台的 `FileResource`，`PathResource`
         * 或 `URI.scheme` == `"file"` 的 `URIResource`，
         * 则会根据 [AdditionalParams.localFileToBase64] 的与否解析为 base64
         * 或本地文件路径。
         * （注意！一些尚且不支持读取本地文件的平台，可能会直接抛出 [UnsupportedOperationException]）。
         * - 如果为可以直接读取字节数据的类型，例如 [ByteArrayResource]，
         * 则计算并使用 `base64` 格式。
         * - 其他无法判明类型的情况，尝试使用 `base64` 格式。
         *
         * @throws UnsupportedOperationException 如果解析 [resource] 时平台不支持
         */
        @JvmStatic
        @JvmOverloads
        public fun create(resource: Resource, additional: AdditionalParams? = null): OneBotVideo {
            val file = resolveResourceToFileValue(
                resource,
                additional?.localFileToBase64 == true
            )

            val data = Data(
                file = file,
                cache = additional?.cache,
                proxy = additional?.proxy,
                timeout = additional?.timeout,
            )

            return OneBotVideo(data, resource)
        }
    }

    /**
     * @property file 文件名
     * @property url (receive only) 视频 URL
     * @property cache (send only) 只在通过网络 URL 发送时有效，表示是否使用已缓存的文件，默认 1
     * @property proxy (send only) 只在通过网络 URL 发送时有效，
     * 表示是否通过代理下载文件（需通过环境变量或配置文件配置代理），默认 1
     * @property timeout (send only) 只在通过网络 URL 发送时有效，单位秒，表示下载网络文件的超时时间，默认不超时
     */
    @Serializable
    public data class Data internal constructor(
        val file: String,
        val url: String? = null, // receive only
        val cache: Boolean? = null, // send only
        val proxy: Boolean? = null, // send only
        val timeout: Int? = null, // send only
    )

    /**
     * 用于 [OneBotVideo] 提供的部分工厂函数中，
     * 指定 [Data] 中部分属性。
     */
    public class AdditionalParams {
        public var localFileToBase64: Boolean = false
        public var cache: Boolean? = null
        public var proxy: Boolean? = null
        public var timeout: Int? = null
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is OneBotVideo) return false

        if (data != other.data) return false
        if (resource0 != other.resource0) return false

        return true
    }

    override fun hashCode(): Int {
        var result = data.hashCode()
        result = 31 * result + (resource0?.hashCode() ?: 0)
        return result
    }

    override fun toString(): String {
        return "OneBotVideo(data=$data)"
    }
}

/**
 * [@某人](https://github.com/botuniverse/onebot-11/blob/master/message/segment.md#%E6%9F%90%E4%BA%BA)
 *
 * 可用于发送，与直接使用 [At] 和 [AtAll] 没什么区别。
 * 不会被用于接收，接收时会直接解析为对应的 [At] 或 [AtAll]
 *
 */
@Serializable
@SerialName(OneBotAt.TYPE)
public class OneBotAt private constructor(
    override val data: Data,
) : OneBotMessageSegment<OneBotAt.Data>, MentionMessage {
    /**
     * 判断是否为 at 全体
     */
    public val isAll: Boolean
        get() = data.qq == ALL_QQ

    public companion object Factory {
        public const val TYPE: String = "at"
        public const val ALL_QQ: String = "all"

        /**
         * 构建一个 [OneBotAt]。
         */
        @JvmStatic
        public fun create(target: String): OneBotAt =
            OneBotAt(Data(target))

        /**
         * 构建一个 [OneBotAt]。
         */
        @JvmStatic
        public fun create(target: ID): OneBotAt =
            create(target.literal)

        /**
         * 构建一个代表 at 全体的 [OneBotAt]。
         */
        @JvmStatic
        public fun createAtAll(): OneBotAt =
            OneBotAt(Data(ALL_QQ))
    }

    @Serializable
    public data class Data internal constructor(val qq: String)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is OneBotAt) return false

        if (data != other.data) return false

        return true
    }

    override fun hashCode(): Int {
        return data.hashCode()
    }

    override fun toString(): String {
        return "OneBotAt(data=$data)"
    }
}

/**
 * [猜拳魔法表情](https://github.com/botuniverse/onebot-11/blob/master/message/segment.md#%E7%8C%9C%E6%8B%B3%E9%AD%94%E6%B3%95%E8%A1%A8%E6%83%85)
 *
 */
@Serializable
@SerialName(OneBotRps.TYPE)
public object OneBotRps : OneBotMessageSegment<Unit> {
    public const val TYPE: String = "rps"

    override val data: Unit
        get() = Unit
}

/**
 * [掷骰子魔法表情](https://github.com/botuniverse/onebot-11/blob/master/message/segment.md#%E6%8E%B7%E9%AA%B0%E5%AD%90%E9%AD%94%E6%B3%95%E8%A1%A8%E6%83%85)
 *
 */
@Serializable
@SerialName(OneBotDice.TYPE)
public object OneBotDice : OneBotMessageSegment<Unit> {
    public const val TYPE: String = "dice"

    override val data: Unit
        get() = Unit
}

/**
 * [窗口抖动（戳一戳）](https://github.com/botuniverse/onebot-11/blob/master/message/segment.md#%E7%AA%97%E5%8F%A3%E6%8A%96%E5%8A%A8%E6%88%B3%E4%B8%80%E6%88%B3-)
 *
 */
@Serializable
@SerialName(OneBotShake.TYPE)
public object OneBotShake : OneBotMessageSegment<Unit> {
    public const val TYPE: String = "shake"

    override val data: Unit
        get() = Unit
}

/**
 * [戳一戳](https://github.com/botuniverse/onebot-11/blob/master/message/segment.md#%E6%88%B3%E4%B8%80%E6%88%B3)
 *
 */
@Serializable
@SerialName(OneBotPoke.TYPE)
public class OneBotPoke private constructor(override val data: Data) : OneBotMessageSegment<OneBotPoke.Data> {
    /**
     * @see Data.type
     */
    public val type: String
        get() = data.type

    /**
     * @see Data.id
     */
    public val id: ID
        get() = data.id.ID

    /**
     * @see Data.name
     */
    public val name: String?
        get() = data.name

    public companion object Factory {
        public const val TYPE: String = "poke"

        /**
         * 构建 [OneBotPoke].
         */
        @JvmStatic
        public fun create(data: Data): OneBotPoke =
            OneBotPoke(data)

        /**
         * 构建 [OneBotPoke].
         */
        @JvmStatic
        @JvmOverloads
        public fun create(type: String, id: ID, name: String? = null): OneBotPoke =
            create(Data(type, id.literal, name))
    }

    /**
     * [OneBotPoke] 的属性。
     */
    @Serializable
    public data class Data internal constructor(
        val type: String,
        val id: String,
        val name: String?
    )

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is OneBotPoke) return false

        if (data != other.data) return false

        return true
    }

    override fun hashCode(): Int {
        return data.hashCode()
    }

    override fun toString(): String {
        return "OneBotPoke(data=$data)"
    }
}


// TODO [匿名发消息](https://github.com/botuniverse/onebot-11/blob/master/message/segment.md#%E5%8C%BF%E5%90%8D%E5%8F%91%E6%B6%88%E6%81%AF-)
// TODO [链接分享](https://github.com/botuniverse/onebot-11/blob/master/message/segment.md#%E9%93%BE%E6%8E%A5%E5%88%86%E4%BA%AB)
// TODO [推荐好友](https://github.com/botuniverse/onebot-11/blob/master/message/segment.md#%E6%8E%A8%E8%8D%90%E5%A5%BD%E5%8F%8B)
// TODO [推荐群](https://github.com/botuniverse/onebot-11/blob/master/message/segment.md#%E6%8E%A8%E8%8D%90%E7%BE%A4)
// TODO [位置](https://github.com/botuniverse/onebot-11/blob/master/message/segment.md#%E4%BD%8D%E7%BD%AE)
// TODO [音乐分享](https://github.com/botuniverse/onebot-11/blob/master/message/segment.md#%E9%9F%B3%E4%B9%90%E5%88%86%E4%BA%AB-)
// TODO [音乐自定义分享](https://github.com/botuniverse/onebot-11/blob/master/message/segment.md#%E9%9F%B3%E4%B9%90%E8%87%AA%E5%AE%9A%E4%B9%89%E5%88%86%E4%BA%AB-)
// TODO [回复](https://github.com/botuniverse/onebot-11/blob/master/message/segment.md#%E5%9B%9E%E5%A4%8D)
// TODO [合并转发](https://github.com/botuniverse/onebot-11/blob/master/message/segment.md#%E5%90%88%E5%B9%B6%E8%BD%AC%E5%8F%91-)
// TODO [合并转发节点](https://github.com/botuniverse/onebot-11/blob/master/message/segment.md#%E5%90%88%E5%B9%B6%E8%BD%AC%E5%8F%91%E8%8A%82%E7%82%B9-)
// TODO [合并转发自定义节点](https://github.com/botuniverse/onebot-11/blob/master/message/segment.md#%E5%90%88%E5%B9%B6%E8%BD%AC%E5%8F%91%E8%87%AA%E5%AE%9A%E4%B9%89%E8%8A%82%E7%82%B9)
// TODO [XML 消息](https://github.com/botuniverse/onebot-11/blob/master/message/segment.md#xml-%E6%B6%88%E6%81%AF)
// TODO [JSON 消息](https://github.com/botuniverse/onebot-11/blob/master/message/segment.md#json-%E6%B6%88%E6%81%AF)



internal fun resolveUrlOrFileToResource(url: String?, file: String): Resource {
    return if (url != null) {
        uriResource(url)
    } else {
        when {
            file.startsWith("file://") -> {
                pathResource(file.substring(7))
            }

            file.startsWith("base64://") -> {
                base64Resource(file.substring(9))
            }

            else -> {
                uriResource(file)
            }
        }
    }
}

internal expect fun uriResource(uri: String): Resource
internal expect fun pathResource(path: String): Resource

@OptIn(ExperimentalEncodingApi::class)
internal fun base64Resource(data: String): Resource {
    return Base64.UrlSafe.decode(data).toResource()
}

internal expect fun resolveResourceToFileValuePlatform(
    resource: Resource,
    localFileToBase64: Boolean,
): String?

internal fun resolveResourceToFileValue(
    resource: Resource,
    localFileToBase64: Boolean,
): String {
    return when (resource) {
        is ByteArrayResource -> computeBase64FileValue(resource.data())
        else -> {
            resolveResourceToFileValuePlatform(resource, localFileToBase64)
                ?: computeBase64FileValue(resource.data())
        }
    }
}

@OptIn(ExperimentalEncodingApi::class)
internal fun computeBase64FileValue(data: ByteArray): String {
    return buildString {
        append("base64://")
        Base64.UrlSafe.encodeToAppendable(data, this)
    }
}
