package io.github.kitakkun.kondition.compiler.fir

import org.jetbrains.kotlin.fir.extensions.FirExtensionRegistrar

class KonditionFirExtensionRegistrar : FirExtensionRegistrar() {
    override fun ExtensionRegistrarContext.configurePlugin() {
        +::KonditionFirCheckersExtension
    }
}
