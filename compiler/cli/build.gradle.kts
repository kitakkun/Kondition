plugins {
    alias(libs.plugins.konditionCompilerModule)
    alias(libs.plugins.konditionPublish)
}

konditionPublication {
    artifactId = "compiler-cli"
}

dependencies {
    implementation(projects.compiler.k2)
    implementation(projects.compiler.backend)
    compileOnly(libs.kotlin.compiler.embeddable)
}
