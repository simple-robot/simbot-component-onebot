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

import io.gitlab.arturbosch.detekt.Detekt
import love.forte.gradle.common.core.project.setup
import love.forte.gradle.common.core.repository.Repositories
import love.forte.plugin.suspendtrans.gradle.SuspendTransformPluginExtension
import love.forte.simbot.gradle.suspendtransforms.addSimbotJvmTransforms
import util.isCi

plugins {
    idea
    id("org.jetbrains.dokka")
    `simbot-onebot-nexus-publish`
    `simbot-onebot-changelog-generator`
    alias(libs.plugins.detekt)
    alias(libs.plugins.kotlinxBinaryCompatibilityValidator)
    id("love.forte.plugin.suspend-transform") apply false
    // alias(libs.plugins.suspendTransform) apply false
}

setup(P.ComponentOneBot)

logger.info("=== Current version: {} ===", version)

allprojects {
    setup(P.ComponentOneBot)
    repositories {
        mavenCentral()
        maven {
            url = uri(Repositories.Snapshot.URL)
            mavenContent {
                snapshotsOnly()
            }
        }
        mavenLocal()
    }
}

subprojects {
    afterEvaluate {
        if (plugins.hasPlugin(libs.plugins.suspendTransform.get().pluginId)) {
            extensions.configure<SuspendTransformPluginExtension>("suspendTransformPlugin") {
                includeRuntime = false
                includeAnnotation = false

                addSimbotJvmTransforms()
            }
        }
    }
}

idea {
    module.apply {
        isDownloadSources = true
    }
    project {
        modules.forEach { module ->
            module.apply {
                isDownloadSources = true
            }
        }
    }
}

//region detekt
dependencies {
    detektPlugins("io.gitlab.arturbosch.detekt:detekt-formatting:${libs.versions.detekt.get()}")
}

detekt {
    source.setFrom(subprojects.map { it.projectDir.absoluteFile.resolve("src") })
    config.setFrom(rootDir.resolve("config/detekt/detekt.yml"))
    baseline = rootDir.resolve("config/detekt/baseline.xml")
    // buildUponDefaultConfig = true
    parallel = true
    reportsDir = rootProject.layout.buildDirectory.dir("reports/detekt").get().asFile
    if (!isCi) {
        autoCorrect = true
    }
    basePath = projectDir.absolutePath
}

// https://detekt.dev/blog/2019/03/03/configure-detekt-on-root-project/
tasks.withType<Detekt>().configureEach {
    // internal 处理器不管
    exclude("internal-processors/**")

    include("**/src/*Main/kotlin/**/*.kt")
    include("**/src/*Main/kotlin/**/*.java")
    include("**/src/*Main/java/**/*.kt")
    include("**/src/*Main/java/**/*.java")
    include("**/src/main/kotlin/**/*.kt")
    include("**/src/main/kotlin/**/*.java")
    include("**/src/main/java/**/*.kt")
    include("**/src/main/java/**/*.java")

    exclude("**/src/*/resources/")
    exclude("**/build/")
    exclude("**/*Test/kotlin/")
    exclude("**/*Test/java/")
    exclude("**/test/kotlin/")
    exclude("**/test/java/")
}
//endregion

apiValidation {
    ignoredPackages.add("*.internal.*")

    this.ignoredProjects.addAll(
        listOf(
            "event-type-resolver-processor",
            "include-component-message-elements-processor",
        )
    )

    // 实验性和内部API可能无法保证二进制兼容
    nonPublicMarkers.addAll(
        listOf(
            "love.forte.simbot.component.onebot.common.annotations.InternalOneBotAPI",
            "love.forte.simbot.component.onebot.common.annotations.ExperimentalOneBotAPI",

            "love.forte.simbot.annotations.ExperimentalSimbotAPI",
            "love.forte.simbot.annotations.InternalSimbotAPI",
            "love.forte.simbot.component.onebot.common.annotations.ApiResultConstructor",
            "love.forte.simbot.component.onebot.common.annotations.SourceEventConstructor",

            // CustomEventResolver
            "love.forte.simbot.component.onebot.v11.core.event.ExperimentalCustomEventResolverApi"
        ),
    )

    apiDumpDirectory = "api"
}

// region Dokka

subprojects {
    afterEvaluate {
        val p = this
        if (plugins.hasPlugin(libs.plugins.dokka.get().pluginId)) {
            dokka {
                configSourceSets(p)
                pluginsConfiguration.html {
                    configHtmlCustoms(p)
                }
            }
            rootProject.dependencies.dokka(p)
        }
    }
}

dokka {
    moduleName = "Simple Robot 组件 | OneBot"

    dokkaPublications.all {
        if (isSimbotLocal()) {
            logger.info("Is 'SIMBOT_LOCAL', offline")
            offlineMode = true
        }
    }

    configSourceSets(project)

    pluginsConfiguration.html {
        configHtmlCustoms(project)
    }
}
// endregion

