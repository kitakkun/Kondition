plugins {
    `kotlin-dsl`
}

dependencies {
    implementation(libs.spotless)
    implementation(libs.maven.publish)
    implementation(libs.kotlin.gradle.plugin)
}
