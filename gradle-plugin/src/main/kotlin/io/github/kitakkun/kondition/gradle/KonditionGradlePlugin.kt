@file:Suppress("UNUSED")

package io.github.kitakkun.kondition.gradle

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.dsl.DependencyHandler
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinSingleTargetExtension
import org.jetbrains.kotlin.gradle.dsl.kotlinExtension

class KonditionGradlePlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            // access to initialize kotlinVersion (will be used later)
            KonditionPluginConst.kotlinVersion

            plugins.apply(KonditionCompilerPlugin::class.java)

            extensions.create("kondition", KonditionExtension::class.java)

            when (kotlinExtension) {
                is KotlinMultiplatformExtension -> {
                    dependencies.commonMainImplementation(KonditionPluginConst.CORE_LIBRARY_DEPENDENCY_NOTATION)
                }

                is KotlinSingleTargetExtension<*> -> {
                    dependencies.implementation(KonditionPluginConst.CORE_LIBRARY_DEPENDENCY_NOTATION)
                }
            }
        }
    }
}

private fun DependencyHandler.implementation(notation: String) {
    add("implementation", notation)
}

private fun DependencyHandler.commonMainImplementation(notation: String) {
    add("commonMainImplementation", notation)
}
