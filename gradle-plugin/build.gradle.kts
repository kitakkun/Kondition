plugins {
    alias(libs.plugins.kotlinJvm)
    alias(libs.plugins.konditionPublish)
    alias(libs.plugins.konditionLint)
    alias(libs.plugins.buildconfig)
    `java-gradle-plugin`
    groovy
}

dependencies {
    implementation(libs.kotlin.gradle.plugin.api)
    implementation(libs.kotlin.gradle.plugin)

    testImplementation(gradleTestKit())
    testImplementation("org.spockframework:spock-core:2.2-groovy-3.0") {
        exclude(group = "org.codehaus.groovy")
    }
    testImplementation(libs.junit.jupiter)
    testRuntimeOnly(libs.junit.platform.launcher)
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
    buildConfigField("VERSION", libs.versions.kondition.get())
}

tasks.test {
    useJUnitPlatform()
}
