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
import love.forte.simbot.bot.Bot
import love.forte.simbot.common.id.ID
import love.forte.simbot.component.onebot.v11.core.api.GetLoginInfoApi
import love.forte.simbot.component.onebot.v11.core.api.GetLoginInfoResult
import love.forte.simbot.suspendrunner.ST
import kotlin.coroutines.CoroutineContext


/**
 * 一个 OneBot协议的 [Bot]。
 *
 * @author ForteScarlet
 */
public interface OneBotBot : Bot {
    override val coroutineContext: CoroutineContext

    /**
     * 当前Bot的配置类。
     */
    public val configuration: OneBotBotConfiguration

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
     * 如果希望通过API查询登录信息或刷新此缓存，参考 [getLoginInfo]。
     * @throws IllegalStateException Bot的selfId尚未初始化，即Bot尚未启动。
     */
    override val name: String

    /**
     * 通过 [GetLoginInfoApi] 查询当前Bot的信息，
     * 并刷新 [userId] 和 [name] 的缓存值。
     */
    @ST
    public suspend fun getLoginInfo(): GetLoginInfoResult

    /**
     * 启动Bot。
     * 启动过程中会通过 [GetLoginInfoApi] 初始化当前账户信息，
     * 并同时初始化 [userId]。
     *
     * 启动时，会开始建立与 [OneBotBotConfiguration.eventServerHost] 的 WS 连接。
     * 如果在已经启动的情况下再次启动，则会关闭原本的连接后重新连接。
     */
    override suspend fun start()


}
