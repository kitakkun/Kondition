plugins {
    alias(libs.plugins.kotlinJvm)
    alias(libs.plugins.konditionLint)
    alias(libs.plugins.konditionPublish)
}

dependencies {
    implementation(projects.core)
    implementation(libs.kotlin.compiler.embeddable)
}

konditionPublication {
    artifactId = "compiler"
}
