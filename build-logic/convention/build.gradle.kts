plugins {
    `kotlin-dsl`
}

gradlePlugin {
    plugins {
        register("io.github.kitakkun.kondition.conventions.lint") {
            id = "kondition.lint"
            implementationClass = "KonditionLintPlugin"
        }
        register("io.github.kitakkun.kondition.conventions.publish.jvm") {
            id = "kondition.publish.jvm"
            implementationClass = "KonditionJvmPublicationPlugin"
        }
        register("io.github.kitakkun.kondition.conventions.publish.kmp") {
            id = "kondition.publish.kmp"
            implementationClass = "KonditionKmpPublicationPlugin"
        }
    }
}

dependencies {
    compileOnly(libs.spotless)
    compileOnly(libs.maven.publish)
}
