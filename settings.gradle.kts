pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}
rootProject.name = "Compose Launcher"
include(":sample")
include(":launcher")
include(":launcher-annotations")
include(":launcher-processor")
include(":test")
include(":sample-features:feature-x")
