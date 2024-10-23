import org.jetbrains.kotlin.gradle.utils.loadPropertyFromResources

plugins {
    alias(libs.plugins.kotlinJvm)
    alias(libs.plugins.konditionLint)
    alias(libs.plugins.konditionPublish)
}

val kotlinVersion = object {}.loadPropertyFromResources("project.properties", "project.version")

kotlin {
    sourceSets {
        main {
            val kotlinVersion = kotlinVersion.replace(".", "_")
            kotlin.setSrcDirs(kotlin.srcDirs + file("src/main/v$kotlinVersion"))
        }
    }
}

dependencies {
    implementation(libs.kotlin.compiler.embeddable)
}

konditionPublication {
    artifactId = "compiler"
}

version = "${kotlinVersion}-${libs.versions.kondition.get()}"
