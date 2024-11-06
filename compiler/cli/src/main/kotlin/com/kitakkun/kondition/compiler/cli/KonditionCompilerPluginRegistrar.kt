package com.kitakkun.kondition.compiler.cli

import com.kitakkun.kondition.compiler.backend.KonditionIrGenerationExtension
import com.kitakkun.kondition.compiler.k2.KonditionFirExtensionRegistrar
import org.jetbrains.kotlin.backend.common.extensions.IrGenerationExtension
import org.jetbrains.kotlin.compiler.plugin.CompilerPluginRegistrar
import org.jetbrains.kotlin.compiler.plugin.ExperimentalCompilerApi
import org.jetbrains.kotlin.config.CompilerConfiguration
import org.jetbrains.kotlin.fir.extensions.FirExtensionRegistrarAdapter

@OptIn(ExperimentalCompilerApi::class)
class KonditionCompilerPluginRegistrar : CompilerPluginRegistrar() {
    override val supportsK2: Boolean = true

    override fun ExtensionStorage.registerExtensions(configuration: CompilerConfiguration) {
        FirExtensionRegistrarAdapter.registerExtension(KonditionFirExtensionRegistrar())
        IrGenerationExtension.registerExtension(KonditionIrGenerationExtension(configuration))
    }
}
