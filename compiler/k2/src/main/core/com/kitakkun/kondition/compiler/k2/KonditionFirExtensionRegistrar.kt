package com.kitakkun.kondition.compiler.k2

import com.kitakkun.kondition.compiler.k2.extensions.KonditionInterfaceGenerationExtension
import org.jetbrains.kotlin.fir.extensions.FirExtensionRegistrar

class KonditionFirExtensionRegistrar : FirExtensionRegistrar() {
    override fun ExtensionRegistrarContext.configurePlugin() {
        VersionSpecificAPI.INSTANCE = VersionSpecificAPIImpl

        +::KonditionFirCheckersExtension
        +::KonditionInterfaceGenerationExtension
    }
}
