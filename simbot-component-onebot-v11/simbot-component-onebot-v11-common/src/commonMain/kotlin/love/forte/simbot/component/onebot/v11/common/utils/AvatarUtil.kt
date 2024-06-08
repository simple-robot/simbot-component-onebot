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

@file:JvmName("AvatarUtil")

package love.forte.simbot.component.onebot.v11.common.utils

import love.forte.simbot.component.onebot.common.annotations.InternalOneBotAPI
import kotlin.jvm.JvmName

/**
 * 得到 `s=640` 的QQ头像。
 *
 * @param id QQ号
 */
@InternalOneBotAPI
public fun qqAvatar640(id: String): String =
    "https://q1.qlogo.cn/g?b=qq&nk=$id&s=640"

/**
 * 得到 `s=100` 的QQ头像。
 *
 * @param id QQ号
 */
@InternalOneBotAPI
public fun qqAvatar100(id: String): String =
    "https://q1.qlogo.cn/g?b=qq&nk=$id&s=100"
