import org.jetbrains.kotlin.gradle.utils.loadPropertyFromResources

plugins {
    alias(libs.plugins.konditionCompilerModule)
    alias(libs.plugins.konditionLint)
    alias(libs.plugins.konditionPublish)
}

val kotlinVersion = object {}.loadPropertyFromResources("project.properties", "project.version")

dependencies {
    implementation(libs.kotlin.compiler.embeddable)
}

konditionPublication {
    artifactId = "compiler"
}
