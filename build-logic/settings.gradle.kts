dependencyResolutionManagement {
    repositories {
        mavenCentral()
        google()
        gradlePluginPortal()
    }

    versionCatalogs {
        create("libs").from(files("../gradle/libs.versions.toml"))
    }
}


include(":convention")
rootProject.name = "build-logic"
