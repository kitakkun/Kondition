package com.kitakkun.kondition.gradle

import org.gradle.api.provider.Provider
import org.jetbrains.kotlin.gradle.plugin.KotlinCompilation
import org.jetbrains.kotlin.gradle.plugin.KotlinCompilerPluginSupportPlugin
import org.jetbrains.kotlin.gradle.plugin.SubpluginArtifact
import org.jetbrains.kotlin.gradle.plugin.SubpluginOption

class KonditionCompilerPlugin : KotlinCompilerPluginSupportPlugin {
    override fun applyToCompilation(kotlinCompilation: KotlinCompilation<*>): Provider<List<SubpluginOption>> {
        return kotlinCompilation.target.project.provider { emptyList() }
    }

    override fun getCompilerPluginId(): String = KonditionPluginConst.COMPILER_PLUGIN_ID

    override fun isApplicable(kotlinCompilation: KotlinCompilation<*>): Boolean = kotlinCompilation.target.project.extensions.getByType(KonditionExtension::class.java).enabled

    override fun getPluginArtifact(): SubpluginArtifact {
        return SubpluginArtifact(
            groupId = KonditionPluginConst.GROUP_ID,
            artifactId = "compiler-cli",
            version = KonditionPluginConst.kotlinPrefixedVersion,
        )
    }
}
