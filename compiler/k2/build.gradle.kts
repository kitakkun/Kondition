plugins {
    alias(libs.plugins.konditionCompilerModule)
    alias(libs.plugins.konditionPublish)
}

konditionPublication {
    artifactId = "compiler-k2"
}

dependencies {
    implementation(projects.compiler.common)
    compileOnly(libs.kotlin.compiler.embeddable)
}
