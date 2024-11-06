plugins {
    `kotlin-dsl`
}

dependencies {
    implementation(":gradle-conventions-settings")
    implementation(libs.spotless)
    implementation(libs.maven.publish)
    implementation(libs.kotlin.gradle.plugin)
}
