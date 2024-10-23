plugins {
    alias(libs.plugins.kotlinMultiplatform) apply false
    alias(libs.plugins.kotlinJvm) apply false
    alias(libs.plugins.androidLibrary) apply false
    alias(libs.plugins.spotless) apply false
    alias(libs.plugins.buildconfig) apply false
    alias(libs.plugins.maven.publish) apply false
    alias(libs.plugins.konditionLint) apply false
    alias(libs.plugins.konditionPublish) apply false
}

allprojects {
    group = "io.github.kitakkun.kondition"
    version = rootProject.libs.versions.kondition.get()
}
