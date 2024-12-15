package com.kitakkun.kondition.gradle

import com.autonomousapps.kit.AbstractGradleProject
import com.autonomousapps.kit.GradleBuilder.build
import com.autonomousapps.kit.GradleBuilder.buildAndFail
import com.autonomousapps.kit.Source
import com.autonomousapps.kit.gradle.Kotlin
import com.autonomousapps.kit.gradle.Plugin
import com.kitakkun.kondition.gradle_plugin.BuildConfigForTest
import org.junit.Test

class KonditionGradlePluginDisabledTest {
    @Test
    fun testEnabled() {
        val project = TestProject(enableKonditionCompiler = true).gradleProject
        buildAndFail(project.rootDir, ":run")
    }

    @Test
    fun testDisabled() {
        val project = TestProject(enableKonditionCompiler = false).gradleProject
        build(project.rootDir, ":run")
    }
}

class TestProject(
    enableKonditionCompiler: Boolean,
) : AbstractGradleProject() {
    val gradleProject = newGradleProjectBuilder()
        .withRootProject {
            withBuildScript {
                sources = listOf(
                    Source.kotlin(
                        """
                        import com.kitakkun.kondition.core.annotation.Positive

                        fun main() {
                            a(-10) // will throw error if kondition plugin is enabled
                        }

                        fun a(@Positive value: Int) {
                        }
                        """.trimIndent(),
                    ).withPath("", "main").build(),
                )
                kotlin = Kotlin.ofTarget(17)
                plugins(
                    Plugin.application,
                    Plugin("org.jetbrains.kotlin.jvm", BuildConfigForTest.KOTLIN_VERSION),
                    Plugin("com.kitakkun.kondition", BuildConfigForTest.VERSION),
                )
                withGroovy(
                    """
                    application {
                        mainClass = "MainKt"
                    }
                    
                    kondition {
                        enabled = $enableKonditionCompiler
                    }
                    """.trimIndent(),
                )
            }
        }
        .write()
}
