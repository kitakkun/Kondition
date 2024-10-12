plugins {
    `kotlin-dsl`
    alias(libs.plugins.buildconfig)
}

gradlePlugin {
    plugins {
        register("io.github.kitakkun.kondition.conventions.lint") {
            id = "kondition.lint"
            implementationClass = "KonditionLintPlugin"
        }
        register("io.github.kitakkun.kondition.conventions.publish") {
            id = "kondition.publish"
            implementationClass = "KonditionPublicationPlugin"
        }
    }
}

buildConfig {
    buildConfigField("VERSION", libs.versions.kondition.get().toString())
}

dependencies {
    compileOnly(libs.spotless)
    compileOnly(libs.maven.publish)
}
