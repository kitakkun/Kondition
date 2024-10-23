package io.github.kitakkun.kondition.compiler

import VersionSpecificAPI
import io.github.kitakkun.kondition.compiler.fir.KonditionFirExtensionRegistrar
import io.github.kitakkun.kondition.compiler.ir.KonditionIrGenerationExtension
import org.jetbrains.kotlin.backend.common.extensions.IrGenerationExtension
import org.jetbrains.kotlin.compiler.plugin.CompilerPluginRegistrar
import org.jetbrains.kotlin.compiler.plugin.ExperimentalCompilerApi
import org.jetbrains.kotlin.config.CompilerConfiguration
import org.jetbrains.kotlin.fir.extensions.FirExtensionRegistrarAdapter

@OptIn(ExperimentalCompilerApi::class)
class KonditionCompilerPluginRegistrar : CompilerPluginRegistrar() {
    override val supportsK2: Boolean = true

    override fun ExtensionStorage.registerExtensions(configuration: CompilerConfiguration) {
        VersionSpecificAPI.INSTANCE = VersionSpecificAPIImpl
        FirExtensionRegistrarAdapter.registerExtension(KonditionFirExtensionRegistrar())
        IrGenerationExtension.registerExtension(KonditionIrGenerationExtension(configuration))
    }
}
