plugins {
    alias(libs.plugins.kotlinJvm)
    alias(libs.plugins.konditionLint)
}

dependencies {
    implementation(projects.core)
    implementation(libs.kotlin.compiler.embeddable)
}
