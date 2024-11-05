plugins {
    `kotlin-dsl`
    alias(libs.plugins.buildconfig)
}

buildConfig {
    buildConfigField("VERSION", libs.versions.kondition.get().toString())
}

dependencies {
    implementation(libs.spotless)
    implementation(libs.maven.publish)
    implementation(libs.kotlin.gradle.plugin)
}
