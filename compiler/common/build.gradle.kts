plugins {
    alias(libs.plugins.konditionCompilerModule)
    alias(libs.plugins.konditionPublish)
}

konditionPublication {
    artifactId = "compiler-common"
}

dependencies {
    compileOnly(libs.kotlin.compiler.embeddable)
}
