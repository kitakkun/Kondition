package io.github.kitakkun.kondition.compiler.ir

import io.github.kitakkun.kondition.compiler.ir.requirement.AlphabeticRequirementProvider
import io.github.kitakkun.kondition.compiler.ir.requirement.MatchRegexRequirementProvider
import io.github.kitakkun.kondition.compiler.ir.requirement.NegativeRequirementProvider
import io.github.kitakkun.kondition.compiler.ir.requirement.NonBlankRequirementProvider
import io.github.kitakkun.kondition.compiler.ir.requirement.NonEmptyRequirementProvider
import io.github.kitakkun.kondition.compiler.ir.requirement.NonNegativeRequirementProvider
import io.github.kitakkun.kondition.compiler.ir.requirement.NonPositiveRequirementProvider
import io.github.kitakkun.kondition.compiler.ir.requirement.NonZeroRequirementProvider
import io.github.kitakkun.kondition.compiler.ir.requirement.NumericRequirementProvider
import io.github.kitakkun.kondition.compiler.ir.requirement.PositiveRequirementProvider
import io.github.kitakkun.kondition.compiler.ir.requirement.RangedByteRequirementProvider
import io.github.kitakkun.kondition.compiler.ir.requirement.RangedDoubleRequirementProvider
import io.github.kitakkun.kondition.compiler.ir.requirement.RangedFloatRequirementProvider
import io.github.kitakkun.kondition.compiler.ir.requirement.RangedIntRequirementProvider
import io.github.kitakkun.kondition.compiler.ir.requirement.RangedLongRequirementProvider
import io.github.kitakkun.kondition.compiler.ir.requirement.RangedShortRequirementProvider
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
                    AlphabeticRequirementProvider(),
                    NumericRequirementProvider(),
                    // number ranges
                    RangedIntRequirementProvider(),
                    RangedFloatRequirementProvider(),
                    RangedDoubleRequirementProvider(),
                    RangedLongRequirementProvider(),
                    RangedShortRequirementProvider(),
                    RangedByteRequirementProvider(),

                    PositiveRequirementProvider(),
                    NonPositiveRequirementProvider(),
                    NegativeRequirementProvider(),
                    NonNegativeRequirementProvider(),
                    NonZeroRequirementProvider(),
                ),
            ),
        )
    }
}
