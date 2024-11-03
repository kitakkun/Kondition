pluginManagement {
    includeBuild("gradle-conventions-settings")
    includeBuild("gradle-conventions")
}

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version("0.4.0")
    id("settings-conventions")
}

include(":gradle-plugin")
include(":compiler")
include(":core")
include(":test")
include(":examples:playground")

rootProject.name = "Kondition"
