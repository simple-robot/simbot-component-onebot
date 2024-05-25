import love.forte.gradle.common.kotlin.multiplatform.applyTier1
import love.forte.gradle.common.kotlin.multiplatform.applyTier2
import love.forte.gradle.common.kotlin.multiplatform.applyTier3

plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization")

    // alias(libs.plugins.ksp)
}

useK2()
configJavaCompileWithModule("simbot.component.onebot11.common")
// apply(plugin = "simbot-onebot-multiplatform-maven-publish")

kotlin {
    explicitApi()
    applyDefaultHierarchyTemplate()

    configKotlinJvm {
        withJava()
    }
    js(IR) {
        configJs()
    }

    applyTier1()
    applyTier2()
    applyTier3()

    sourceSets {
        commonMain.dependencies {
            api(libs.kotlinx.serialization.core)
        }

        commonTest.dependencies {
            api(kotlin("test"))
            api(libs.kotlinx.serialization.json)
            api(libs.ktor.client.mock)
            api(libs.kotlinx.coroutines.test)
        }

        jvmTest.dependencies {
            implementation(libs.log4j.api)
            implementation(libs.log4j.core)
            implementation(libs.log4j.slf4j2)
        }
    }
}
