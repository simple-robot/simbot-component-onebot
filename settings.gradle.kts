plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.5.0"
}
rootProject.name = "simbot-component-onebot"

include(":internal-processors:include-component-message-elements-processor")
include(":simbot-component-onebot11-core")
