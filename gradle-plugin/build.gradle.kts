plugins {
    alias(libs.plugins.kotlinJvm)
    alias(libs.plugins.konditionPublish)
    alias(libs.plugins.konditionLint)
    alias(libs.plugins.buildconfig)
    alias(libs.plugins.gradleTestKitSupport)
    `java-gradle-plugin`
}

dependencies {
    implementation(libs.kotlin.gradle.plugin.api)
    implementation(libs.kotlin.gradle.plugin)

    functionalTestImplementation(libs.gradle.testkit.support)
    functionalTestImplementation(libs.gradle.testkit.truth)
    functionalTestImplementation(libs.kotlin.test.junit)
}

gradlePlugin {
    plugins {
        create("kondition") {
            id = "com.kitakkun.kondition"
            implementationClass = "com.kitakkun.kondition.gradle.KonditionGradlePlugin"
        }
    }
}

konditionPublication {
    artifactId = "gradle-plugin"
}

buildConfig {
    generateAtSync.set(true)
    useKotlinOutput()
    sourceSets.getByName("functionalTest") {
        className("BuildConfigForTest")
        buildConfigField("VERSION", libs.versions.kondition.get())
        buildConfigField("KOTLIN_VERSION", libs.versions.kotlin.get())
    }
    buildConfigField("VERSION", libs.versions.kondition.get())
}

tasks.test {
    useJUnitPlatform()
}
