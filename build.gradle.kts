plugins {
    alias(libs.plugins.kotlinMultiplatform) apply false
    alias(libs.plugins.kotlinJvm) apply false
    alias(libs.plugins.spotless) apply false
    alias(libs.plugins.maven.publish) apply false
    alias(libs.plugins.konditionLint) apply false
    alias(libs.plugins.konditionKmpPublish) apply false
    alias(libs.plugins.konditionJvmPublish) apply false
}
