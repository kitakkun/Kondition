plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.konditionLint)
    alias(libs.plugins.konditionKmpPublish)
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
