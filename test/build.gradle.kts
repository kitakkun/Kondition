plugins {
    alias(libs.plugins.kotlinMultiplatform)
}

kotlin {
    jvm()
}

dependencies {
    commonTestImplementation(projects.core)
    kotlinCompilerPluginClasspath(projects.compiler)

    commonTestImplementation(libs.kotlin.test)
}
