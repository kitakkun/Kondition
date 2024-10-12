plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.konditionLint)
    alias(libs.plugins.konditionPublish)
}

kotlin {
    jvm()

    sourceSets {
        commonMain.dependencies {
        }
    }
}

konditionPublication {
    artifactId = "core"
}
