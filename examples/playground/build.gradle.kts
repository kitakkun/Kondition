plugins {
    alias(libs.plugins.kotlinMultiplatform)
}

kotlin {
    jvm()
}

dependencies {
    commonMainImplementation("io.github.kitakkun.kondition:core:0.0.0-alpha01")
    kotlinCompilerPluginClasspath("io.github.kitakkun.kondition:compiler:0.0.0-alpha01")
}
