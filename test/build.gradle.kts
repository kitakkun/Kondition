plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.konditionLint)
}

kotlin {
    jvm()
}

dependencies {
    commonTestImplementation(projects.core)
    kotlinCompilerPluginClasspath(projects.compiler)

    commonTestImplementation(libs.kotlin.test)
}
