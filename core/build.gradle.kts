plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.konditionLint)
}

kotlin {
    jvm()

    sourceSets {
        commonMain.dependencies {
        }
    }
}
