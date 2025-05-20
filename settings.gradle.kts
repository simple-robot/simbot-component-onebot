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
pluginManagement {
    repositories {
        mavenCentral()
        gradlePluginPortal()
        mavenLocal()
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}

rootProject.name = "simbot-component-onebot"

include(":internal-processors:include-component-message-elements-processor")
include(":internal-processors:event-type-resolver-processor")

include(":simbot-component-onebot-common")

include(":simbot-component-onebot-v11:simbot-component-onebot-v11-common")
include(":simbot-component-onebot-v11:simbot-component-onebot-v11-event")
include(":simbot-component-onebot-v11:simbot-component-onebot-v11-message")
include(":simbot-component-onebot-v11:simbot-component-onebot-v11-core")
