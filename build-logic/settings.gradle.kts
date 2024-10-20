dependencyResolutionManagement {
    repositories {
        mavenCentral()
        google()
        gradlePluginPortal()
    }

    versionCatalogs {
        create("libs").from(files("../versions-root/libs.versions.toml"))
    }
}

include(":convention")
rootProject.name = "build-logic"
