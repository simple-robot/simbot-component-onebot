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

import com.google.devtools.ksp.gradle.KspTaskMetadata
import love.forte.gradle.common.core.project.setup
import love.forte.gradle.common.kotlin.multiplatform.applyTier1
import love.forte.gradle.common.kotlin.multiplatform.applyTier2
import love.forte.gradle.common.kotlin.multiplatform.applyTier3
import org.jetbrains.dokka.gradle.DokkaTaskPartial
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi

plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization")
    id("org.jetbrains.dokka")
    id("love.forte.plugin.suspend-transform")
    alias(libs.plugins.ksp)
}

setup(P.ComponentOneBot)
configJavaCompileWithModule("simbot.component.onebot11v.core")
apply(plugin = "simbot-onebot-multiplatform-maven-publish")

kotlin {
    explicitApi()
    applyDefaultHierarchyTemplate()

    @OptIn(ExperimentalKotlinGradlePluginApi::class)
    compilerOptions {
        optIn.addAll(
            "love.forte.simbot.annotations.InternalSimbotAPI",
            "love.forte.simbot.component.onebot.common.annotations.InternalOneBotAPI",
            "love.forte.simbot.component.onebot.common.annotations.ExperimentalOneBotAPI",
        )
    }

    configKotlinJvm()

    js(IR) {
        configJs()
    }

    applyTier1()
    applyTier2()
    applyTier3(supportKtorClient = true)

    sourceSets {
        commonMain.dependencies {
            // JVM compileOnly
            implementation(libs.simbot.api)
            api(libs.jetbrains.annotations)

            api(libs.simbot.common.annotations)
            implementation(libs.simbot.common.atomic)

            api(project(":simbot-component-onebot-common"))
            api(project(":simbot-component-onebot-v11:simbot-component-onebot-v11-common"))
            api(project(":simbot-component-onebot-v11:simbot-component-onebot-v11-message"))
            api(project(":simbot-component-onebot-v11:simbot-component-onebot-v11-event"))

            api(libs.kotlinx.coroutines.core)
            api(libs.kotlinx.serialization.json)
            api(libs.ktor.client.core)
            api(libs.ktor.client.ws)
            api(libs.simbot.logger)
            api(libs.simbot.common.suspend)
            api(libs.ktor.client.contentNegotiation)
            // api(libs.simbot.common.atomic)
            // api(libs.simbot.common.core)
        }

        commonTest.dependencies {
            api(libs.simbot.core)
            api(kotlin("test"))
            api(libs.ktor.client.mock)
            api(libs.kotlinx.coroutines.test)
        }

        jvmMain {
            dependencies {
                compileOnly(libs.simbot.api)
                compileOnly(libs.ktor.client.contentNegotiation)
                compileOnly(libs.jetbrains.annotations)
            }
        }

        jvmTest.dependencies {
            implementation(libs.log4j.api)
            implementation(libs.mockk)
            implementation(libs.log4j.core)
            implementation(libs.log4j.slf4j2)
            implementation(libs.kotlinPoet)
            implementation(libs.kotlinx.coroutines.reactor)
            implementation(libs.ktor.client.java)
            implementation(libs.ktor.server.netty)
            implementation(libs.ktor.server.ws)
        }

        appleTest.dependencies {
            implementation(libs.ktor.client.darwin)
        }
    }
}

// https://github.com/google/ksp/issues/963#issuecomment-1894144639
dependencies {
    // kspCommonMainMetadata(project(":internal-processors:include-component-message-elements-processor"))
}
kotlin.sourceSets.commonMain {
    // solves all implicit dependency trouble and IDEs source code detection
    // see https://github.com/google/ksp/issues/963#issuecomment-1894144639
    tasks.withType<KspTaskMetadata> { kotlin.srcDir(destinationDirectory) }
}

tasks.withType<DokkaTaskPartial>().configureEach {
    dokkaSourceSets.configureEach {
        suppressGeneratedFiles.set(false)
    }
}
