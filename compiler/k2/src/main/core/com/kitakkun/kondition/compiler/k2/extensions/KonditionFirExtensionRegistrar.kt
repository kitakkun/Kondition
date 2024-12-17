package com.kitakkun.kondition.compiler.k2.extensions

import com.kitakkun.kondition.compiler.k2.api.VersionSpecificAPI
import com.kitakkun.kondition.compiler.k2.api.VersionSpecificAPIImpl
import org.jetbrains.kotlin.fir.extensions.FirExtensionRegistrar

class KonditionFirExtensionRegistrar : FirExtensionRegistrar() {
    override fun ExtensionRegistrarContext.configurePlugin() {
        VersionSpecificAPI.INSTANCE = VersionSpecificAPIImpl

        +::KonditionFirCheckersExtension
        +::KonditionInterfaceGenerationExtension
    }
}
