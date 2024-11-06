plugins {
    alias(libs.plugins.konditionCompilerModule)
    alias(libs.plugins.konditionPublish)
}

konditionPublication {
    artifactId = "compiler-backend"
}

dependencies {
    implementation(projects.compiler.common)
    implementation(libs.kotlin.compiler.embeddable)
}
