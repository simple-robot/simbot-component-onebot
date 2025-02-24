plugins {
    id("org.jetbrains.dokka")
}

childProjects.values.forEach { childProject ->
    childProject.afterEvaluate {
        if (childProject.plugins.hasPlugin(libs.plugins.dokka.get().pluginId)) {
            dokka {
                configSourceSets(childProject)
                configHtmlCustoms(childProject)
            }
            rootProject.dependencies.dokka(childProject)
        }
    }
}
