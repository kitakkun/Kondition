package com.kitakkun.kondition.gradle

import com.kitakkun.kondition.gradle_plugin.BuildConfig
import org.jetbrains.kotlin.gradle.utils.loadPropertyFromResources

object KonditionPluginConst {
    const val GROUP_ID = "com.kitakkun.kondition"
    const val COMPILER_PLUGIN_ID = "kondition-compiler-plugin"
    const val CORE_LIBRARY_DEPENDENCY_NOTATION = "$GROUP_ID:core:${BuildConfig.VERSION}"

    val kotlinVersion by lazy { loadKotlinVersion() }
    val kotlinPrefixedVersion by lazy { "${kotlinVersion}-${BuildConfig.VERSION}" }

    private fun loadKotlinVersion(): String {
        return object {}.loadPropertyFromResources("project.properties", "project.version")
    }
}
