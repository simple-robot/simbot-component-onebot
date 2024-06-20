/*
 * Copyright (c) 2023-2024. ForteScarlet.
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

import love.forte.gradle.common.core.project.ProjectDetail
import love.forte.gradle.common.core.project.Version
import love.forte.gradle.common.core.project.minus
import love.forte.gradle.common.core.property.systemProp
import love.forte.gradle.common.core.project.version as v


/**
 * Project versions.
 */
@Suppress("unused", "MemberVisibilityCanBePrivate")
object P {
    object Simbot {
        const val GROUP = "love.forte.simbot"
    }

    object ComponentOneBot : ProjectDetail() {
        const val GROUP = "love.forte.simbot.component"
        const val DESCRIPTION = "基于Kotlin协程的OneBot协议实现库, 同时也是 Simple Robot 的组件库"
        const val HOMEPAGE = "https://github.com/simple-robot/simbot-component-onebot"

        override val group: String get() = GROUP
        override val description: String get() = DESCRIPTION
        override val homepage: String get() = HOMEPAGE


        private val baseVersion = v(0, 7, 0)

        val snapshotVersion = baseVersion - Version.SNAPSHOT
        override val version = if (isSnapshot()) snapshotVersion else baseVersion

        override val developers: List<Developer> = developers {
            developer {
                id = "forte"
                name = "ForteScarlet"
                email = "ForteScarlet@163.com"
                url = "https://github.com/ForteScarlet"
            }
            developer {
                id = "forliy"
                name = "ForliyScarlet"
                email = "ForliyScarlet@163.com"
                url = "https://github.com/ForliyScarlet"
            }
        }

        override val licenses: List<License> = licenses {
            license {
                name = "GNU GENERAL PUBLIC LICENSE, Version 3"
                url = "https://www.gnu.org/licenses/gpl-3.0-standalone.html"
            }
            license {
                name = "GNU LESSER GENERAL PUBLIC LICENSE, Version 3"
                url = "https://www.gnu.org/licenses/lgpl-3.0-standalone.html"
            }
        }

        override val scm: Scm = scm {
            url = HOMEPAGE
            connection = "scm:git:$HOMEPAGE.git"
            developerConnection = "scm:git:ssh://git@github.com/simple-robot/simbot-component-onebot.git"
        }
    }


}

private val _isSnapshot by lazy { initIsSnapshot() }

private fun initIsSnapshot(): Boolean {
    val property = System.getProperty("simbot.snapshot").toBoolean()
    val env = System.getenv(Env.IS_SNAPSHOT).toBoolean()

    return property || env
}

fun isSnapshot(): Boolean = _isSnapshot

fun isSimbotLocal(): Boolean = systemProp("SIMBOT_LOCAL").toBoolean()

val isLinux: Boolean = systemProp("os.name")?.contains("linux", true) ?: false
