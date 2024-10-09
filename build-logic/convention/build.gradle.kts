plugins {
    `kotlin-dsl`
}

gradlePlugin {
    plugins {
        register("io.github.kitakkun.kondition.conventions.lint") {
            id = "kondition.lint"
            implementationClass = "KonditionLintPlugin"
        }
    }
}

dependencies {
    compileOnly(libs.spotless)
}
