import io.gitlab.arturbosch.detekt.Detekt
import love.forte.gradle.common.core.project.setup
import love.forte.gradle.common.core.repository.Repositories
import util.isCi

plugins {
    idea
    alias(libs.plugins.detekt)
}

group = "love.forte.simbot.component"
version = "0.0.1"

setup(P.ComponentOneBot)

buildscript {
    repositories {
        mavenCentral()
    }
}


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

// detekt
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
