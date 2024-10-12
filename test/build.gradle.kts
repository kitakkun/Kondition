plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.konditionLint)
    alias(libs.plugins.androidLibrary)
}

kotlin {
    jvmToolchain(17)
    jvm()
    androidTarget()
    // linux
    linuxX64()
    linuxArm64()
    // windows
    mingwX64()
    // macos
    macosX64()
    macosArm64()
    // ios
    iosSimulatorArm64()
    iosX64()
    iosArm64()
    // watchos
    watchosSimulatorArm64()
    watchosArm32()
    watchosArm64()
    // tvos
    tvosSimulatorArm64()
    tvosX64()
    tvosArm64()
}

dependencies {
    commonTestImplementation(projects.core)
    kotlinCompilerPluginClasspath(projects.compiler)
    kotlinNativeCompilerPluginClasspath(projects.compiler)

    commonTestImplementation(libs.kotlin.test)
}

android {
    namespace = "io.github.kitakkun.kondition.test"
    compileSdk = 35
}
