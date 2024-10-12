package io.github.kitakkun.kondition.gradle

import org.gradle.testkit.runner.GradleRunner
import org.gradle.testkit.runner.TaskOutcome
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import java.io.File

/**
 * FIXME: almost the same as [KonditionGradlePluginTest].
 * should be refactored to reduce boilerplate.
 */
class KonditionGradlePluginDisabledTest {
    @TempDir
    private lateinit var testProjectDir: File
    private lateinit var settingsFile: File
    private lateinit var buildFile: File
    private lateinit var sourceDir: File
    private lateinit var sourceFile: File

    @BeforeEach
    fun setup() {
        settingsFile = File(testProjectDir, "settings.gradle.kts")
        buildFile = File(testProjectDir, "build.gradle.kts")
        sourceDir = File(testProjectDir, "src/main/kotlin")
        sourceDir.mkdirs()
        sourceFile = File(sourceDir, "Main.kt")
    }

    @Test
    fun test() {
        settingsFile.writeText(
            """
            pluginManagement {
                repositories {
                    mavenCentral()
                }
            }

            dependencyResolutionManagement {
                repositories {
                    mavenLocal() // should be prioritized to use local version.
                    mavenCentral()
                }
            }

            rootProject.name = "sandbox"
            """.trimIndent()
        )
        buildFile.writeText(
            """
            plugins {
                id("org.jetbrains.kotlin.jvm") version "2.0.0"
                id("io.github.kitakkun.kondition")
                application
            }

            kondition {
                enabled = false
            }
            
            application {
                mainClass = "MainKt"
            }
            """.trimIndent()
        )
        sourceFile.writeText(
            """
            import io.github.kitakkun.kondition.core.annotation.Positive
            
            fun main() {
                a(-10) // will throw error if kondition plugin is enabled
            }
            
            fun a(@Positive value: Int) {
            }
        """.trimIndent()
        )

        val result = GradleRunner.create()
            .withProjectDir(testProjectDir)
            .withArguments("run")
            .withPluginClasspath()
            .run()

        println(result.output)
        Assertions.assertEquals(TaskOutcome.SUCCESS, result.task(":run")?.outcome)
    }
}
