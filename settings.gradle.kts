plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.5.0"
}
rootProject.name = "simbot-component-onebot"

include(":internal-processors:include-component-message-elements-processor")
include(":internal-processors:event-type-resolver-processor")
include(":simbot-component-onebot11-common")
include(":simbot-component-onebot11-event")
// include(":simbot-component-onebot11-api")
include(":simbot-component-onebot11-message")
include(":simbot-component-onebot11-core")
