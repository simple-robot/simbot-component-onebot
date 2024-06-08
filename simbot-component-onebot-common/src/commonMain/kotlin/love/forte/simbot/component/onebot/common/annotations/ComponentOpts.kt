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

package love.forte.simbot.component.onebot.common.annotations

/**
 * 标记为仅用于OneBot组件内部使用的API，可能会随时变更、删除
 */
@Retention(AnnotationRetention.BINARY)
@RequiresOptIn(
    message = "仅用于OneBot组件内部使用的API，可能会随时变更、删除",
    level = RequiresOptIn.Level.ERROR
)
@MustBeDocumented
public annotation class InternalOneBotAPI

/**
 * 标记为OneBot组件中仍处于实验阶段的API，可能会随时变更、删除
 */
@Retention(AnnotationRetention.BINARY)
@RequiresOptIn(
    message = "OneBot组件中仍处于实验阶段的API，可能会随时变更、删除",
    level = RequiresOptIn.Level.ERROR
)
@MustBeDocumented
public annotation class ExperimentalOneBotAPI
