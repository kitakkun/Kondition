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
    versionCatalogs {
        create("libs") {
            from(files("$rootDir/versions-root/libs.versions.toml"))
            System.getenv("KOTLIN_VERSION")?.let {
                version("kotlin", it)
            }
        }
    }
}

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version("0.4.0")
}

includeBuild("build-logic")
include(":gradle-plugin")
include(":compiler")
include(":core")
include(":test")
include(":examples:playground")

rootProject.name = "Kondition"
