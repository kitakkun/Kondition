pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
}

dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
    }
}

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

includeBuild("build-logic")
include(":gradle-plugin")
include(":compiler")
include(":core")
include(":test")
include(":examples:playground")

rootProject.name = "Kondition"
