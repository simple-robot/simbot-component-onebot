/*
 * Copyright (c) 2022-2024. ForteScarlet.
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


tasks.register("createChangelog") {
    group = "documentation"
    doFirst {
        val realVersion = P.ComponentOneBot.version.toString()
        val version = "v$realVersion"
        logger.info("Generate change log for {} ...", version)
        // configurations.runtimeClasspath
        val changelogDir = rootProject.file(".changelog").also {
            it.mkdirs()
        }
        val file = File(changelogDir, "$version.md")
        if (!file.exists()) {

            val libs = rootProject.extensions.getByType<VersionCatalogsExtension>()
                .named("libs")

            val simbotVersion = libs.findVersion("simbot").get()

            val coreVersion = simbotVersion
            val autoGenerateText = buildString {
                appendLine("> 对应核心版本: [**v$coreVersion**](https://github.com/simple-robot/simpler-robot/releases/tag/v$coreVersion)\n\n")

                if ("dev" in version) {
                    appendLine(
                        """
                        > [!warning]
                        > **目前版本处于 `dev` 阶段，代表此版本是一个开发预览版，可能不稳定、可能随时发生更改、且不保证可用性。**
                        
                        
                    """.trimIndent()
                    )
                }

                appendLine(
                    """
                    我们欢迎并期望着您的的[反馈](https://github.com/simple-robot/simbot-component-onebot/issues)或[协助](https://github.com/simple-robot/simbot-component-onebot/pulls)，
                    感谢您的贡献与支持！

                    也欢迎您为我们献上一颗 `star`，这是对我们最大的鼓励与认可！
                """.trimIndent()
                )
            }

            file.createNewFile()
            file.writeText(autoGenerateText)
        }
    }
}


fun repoRow(moduleName: String, group: String, id: String, version: String): String {
    return "| $moduleName | [$moduleName: v$version](https://repo1.maven.org/maven2/${
        group.replace(
            ".",
            "/"
        )
    }/${
        id.replace(
            ".",
            "/"
        )
    }/$version) | [$moduleName: v$version](https://search.maven.org/artifact/$group/$id/$version/jar)  |"
}
