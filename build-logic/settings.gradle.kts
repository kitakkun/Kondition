dependencyResolutionManagement {
    repositories {
        mavenCentral()
        google()
        gradlePluginPortal()
    }

    versionCatalogs {
        create("libs") {
            from(files("../versions-root/libs.versions.toml"))
            System.getenv("KOTLIN_VERSION")?.let {
                version("kotlin", it)
            }
        }
    }
}

include(":convention")
rootProject.name = "build-logic"
