@file:Suppress("UNUSED")

package io.github.kitakkun.kondition.gradle

import Kondition.gradle_plugin.BuildConfig
import org.gradle.api.Project
import org.gradle.api.artifacts.dsl.DependencyHandler
import org.gradle.api.provider.Provider
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinSingleTargetExtension
import org.jetbrains.kotlin.gradle.dsl.kotlinExtension
import org.jetbrains.kotlin.gradle.plugin.KotlinCompilation
import org.jetbrains.kotlin.gradle.plugin.KotlinCompilerPluginSupportPlugin
import org.jetbrains.kotlin.gradle.plugin.SubpluginArtifact
import org.jetbrains.kotlin.gradle.plugin.SubpluginOption

class KonditionGradlePlugin : KotlinCompilerPluginSupportPlugin {
    companion object {
        const val GROUP_ID = "io.github.kitakkun.kondition"
        const val CORE_LIBRARY_DEPENDENCY_NOTATION = "$GROUP_ID:core:${BuildConfig.VERSION}"
    }

    override fun apply(target: Project) {
        with(target) {
            extensions.create("kondition", KonditionExtension::class.java)
            when (kotlinExtension) {
                is KotlinMultiplatformExtension -> {
                    dependencies.commonMainKotlinCompilerPluginClasspath(CORE_LIBRARY_DEPENDENCY_NOTATION)
                }

                is KotlinSingleTargetExtension<*> -> {
                    dependencies.implementation(CORE_LIBRARY_DEPENDENCY_NOTATION)
                }
            }
        }
    }

    override fun applyToCompilation(kotlinCompilation: KotlinCompilation<*>): Provider<List<SubpluginOption>> = kotlinCompilation.target.project.provider { emptyList() }

    override fun getCompilerPluginId(): String = "io.github.kitakkun.kondition.compiler"

    override fun getPluginArtifact(): SubpluginArtifact = SubpluginArtifact(
        groupId = "io.github.kitakkun.kondition",
        artifactId = "compiler",
        version = BuildConfig.VERSION,
    )

    override fun isApplicable(kotlinCompilation: KotlinCompilation<*>): Boolean =
        kotlinCompilation.target.project.extensions.getByType(KonditionExtension::class.java).enabled
}

private fun DependencyHandler.implementation(notation: String) {
    add("implementation", notation)
}

private fun DependencyHandler.commonMainKotlinCompilerPluginClasspath(notation: String) {
    add("commonMainImplementation", notation)
}
