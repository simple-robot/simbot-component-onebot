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

package love.forte.simbot.component.onebot.v11.event.message

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import love.forte.simbot.common.id.ID
import love.forte.simbot.common.id.LongID
import love.forte.simbot.component.onebot.v11.event.Event
import love.forte.simbot.component.onebot.v11.event.SourceEventConstructor
import love.forte.simbot.component.onebot.v11.message.segment.OneBotMessageSegment


/**
 * [消息事件](https://github.com/botuniverse/onebot-11/blob/master/event/message.md#消息事件)
 *
 * [MessageEvent] 中定义的属性为针对私聊消息和群消息事件属性中人为抽取的 _可能公共_ 属性，
 * 并非协议中明确定义一定相同的公共属性。
 * 此处只做最低限度的公共抽取。
 *
 * @author ForteScarlet
 */
public interface MessageEvent : Event {
    /**
     * 消息 ID
     */
    public val messageId: ID

    /**
     * 消息类型。比如 `private`、`group`。
     */
    public val messageType: String

    /**
     * 消息子类型。
     */
    public val subType: String

    /**
     * 消息内容。
     */
    public val message: List<OneBotMessageSegment>

    /**
     * 原始消息内容。
     */
    public val rawMessage: String

    /**
     * 发送者 QQ 号
     */
    public val userId: LongID

    /**
     * 字体。
     * 如果不支持、无法获取则会得到 `null`。
     */
    public val font: Int?

    /**
     * 发送人信息
     */
    public val sender: Sender

    /**
     * 从私聊消息事件的 `sender` 和群消息事件的 `sender`
     * 中抽取出的公共属性所定义的 sender 接口。
     */
    public interface Sender {
        /**
         * 发送者 QQ 号
         */
        public val userId: LongID

        /**
         * 昵称
         */
        public val nickname: String

        /**
         * 性别，`male` 或 `female` 或 `unknown`
         */
        public val sex: String

        /**
         * 年龄。在不支持的情况下可能会得到负数，
         * 比如一个默认值 `-1`。
         *
         * 这种不支持的默认值可能来自 OneBot 协议服务端实现，
         * 也可能来自组件内对设置的于此字段缺失的缺省默认值。
         */
        public val age: Int
    }
}

/**
 * [私聊消息事件](https://github.com/botuniverse/onebot-11/blob/master/event/message.md#私聊消息)
 *
 * @property time 事件发生的时间戳
 * @property selfId 收到事件的机器人 QQ 号
 * @property postType 上报类型
 * @property messageType 消息类型
 * @property subType 消息子类型，如果是好友则是 `friend`，如果是群临时会话则是 `group`
 * @property messageId 消息 ID
 * @property userId 发送者 QQ 号
 * @property message 消息内容
 * @property rawMessage 原始消息内容
 * @property font 字体
 * @property sender 发送人信息
 */
@Serializable
public data class PrivateMessageEvent @SourceEventConstructor constructor(
    override val time: Long,
    @SerialName("self_id")
    override val selfId: LongID,
    @SerialName("post_type")
    override val postType: String,
    @SerialName("message_id")
    override val messageId: ID,
    @SerialName("message_type")
    override val messageType: String,
    @SerialName("sub_type")
    override val subType: String,
    override val message: List<OneBotMessageSegment> = emptyList(),
    @SerialName("raw_message")
    override val rawMessage: String,
    @SerialName("user_id")
    override val userId: LongID,
    override val font: Int?,
    override val sender: Sender
) : MessageEvent {
    /**
     * 私聊事件的发送人信息
     */
    @Serializable
    public data class Sender @SourceEventConstructor constructor(
        @SerialName("user_id")
        override val userId: LongID,
        override val nickname: String,
        override val sex: String = DEFAULT_SEX,
        override val age: Int = DEFAULT_AGE
    ) : MessageEvent.Sender
}


/**
 * [群消息事件](https://github.com/botuniverse/onebot-11/blob/master/event/message.md#群消息)
 * @property time 事件发生的时间戳
 * @property selfId 收到事件的机器人 QQ 号
 * @property postType 上报类型
 * @property messageType 消息类型
 * @property subType 消息子类型，正常消息是 `normal`，匿名消息是 `anonymous`，
 * 系统提示（如「管理员已禁止群内匿名聊天」）是 `notice`
 * @property anonymous 匿名信息，如果不是匿名消息则为 `null`
 * @property messageId 消息 ID
 * @property groupId 群号
 * @property userId 发送者 QQ 号
 * @property message 消息内容
 * @property rawMessage 原始消息内容
 * @property font 字体
 * @property sender 发送人信息
 *
 *
 */
@Serializable
public data class GroupMessageEvent @SourceEventConstructor constructor(
    override val time: Long,
    @SerialName("self_id")
    override val selfId: LongID,
    @SerialName("post_type")
    override val postType: String,
    @SerialName("message_id")
    override val messageId: ID,
    @SerialName("group_id")
    public val groupId: ID,
    @SerialName("message_type")
    override val messageType: String,
    @SerialName("sub_type")
    override val subType: String,
    public val anonymous: Anonymous? = null,
    override val message: List<OneBotMessageSegment> = emptyList(),
    @SerialName("raw_message")
    override val rawMessage: String,
    @SerialName("user_id")
    override val userId: LongID,
    override val font: Int?,
    override val sender: Sender
) : MessageEvent {

    /**
     * 匿名信息
     *
     * @property id 匿名用户 ID
     * @property name 匿名用户名称
     * @property flag 匿名用户 `flag`，在调用禁言 API 时需要传入
     */
    @Serializable
    public data class Anonymous @SourceEventConstructor constructor(
        val id: LongID,
        val name: String,
        val flag: String,
    )

    /**
     * 群消息的发送人信息
     */
    @Serializable
    public data class Sender @SourceEventConstructor constructor(
        @SerialName("user_id")
        override val userId: LongID,
        override val nickname: String,
        public val card: String = "",
        public val area: String? = null,
        public val level: Int? = null,
        public val role: String,
        public val title: String? = null,
        override val sex: String = DEFAULT_SEX,
        override val age: Int = DEFAULT_AGE,
    ) : MessageEvent.Sender

}

private const val DEFAULT_SEX: String = "unknown"
private const val DEFAULT_AGE: Int = -1
