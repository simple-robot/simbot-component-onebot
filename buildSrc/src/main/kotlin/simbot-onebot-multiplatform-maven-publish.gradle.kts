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
import love.forte.gradle.common.core.property.ofIf
import org.gradle.internal.impldep.org.bouncycastle.asn1.x509.X509ObjectIdentifiers.organization

plugins {
    signing
    // https://www.jetbrains.com/help/kotlin-multiplatform-dev/multiplatform-publish-libraries.html#configure-the-project
    id("com.vanniktech.maven.publish")
    id("org.jetbrains.dokka")
}

setup(P.ComponentOneBot)

val p = project

mavenPublishing {
    publishToMavenCentral(automaticRelease = true)
    if (!isSimbotLocal()) {
        signAllPublications()
    }

    pom {
        name = p.name
        description = p.description
        url = P.ComponentOneBot.HOMEPAGE
        licenses {
            P.ComponentOneBot.licenses.forEach { license ->
                license {
                    name ofIf license.name
                    url ofIf license.url
                    distribution ofIf license.distribution
                    comments ofIf license.comments
                }
            }
        }

        val scm = P.ComponentOneBot.scm
        scm {
            url ofIf scm.url
            connection ofIf scm.connection
            developerConnection ofIf scm.developerConnection
            tag ofIf scm.tag
        }

        developers {
            P.ComponentOneBot.developers.forEach { developer ->
                developer {
                    id ofIf developer.id
                    name ofIf developer.name
                    email ofIf developer.email
                    url ofIf developer.url
                    organization ofIf developer.organization
                    organizationUrl ofIf developer.organizationUrl
                    timezone ofIf developer.timezone
                    roles.addAll(developer.roles)
                    properties.putAll(developer.properties)
                }
            }
        }
    }
}

// TODO see https://github.com/gradle-nexus/publish-plugin/issues/208#issuecomment-1465029831
// val signingTasks: TaskCollection<Sign> = tasks.withType<Sign>()
// tasks.withType<PublishToMavenRepository>().configureEach {
//     mustRunAfter(signingTasks)
// }
// TODO see https://github.com/gradle/gradle/issues/26132
// Resolves issues with .asc task output of the sign task of native targets.
// See: https://github.com/gradle/gradle/issues/26132
// And: https://youtrack.jetbrains.com/issue/KT-46466
// tasks.withType<Sign>().configureEach {
//     val pubName = name.removePrefix("sign").removeSuffix("Publication")
//     logger.info("config Sign with pubName {}", pubName)
//
//     // These tasks only exist for native targets, hence findByName() to avoid trying to find them for other targets
//
//     // Task ':linkDebugTest<platform>' uses this output of task ':sign<platform>Publication' without declaring an explicit or implicit dependency
//     tasks.findByName("linkDebugTest$pubName")?.let {
//         mustRunAfter(it)
//     }
//     // Task ':compileTestKotlin<platform>' uses this output of task ':sign<platform>Publication' without declaring an explicit or implicit dependency
//     tasks.findByName("compileTestKotlin$pubName")?.let {
//         mustRunAfter(it)
//     }
//
//     logger.info("linkDebugTest{}", pubName)
//     logger.info("compileTestKotlin{}", pubName)
// }
