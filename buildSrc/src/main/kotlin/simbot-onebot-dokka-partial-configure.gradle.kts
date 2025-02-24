// /*
//  * Copyright (c) 2023-2024. ForteScarlet.
//  *
//  * This file is part of simbot-component-onebot.
//  *
//  * simbot-component-onebot is free software: you can redistribute it and/or modify it under the terms
//  * of the GNU Lesser General Public License as published by the Free Software Foundation,
//  * either version 3 of the License, or (at your option) any later version.
//  *
//  * simbot-component-onebot is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
//  * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
//  * See the GNU Lesser General Public License for more details.
//  *
//  * You should have received a copy of the GNU Lesser General Public License along with simbot-component-onebot.
//  * If not, see <https://www.gnu.org/licenses/>.
//  */
//
// import org.jetbrains.dokka.DokkaConfiguration
// import org.jetbrains.dokka.base.DokkaBase
// import org.jetbrains.dokka.base.DokkaBaseConfiguration
// import org.jetbrains.dokka.gradle.DokkaTaskPartial
// import java.net.URI
// import java.time.Year
//
// plugins {
//     id("org.jetbrains.dokka")
// }
//
//
// // dokka config
// @Suppress("MaxLineLength")
// tasks.withType<DokkaTaskPartial>().configureEach {
//     pluginConfiguration<DokkaBase, DokkaBaseConfiguration> {
//         customAssets = listOf(
//             rootProject.file(".simbot/dokka-assets/logo-icon.svg"),
//             rootProject.file(".simbot/dokka-assets/logo-icon-light.svg"),
//         )
//         customStyleSheets = listOf(rootProject.file(".simbot/dokka-assets/css/kdoc-style.css"))
//         if (!isSimbotLocal()) {
//             templatesDir = rootProject.file(".simbot/dokka-templates")
//         }
//         val now = Year.now().value
//         val yearRange = if (now == 2024) "2024" else "2024-$now"
//         footerMessage = "© $yearRange <a href='https://github.com/simple-robot'>Simple Robot</a>. All rights reserved."
//         separateInheritedMembers = true
//         mergeImplicitExpectActualDeclarations = true
//         homepageLink = P.ComponentOneBot.HOMEPAGE
//     }
//
//     if (isSimbotLocal()) {
//         offlineMode.set(true)
//     }
//
//     dokkaSourceSets.configureEach {
//         version = P.ComponentOneBot.version
//         documentedVisibilities.set(
//             listOf(
//                 DokkaConfiguration.Visibility.PUBLIC,
//                 DokkaConfiguration.Visibility.PROTECTED
//             )
//         )
//         fun checkModule(projectFileName: String): Boolean {
//             val moduleMdFile = project.file(projectFileName)
//             if (moduleMdFile.exists()) {
//                 moduleMdFile.useLines { lines ->
//                     val head = lines.first { it.isNotBlank() }.trim()
//                     if (head == "# Module ${project.name}") {
//                         includes.from(projectFileName)
//                         return true
//                     }
//                 }
//             }
//
//             return false
//         }
//
//         if (!checkModule("Module.md")) {
//             checkModule("README.md")
//         }
//
//         sourceLink {
//             localDirectory.set(projectDir.resolve("src"))
//             val relativeTo = projectDir.relativeTo(rootProject.projectDir)
//                 .path
//                 .replace('\\', '/')
//
//             val uri = URI.create(
//                 "${P.ComponentOneBot.HOMEPAGE}/tree/dev/main/${relativeTo}/src/"
//             )
//
//             logger.info("Dokka source link URI: {}", uri)
//
//             // remoteUrl.set(URI.create("${P.ComponentOneBot.HOMEPAGE}/tree/dev/main/$relativeTo/src/").toURL())
//             remoteUrl.set(uri.toURL())
//             remoteLineSuffix.set("#L")
//         }
//
//         perPackageOption {
//             matchingRegex.set(".*internal.*") // will match all .internal packages and sub-packages
//             suppress.set(true)
//         }
//
//         fun externalDocumentation(docUri: URI) {
//             externalDocumentationLink {
//                 url.set(docUri.toURL())
//                 packageListUrl.set(docUri.resolve("package-list").toURL())
//             }
//         }
//
//         if (!isSimbotLocal()) {
//             // kotlin-coroutines doc
//             externalDocumentation(URI.create("https://kotlinlang.org/api/kotlinx.coroutines/"))
//
//             // kotlin-serialization doc
//             externalDocumentation(URI.create("https://kotlinlang.org/api/kotlinx.serialization/"))
//
//             // ktor
//             externalDocumentation(URI.create("https://api.ktor.io/"))
//
//             // simbot doc
//             externalDocumentation(URI.create("https://docs.simbot.forte.love/main-v4/"))
//         }
//     }
// }
