plugins {
    alias(libs.plugins.kotlinJvm)
    alias(libs.plugins.konditionLint)
    alias(libs.plugins.konditionPublish)
}

dependencies {
    implementation(libs.kotlin.compiler.embeddable)
    testImplementation(libs.kotlin.test)
}

konditionPublication {
    artifactId = "compiler"
}
