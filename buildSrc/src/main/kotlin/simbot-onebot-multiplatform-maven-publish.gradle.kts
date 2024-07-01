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


import love.forte.gradle.common.core.Gpg
import love.forte.gradle.common.core.project.setup
import love.forte.gradle.common.core.property.systemProp
import love.forte.gradle.common.publication.configure.configPublishMaven
import love.forte.gradle.common.publication.configure.multiplatformConfigPublishing
import love.forte.gradle.common.publication.configure.publishingExtension
import love.forte.gradle.common.publication.configure.setupPom

plugins {
    signing
    `maven-publish`
}

setup(P.ComponentOneBot)

val p = project
val isSnapshot = project.version.toString().contains("SNAPSHOT", true)

val jarJavadoc by tasks.registering(Jar::class) {
    group = "documentation"
    archiveClassifier.set("javadoc")
    if (!(isSnapshot || isSnapshot() || isSimbotLocal())) {
        dependsOn(tasks.dokkaHtml)
        from(tasks.dokkaHtml.flatMap { it.outputDirectory })
        // from(tasks.findByName("dokkaHtml"))
    }
}

publishing {
    repositories {
        mavenLocal()
        if (isSnapshot) {
            configPublishMaven(SnapshotRepository)
        } else {
            configPublishMaven(ReleaseRepository)
        }
    }

    publications {
        withType<MavenPublication> {
            artifacts {
                artifact(jarJavadoc)
            }

            setupPom(project.name, P.ComponentOneBot)
        }
    }
}

signing {
    val gpg = Gpg.ofSystemPropOrNull() ?: return@signing
    val (keyId, secretKey, password) = gpg
    useInMemoryPgpKeys(keyId, secretKey, password)
    sign(publishingExtension.publications)
}


// multiplatformConfigPublishing {
//     project = P.ComponentOneBot
//     isSnapshot = project.version.toString().contains("SNAPSHOT", true)
//     releasesRepository = ReleaseRepository
//     snapshotRepository = SnapshotRepository
//     gpg = Gpg.ofSystemPropOrNull()
//
//     // publishing {
//     //     publications.withType<MavenPublication> {
//     //         val dokkaJar = p.tasks.register("${name}DokkaJar", Jar::class) {
//     //             group = JavaBasePlugin.DOCUMENTATION_GROUP
//     //             description = "Assembles Kotlin docs with Dokka into a Javadoc jar"
//     //             archiveClassifier.set("javadoc")
//     //             from(tasks.named("dokkaHtml"))
//     //
//     //             // Each archive name should be distinct, to avoid implicit dependency issues.
//     //             // We use the same format as the sources Jar tasks.
//     //             // https://youtrack.jetbrains.com/issue/KT-46466
//     //             archiveBaseName.set("${archiveBaseName.get()}-${name}")
//     //         }
//     //         artifact(dokkaJar)
//     //     }
//     // }
//
//     // val jarJavadoc by tasks.registering(Jar::class) {
//     //     group = "documentation"
//     //     archiveClassifier.set("javadoc")
//     //     if (!(isSnapshot || isSnapshot() || isSimbotLocal())) {
//     //         dependsOn(tasks.dokkaHtml)
//     //         from(tasks.dokkaHtml.flatMap { it.outputDirectory })
//     //         // from(tasks.findByName("dokkaHtml"))
//     //     }
//     // }
//
//
//     // val dokkaJar = p.tasks.register("${publication.name}DokkaJar", Jar::class) {
//     //     group = JavaBasePlugin.DOCUMENTATION_GROUP
//     //     description = "Assembles Kotlin docs with Dokka into a Javadoc jar"
//     //     archiveClassifier.set("javadoc")
//     //     from(tasks.named("dokkaHtml"))
//     //
//     //     // Each archive name should be distinct, to avoid implicit dependency issues.
//     //     // We use the same format as the sources Jar tasks.
//     //     // https://youtrack.jetbrains.com/issue/KT-46466
//     //     archiveBaseName.set("${archiveBaseName.get()}-${publication.name}")
//     // }
//
//     artifact(jarJavadoc)
//
//     if (isSimbotLocal()) {
//         logger.info("Is 'SIMBOT_LOCAL', mainHost set as null")
//         mainHost = null
//     }
// }

// TODO see https://github.com/gradle-nexus/publish-plugin/issues/208#issuecomment-1465029831
val signingTasks: TaskCollection<Sign> = tasks.withType<Sign>()
tasks.withType<PublishToMavenRepository>().configureEach {
    mustRunAfter(signingTasks)
}
// TODO see https://github.com/gradle/gradle/issues/26132
// Resolves issues with .asc task output of the sign task of native targets.
// See: https://github.com/gradle/gradle/issues/26132
// And: https://youtrack.jetbrains.com/issue/KT-46466
tasks.withType<Sign>().configureEach {
    val pubName = name.removePrefix("sign").removeSuffix("Publication")
    logger.info("config Sign with pubName {}", pubName)

    // These tasks only exist for native targets, hence findByName() to avoid trying to find them for other targets

    // Task ':linkDebugTest<platform>' uses this output of task ':sign<platform>Publication' without declaring an explicit or implicit dependency
    tasks.findByName("linkDebugTest$pubName")?.let {
        mustRunAfter(it)
    }
    // Task ':compileTestKotlin<platform>' uses this output of task ':sign<platform>Publication' without declaring an explicit or implicit dependency
    tasks.findByName("compileTestKotlin$pubName")?.let {
        mustRunAfter(it)
    }

    logger.info("linkDebugTest{}", pubName)
    logger.info("compileTestKotlin{}", pubName)
}

show()

fun show() {
    // // show project info
    logger.info(
        """
        |=======================================================
        |= project.group:       {}
        |= project.name:        {}
        |= project.version:     {}
        |= project.description: {}
        |= os.name:             {}
        |=======================================================
        """.trimIndent(),
        group,
        name,
        version,
        description,
        systemProp("os.name")
    )
}


inline val Project.sourceSets: SourceSetContainer
    get() = extensions.getByName("sourceSets") as SourceSetContainer

internal val TaskContainer.dokkaHtml: TaskProvider<org.jetbrains.dokka.gradle.DokkaTask>
    get() = named<org.jetbrains.dokka.gradle.DokkaTask>("dokkaHtml")
