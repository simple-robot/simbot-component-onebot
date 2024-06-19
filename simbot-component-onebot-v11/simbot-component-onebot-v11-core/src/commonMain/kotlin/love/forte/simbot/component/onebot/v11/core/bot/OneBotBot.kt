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

package love.forte.simbot.component.onebot.v11.core.bot

import io.ktor.client.*
import io.ktor.http.*
import kotlinx.coroutines.flow.count
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.serialization.json.Json
import love.forte.simbot.bot.Bot
import love.forte.simbot.bot.ContactRelation
import love.forte.simbot.bot.GroupRelation
import love.forte.simbot.bot.GuildRelation
import love.forte.simbot.common.collectable.Collectable
import love.forte.simbot.common.id.ID
import love.forte.simbot.component.onebot.common.annotations.ExperimentalOneBotAPI
import love.forte.simbot.component.onebot.common.annotations.InternalOneBotAPI
import love.forte.simbot.component.onebot.common.annotations.OneBotInternalImplementationsOnly
import love.forte.simbot.component.onebot.v11.core.actor.OneBotFriend
import love.forte.simbot.component.onebot.v11.core.actor.OneBotGroup
import love.forte.simbot.component.onebot.v11.core.actor.OneBotMember
import love.forte.simbot.component.onebot.v11.core.actor.OneBotStranger
import love.forte.simbot.component.onebot.v11.core.api.*
import love.forte.simbot.component.onebot.v11.message.OneBotMessageContent
import love.forte.simbot.suspendrunner.ST
import kotlin.coroutines.CoroutineContext
import kotlin.jvm.JvmSynthetic


/**
 * 一个 OneBot 协议的 [Bot]。
 *
 * ### Bot 的终止
 * Bot的运行状态 ([isActive]) 可能会因为一些原因被更改：
 * - 手动终止 (使用 [cancel])。
 * - 父级Job或被关联的Job被终止 (可能来自 BotManager、Application等)。
 * - 会话连接失败且重试次数到达上限。比如 ws 的连接会话意外被中断，
 * 且在重新连接的过程中始终失败并达到了重试次数上限，此时会话中的任务会被视为因异常结束，
 * 并连带 [OneBotBot] 的任务一同终结。
 *
 *
 * @author ForteScarlet
 */
@OneBotInternalImplementationsOnly
public interface OneBotBot : Bot {
    override val coroutineContext: CoroutineContext

    /**
     * [OneBotBot] 会使用的 [Json]
     */
    public val decoderJson: Json

    /**
     * 当前Bot的配置类。
     */
    public val configuration: OneBotBotConfiguration

    /**
     * 由 [OneBotBot] 衍生出的 actor 使用的 [CoroutineContext]。
     * 源自 [coroutineContext], 但是不包含 [Job][kotlinx.coroutines.Job]。
     */
    @InternalOneBotAPI
    public val subContext: CoroutineContext

    /**
     * [OneBotBot] 用于请求API的 [HttpClient]。
     */
    public val apiClient: HttpClient

    /**
     * [OneBotBot] 用于请求 API 的目标服务器地址
     */
    public val apiHost: Url

    /**
     * [OneBotBotConfiguration] 中配置的 accessToken。
     */
    public val accessToken: String?

    /**
     * 当前Bot的唯一标识。
     * ## 唯一标识
     * 在OneBot中，Bot在启动并可以获悉 `self_id` 或 `user_id`
     * 之前，是没有一个明确的“唯一标识”的。
     * 因此，OneBot组件要求注册 Bot 时必须指定一个可以用于区分不同 Bot 的
     * [OneBotBotConfiguration.botUniqueId]，也就是此 [id]。
     *
     * 当注册bot时，会以 [OneBotBotConfiguration.botUniqueId] 作为检测冲突的标准。
     * 当Bot的 [userId] 被初始化后，可以通过 [isMe] 同时检测 [id] 或 [userId] 是否相同。
     *
     * @see OneBotBotConfiguration.botUniqueId
     */
    override val id: ID

    /**
     * 检测给定的 [id] 是否与 [OneBotBot.id] 或 [userId] (如果已经初始化)
     * 相同。
     *
     * 如果 [userId] 尚未初始化，则只检测 [OneBotBot.id]。
     */
    override fun isMe(id: ID): Boolean

    /**
     * Bot自身的 ID，来自事件或 [GetLoginInfoApi] API.
     * @throws IllegalStateException Bot的selfId尚未初始化，即Bot尚未启动。
     */
    public val userId: ID

    /**
     * Bot的名称，来自 [GetLoginInfoApi] API.
     * [name] 是一个缓存值，不会实时查询或实时刷新。
     *
     * 如果希望通过API查询登录信息或刷新此缓存，参考 [queryLoginInfo]。
     * @throws IllegalStateException Bot的selfId尚未初始化，即Bot尚未启动。
     */
    override val name: String

    /**
     * 通过 [GetLoginInfoApi] 查询当前Bot的信息，
     * 并刷新 [userId] 和 [name] 的缓存值。
     */
    @ST
    public suspend fun queryLoginInfo(): GetLoginInfoResult

    /**
     * 启动Bot。
     * 启动过程中会通过 [GetLoginInfoApi] 初始化当前账户信息，
     * 并同时初始化 [userId]。
     *
     * 启动时，会开始建立与 [OneBotBotConfiguration.eventServerHost] 的 WS 连接。
     * 如果在已经启动的情况下再次启动，则会关闭原本的连接后重新连接。
     */
    @JvmSynthetic
    override suspend fun start()

    /**
     * 联系人相关操作，即好友相关的关系操作。
     */
    override val contactRelation: OneBotBotFriendRelation

    /**
     * 与群聊相关的操作
     */
    override val groupRelation: OneBotBotGroupRelation

    /**
     * 始终为 `null`。
     * OB11协议主要为普通群聊设计。
     */
    override val guildRelation: GuildRelation?
        get() = null

    /**
     * 获取 Cookies
     * @see GetCookiesApi
     * @throws Throwable 任何API请求过程中可能产生的异常
     */
    @ST
    public suspend fun getCookies(domain: String? = null): GetCookiesResult

    /**
     * 获取 QQ 相关接口凭证
     * @see GetCredentialsApi
     * @throws Throwable 任何API请求过程中可能产生的异常
     */
    @ST
    public suspend fun getCredentials(domain: String? = null): GetCredentialsResult

    /**
     * 获取 CSRF Token
     * @see GetCsrfTokenApi
     * @throws Throwable 任何API请求过程中可能产生的异常
     */
    @ST
    public suspend fun getCsrfToken(): GetCsrfTokenResult

    /**
     * 根据 [messageId] 使用 [GetMsgApi] 查询消息内容，
     * 并得到对应的 [OneBotMessageContent]。
     *
     * 注意：此API是实验性的，未来可能会随时被变更或删除。
     *
     * @see GetMsgApi
     * @throws Throwable 任何API请求过程中可能产生的异常，
     * 例如消息不存在
     */
    @ST
    @ExperimentalOneBotAPI
    public suspend fun getMessageContent(messageId: ID): OneBotMessageContent

}

/**
 * 联系人相关操作，即好友相关的关系操作。
 *
 * @see OneBotBot.contactRelation
 */
@OneBotInternalImplementationsOnly
public interface OneBotBotFriendRelation : ContactRelation {
    /**
     * 获取好友列表。
     */
    override val contacts: Collectable<OneBotFriend>

    /**
     * 根据ID查询某个指定的好友。
     *
     * OneBot11协议中没有直接根据ID查询好友的API，
     * 因此会直接通过 [contacts]
     * 查询好友列表，并从内存中筛选。
     */
    @ST(
        blockingBaseName = "getContact",
        blockingSuffix = "",
        asyncBaseName = "getContact",
        reserveBaseName = "getContact"
    )
    override suspend fun contact(id: ID): OneBotFriend? =
        contacts.asFlow().firstOrNull { it.id == id }

    /**
     * 获取好友的数量。
     *
     * OneBot11中没有直接获取的API，因此会通过 [contacts]
     * 获取全部列表并从内存中计数来达成此功能。
     */
    @JvmSynthetic
    override suspend fun contactCount(): Int =
        contacts.asFlow().count()

    /**
     * 根据 [id] 查询对应的陌生人信息。
     *
     * @throws Throwable 请求API可能抛出的任何异常，
     * 例如不存在对应的账户信息。
     */
    @ST(
        blockingBaseName = "getStranger",
        blockingSuffix = "",
        asyncBaseName = "getStranger",
        reserveBaseName = "getStranger"
    )
    public suspend fun stranger(id: ID): OneBotStranger
}


/**
 * 与群聊相关的操作
 *
 * @see OneBotBot.groupRelation
 */
@OneBotInternalImplementationsOnly
public interface OneBotBotGroupRelation : GroupRelation {
    /**
     * 获取群列表
     */
    override val groups: Collectable<OneBotGroup>

    /**
     * 根据ID查询某个指定的群。
     */
    @ST(
        blockingBaseName = "getGroup",
        blockingSuffix = "",
        asyncBaseName = "getGroup",
        reserveBaseName = "getGroup"
    )
    override suspend fun group(id: ID): OneBotGroup?

    /**
     * 获取群数量。
     *
     * OneBot11中没有直接获取的API，因此会通过 [groups]
     * 获取全部列表并从内存中计数来达成此功能。
     */
    @JvmSynthetic
    override suspend fun groupCount(): Int =
        groups.asFlow().count()

    /**
     * 根据ID查询某个群中的某个成员。
     */
    @ST(
        blockingBaseName = "getMember",
        blockingSuffix = "",
        asyncBaseName = "getMember",
        reserveBaseName = "getMember"
    )
    public suspend fun member(groupId: ID, memberId: ID): OneBotMember?

}
