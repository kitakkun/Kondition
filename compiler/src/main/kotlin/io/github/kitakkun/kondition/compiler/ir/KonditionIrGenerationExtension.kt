package io.github.kitakkun.kondition.compiler.ir

import io.github.kitakkun.kondition.compiler.ir.requirement.MatchRegexRequirementProvider
import io.github.kitakkun.kondition.compiler.ir.requirement.NonBlankRequirementProvider
import io.github.kitakkun.kondition.compiler.ir.requirement.NonEmptyRequirementProvider
import io.github.kitakkun.kondition.compiler.ir.requirement.RangedIntRequirementProvider
import io.github.kitakkun.kondition.compiler.ir.transformer.ValueParameterCheckStatementsProducer
import org.jetbrains.kotlin.backend.common.extensions.IrGenerationExtension
import org.jetbrains.kotlin.backend.common.extensions.IrPluginContext
import org.jetbrains.kotlin.cli.common.CLIConfigurationKeys
import org.jetbrains.kotlin.cli.common.messages.MessageCollector
import org.jetbrains.kotlin.config.CompilerConfiguration
import org.jetbrains.kotlin.ir.declarations.IrModuleFragment
import org.jetbrains.kotlin.ir.visitors.transformChildrenVoid

class KonditionIrGenerationExtension(configuration: CompilerConfiguration) : IrGenerationExtension {
    private val messageCollector = configuration.get(CLIConfigurationKeys.MESSAGE_COLLECTOR_KEY, MessageCollector.NONE)

    override fun generate(moduleFragment: IrModuleFragment, pluginContext: IrPluginContext) {
        val context = KonditionIrContext(pluginContext, messageCollector)
        moduleFragment.transformChildrenVoid(
            ValueParameterCheckStatementsProducer(
                irContext = context,
                requirements = listOf(
                    NonEmptyRequirementProvider(),
                    NonBlankRequirementProvider(),
                    MatchRegexRequirementProvider(),
                    RangedIntRequirementProvider(),
                ),
            ),
        )
    }
}
