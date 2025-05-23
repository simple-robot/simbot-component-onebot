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
import love.forte.gradle.common.kotlin.multiplatform.applyTier123
import org.jetbrains.dokka.gradle.DokkaTaskPartial
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi

plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization")
    id("org.jetbrains.dokka")
    alias(libs.plugins.ksp)
}

setup(P.ComponentOneBot)
configJavaCompileWithModule("simbot.component.onebot11v.event")
apply(plugin = "simbot-onebot-multiplatform-maven-publish")

kotlin {
    explicitApi()
    applyDefaultHierarchyTemplate()

    @OptIn(ExperimentalKotlinGradlePluginApi::class)
    compilerOptions {
        optIn.addAll(
            "love.forte.simbot.annotations.InternalSimbotAPI",
            "love.forte.simbot.component.onebot.common.annotations.InternalOneBotAPI"
        )
    }

    configKotlinJvm()

    js(IR) {
        configJs()
    }

    applyTier123()

    sourceSets {
        commonMain.dependencies {
            implementation(libs.simbot.api)
            api(libs.simbot.common.annotations)
            api(project(":simbot-component-onebot-common"))
            api(libs.kotlinx.serialization.json)

            api(project(":simbot-component-onebot-v11:simbot-component-onebot-v11-common"))
            api(project(":simbot-component-onebot-v11:simbot-component-onebot-v11-message"))

            api(libs.kotlinx.coroutines.core)
        }

        commonTest.dependencies {
            api(libs.simbot.core)
            api(kotlin("test"))
            api(libs.kotlinx.coroutines.test)
        }

        jvmMain {
            dependencies {
                compileOnly(libs.simbot.api)
            }
        }

        jvmTest.dependencies {
            implementation(libs.log4j.api)
            implementation(libs.log4j.core)
            implementation(libs.log4j.slf4j2)
            implementation(libs.kotlinPoet)
        }
    }
}

// https://github.com/google/ksp/issues/963#issuecomment-1894144639
dependencies {
    kspCommonMainMetadata(project(":internal-processors:event-type-resolver-processor"))
}
kotlin.sourceSets.commonMain {
    // solves all implicit dependency trouble and IDEs source code detection
    // see https://github.com/google/ksp/issues/963#issuecomment-1894144639
    tasks.withType<KspTaskMetadata> {
        kotlin.srcDir(destinationDirectory.file("kotlin"))
    }
}

tasks.withType<DokkaTaskPartial>().configureEach {
    dokkaSourceSets.configureEach {
        suppressGeneratedFiles.set(false)
    }
}
