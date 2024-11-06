package com.kitakkun.kondition.compiler.k2

import org.jetbrains.kotlin.fir.extensions.FirExtensionRegistrar

class KonditionFirExtensionRegistrar : FirExtensionRegistrar() {
    override fun ExtensionRegistrarContext.configurePlugin() {
        VersionSpecificAPI.INSTANCE = VersionSpecificAPIImpl

        +::KonditionFirCheckersExtension
    }
}
