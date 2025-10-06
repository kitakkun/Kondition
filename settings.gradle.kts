rootProject.name = "Kondition"

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
    includeBuild("gradle-conventions")

    repositories {
        mavenCentral()
        gradlePluginPortal()
        google()
    }
}

dependencyResolutionManagement {
    repositories {
        mavenCentral()
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version ("0.4.0")
}

include(":gradle-plugin")
include(":compiler:common")
include(":compiler:cli")
include(":compiler:k2")
include(":compiler:backend")
include(":core")
include(":compiler-test")
