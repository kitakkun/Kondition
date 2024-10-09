package io.github.kitakkun.kondition.compiler.ir

import io.github.kitakkun.kondition.compiler.ir.transformer.MatchRegexTransformer
import io.github.kitakkun.kondition.compiler.ir.transformer.NonBlankTransformer
import io.github.kitakkun.kondition.compiler.ir.transformer.NonEmptyTransformer
import io.github.kitakkun.kondition.compiler.ir.transformer.RangedIntTransformer
import org.jetbrains.kotlin.backend.common.extensions.IrGenerationExtension
import org.jetbrains.kotlin.backend.common.extensions.IrPluginContext
import org.jetbrains.kotlin.cli.common.CLIConfigurationKeys
import org.jetbrains.kotlin.cli.common.messages.MessageCollector
import org.jetbrains.kotlin.config.CompilerConfiguration
import org.jetbrains.kotlin.ir.declarations.IrModuleFragment
import org.jetbrains.kotlin.ir.visitors.transformChildrenVoid
import kotlin.text.get

class KonditionIrGenerationExtension(configuration: CompilerConfiguration) : IrGenerationExtension {
    private val messageCollector = configuration.get(CLIConfigurationKeys.MESSAGE_COLLECTOR_KEY, MessageCollector.NONE)

    override fun generate(moduleFragment: IrModuleFragment, pluginContext: IrPluginContext) {
        val context = KonditionIrContext(pluginContext, messageCollector)
        moduleFragment.transformChildrenVoid(NonEmptyTransformer(context))
        moduleFragment.transformChildrenVoid(NonBlankTransformer(context))
        moduleFragment.transformChildrenVoid(RangedIntTransformer(context))
        moduleFragment.transformChildrenVoid(MatchRegexTransformer(context))
    }
}
