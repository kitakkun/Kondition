@file:OptIn(ExperimentalWasmDsl::class)

import org.jetbrains.kotlin.gradle.targets.js.dsl.ExperimentalWasmDsl

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.konditionLint)
    alias(libs.plugins.konditionPublish)
    alias(libs.plugins.androidLibrary)
}

kotlin {
    jvmToolchain(17)
    jvm()
    androidTarget()
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
    // linux
    linuxX64()
    linuxArm64()
}

konditionPublication {
    artifactId = "core"
}

android {
    namespace = "io.github.kitakkun.kondition.core"
    compileSdk = 35
}
