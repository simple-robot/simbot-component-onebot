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

import love.forte.gradle.common.core.project.setup
import love.forte.gradle.common.kotlin.multiplatform.applyTier1
import love.forte.gradle.common.kotlin.multiplatform.applyTier123
import love.forte.gradle.common.kotlin.multiplatform.applyTier2
import love.forte.gradle.common.kotlin.multiplatform.applyTier3

plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization")
    `simbot-onebot-dokka-partial-configure`
    // alias(libs.plugins.ksp)
}

setup(P.ComponentOneBot)
configJavaCompileWithModule("simbot.component.onebot11v.common")
apply(plugin = "simbot-onebot-multiplatform-maven-publish")

kotlin {
    explicitApi()
    applyDefaultHierarchyTemplate()

    configKotlinJvm()

    js(IR) {
        configJs()
    }

    applyTier123()

    sourceSets {
        commonMain.dependencies {
            implementation(project(":simbot-component-onebot-common"))
            implementation(libs.simbot.common.annotations)
            api(libs.kotlinx.serialization.core)
        }

        commonTest.dependencies {
            api(kotlin("test"))
            api(libs.kotlinx.serialization.json)
            api(libs.kotlinx.coroutines.test)
        }

        jvmMain.dependencies {
            compileOnly(libs.simbot.common.annotations)
        }

        jvmTest.dependencies {
            compileOnly(libs.simbot.common.annotations)
            implementation(libs.log4j.api)
            implementation(libs.log4j.core)
            implementation(libs.log4j.slf4j2)
        }
    }
}
