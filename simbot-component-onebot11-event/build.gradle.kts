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
import love.forte.gradle.common.kotlin.multiplatform.applyTier1
import love.forte.gradle.common.kotlin.multiplatform.applyTier2
import love.forte.gradle.common.kotlin.multiplatform.applyTier3
import org.jetbrains.dokka.gradle.DokkaTaskPartial

plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization")
    `simbot-onebot-dokka-partial-configure`

    alias(libs.plugins.ksp)
}

configJavaCompileWithModule("simbot.component.onebot11.event")
// apply(plugin = "simbot-onebot-multiplatform-maven-publish")

kotlin {
    explicitApi()
    applyDefaultHierarchyTemplate()

    sourceSets.configureEach {
        languageSettings {
            optIn("love.forte.simbot.annotations.InternalSimbotAPI")
        }
    }

    configKotlinJvm {
        withJava()
    }
    js(IR) {
        configJs()
    }

    applyTier1()
    applyTier2()
    applyTier3(supportKtorClient = true)

    sourceSets {
        commonMain.dependencies {
            api(libs.simbot.api)
            api(libs.simbot.common.annotations)

            api(project(":simbot-component-onebot11-common"))
            api(project(":simbot-component-onebot11-message"))

            api(libs.kotlinx.coroutines.core)
            // api(libs.kotlinx.serialization.json)
            // api(libs.ktor.client.core)
            // api(libs.simbot.logger)
            // api(libs.simbot.common.suspend)
            // api(libs.ktor.client.contentNegotiation)
            // api(libs.simbot.common.atomic)
            // api(libs.simbot.common.core)
        }

        commonTest.dependencies {
            api(project(":simbot-component-onebot11-common"))

            api(libs.simbot.core)
            api(kotlin("test"))
            api(libs.ktor.client.mock)
            api(libs.kotlinx.coroutines.test)
        }

        jvmMain {
            dependencies {
                compileOnly(libs.simbot.api)
                compileOnly(libs.simbot.common.annotations)
            }
        }

        jvmTest.dependencies {
            implementation(libs.log4j.api)
            implementation(libs.log4j.core)
            implementation(libs.log4j.slf4j2)
            implementation(libs.kotlinPoet)
        }

        // jsMain.dependencies {
        //     implementation(libs.simbot.api)
        //     implementation(libs.simbot.common.annotations)
        // }
        // nativeMain.dependencies {
            // compileOnly(libs.simbot.api)
            // compileOnly(libs.simbot.common.annotations)
        // }
    }
}

// https://github.com/google/ksp/issues/963#issuecomment-1894144639
dependencies {
    kspCommonMainMetadata(project(":internal-processors:event-type-resolver-processor"))
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
