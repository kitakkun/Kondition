package com.kitakkun.kondition.compiler.k2.extensions

import org.jetbrains.kotlin.fir.extensions.FirExtensionRegistrar

class KonditionFirExtensionRegistrar : FirExtensionRegistrar() {
    override fun ExtensionRegistrarContext.configurePlugin() {
        +::KonditionFirCheckersExtension
    }
}
