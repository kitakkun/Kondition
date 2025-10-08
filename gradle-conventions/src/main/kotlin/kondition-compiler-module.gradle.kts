import org.jetbrains.kotlin.gradle.dsl.KotlinJvmProjectExtension

plugins {
    id("org.jetbrains.kotlin.jvm")
}

configure<KotlinJvmProjectExtension> {
    jvmToolchain(17)

    compilerOptions.freeCompilerArgs.addAll(
        "-Xcontext-parameters",
        "-opt-in=org.jetbrains.kotlin.ir.symbols.UnsafeDuringIrConstructionAPI",
    )
}
