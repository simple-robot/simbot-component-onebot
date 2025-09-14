/*
 *     Copyright (c) 2025. ForteScarlet.
 *
 *     Project    https://github.com/simple-robot/simbot-component-onebot
 *     Email      ForteScarlet@163.com
 *
 *     This project and this file are part of the Simple Robot Library (Alias: simple-robot, simbot, etc.).
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Lesser General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     Lesser GNU General Public License for more details.
 *
 *     You should have received a copy of the Lesser GNU General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 *
 */

package love.forte.simbot.component.onebot.v11.core.api.nonstandard

/**
 * 与非标准API相关的内容。
 * 非标准API的实现目前尚处于实验阶段。它们不保证兼容性，并且可能在未来发生变更或被移除。
 *
 * @since 1.9.0
 * @author ForteScarlet
 */
@Retention(AnnotationRetention.BINARY)
@Target(AnnotationTarget.CLASS, AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY)
@RequiresOptIn("This api is not part of the onebot protocol standard, and may not be available in the future.")
public annotation class OneBotNonStandardApi
