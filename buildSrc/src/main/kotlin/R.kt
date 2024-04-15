/*
 * Copyright (c) 2023-2024. ForteScarlet.
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

import love.forte.gradle.common.core.property.systemProp
import love.forte.gradle.common.core.repository.Repositories
import love.forte.gradle.common.core.repository.SimpleCredentials
import love.forte.gradle.common.publication.SonatypeContact
import org.slf4j.LoggerFactory

val logger = LoggerFactory.getLogger("Sonatype Userinfo")

private val sonatypeUserInfo by lazy {
    val userInfo = love.forte.gradle.common.publication.sonatypeUserInfoOrNull

    val username = systemProp(SonatypeContact.SONATYPE_USERNAME) ?: return@lazy null
    val password = systemProp(SonatypeContact.SONATYPE_PASSWORD) ?: return@lazy null
    logger.info("sonatype.username: {}", username)
    logger.info("sonatype.password: {}", password)

    if (userInfo == null) {
        logger.warn("sonatype.username or sonatype.password is null, cannot config nexus publishing.")
    }
    
    userInfo
}

val sonatypeUsername: String? get() = sonatypeUserInfo?.username
val sonatypePassword: String? get() = sonatypeUserInfo?.password

val ReleaseRepository by lazy {
    Repositories.Central.Default.copy(SimpleCredentials(sonatypeUsername, sonatypePassword))
}
val SnapshotRepository by lazy {
    Repositories.Snapshot.Default.copy(SimpleCredentials(sonatypeUsername, sonatypePassword))
}
