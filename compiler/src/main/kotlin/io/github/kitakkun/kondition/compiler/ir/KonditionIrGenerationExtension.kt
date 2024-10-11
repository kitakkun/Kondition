package io.github.kitakkun.kondition.compiler.ir

import io.github.kitakkun.kondition.compiler.ir.requirement.AlphabeticRequirementProvider
import io.github.kitakkun.kondition.compiler.ir.requirement.GreaterThanDecimalRequirementProvider
import io.github.kitakkun.kondition.compiler.ir.requirement.GreaterThanLongRequirementProvider
import io.github.kitakkun.kondition.compiler.ir.requirement.GreaterThanOrEqualsDecimalRequirementProvider
import io.github.kitakkun.kondition.compiler.ir.requirement.GreaterThanOrEqualsLongRequirementProvider
import io.github.kitakkun.kondition.compiler.ir.requirement.LengthRequirementProvider
import io.github.kitakkun.kondition.compiler.ir.requirement.LessThanDecimalRequirementProvider
import io.github.kitakkun.kondition.compiler.ir.requirement.LessThanLongRequirementProvider
import io.github.kitakkun.kondition.compiler.ir.requirement.LessThanOrEqualsDecimalRequirementProvider
import io.github.kitakkun.kondition.compiler.ir.requirement.LessThanOrEqualsLongRequirementProvider
import io.github.kitakkun.kondition.compiler.ir.requirement.MatchRegexRequirementProvider
import io.github.kitakkun.kondition.compiler.ir.requirement.MaxLengthRequirementProvider
import io.github.kitakkun.kondition.compiler.ir.requirement.MinLengthRequirementProvider
import io.github.kitakkun.kondition.compiler.ir.requirement.NegativeRequirementProvider
import io.github.kitakkun.kondition.compiler.ir.requirement.NonBlankRequirementProvider
import io.github.kitakkun.kondition.compiler.ir.requirement.NonEmptyRequirementProvider
import io.github.kitakkun.kondition.compiler.ir.requirement.NonNegativeRequirementProvider
import io.github.kitakkun.kondition.compiler.ir.requirement.NonPositiveRequirementProvider
import io.github.kitakkun.kondition.compiler.ir.requirement.NonZeroRequirementProvider
import io.github.kitakkun.kondition.compiler.ir.requirement.NumericRequirementProvider
import io.github.kitakkun.kondition.compiler.ir.requirement.PositiveRequirementProvider
import io.github.kitakkun.kondition.compiler.ir.requirement.RangedDecimalRequirementProvider
import io.github.kitakkun.kondition.compiler.ir.requirement.RangedLongRequirementProvider
import io.github.kitakkun.kondition.compiler.ir.statement.StatementsProducer
import io.github.kitakkun.kondition.compiler.ir.transformer.LocalVariablesCheckProducer
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

        val requirementProviders = listOf(
            NonEmptyRequirementProvider(),
            NonBlankRequirementProvider(),
            MatchRegexRequirementProvider(),
            AlphabeticRequirementProvider(),
            NumericRequirementProvider(),

            LengthRequirementProvider(),
            MinLengthRequirementProvider(),
            MaxLengthRequirementProvider(),

            // number ranges
            RangedLongRequirementProvider(),
            RangedDecimalRequirementProvider(),

            GreaterThanLongRequirementProvider(),
            GreaterThanDecimalRequirementProvider(),
            GreaterThanOrEqualsLongRequirementProvider(),
            GreaterThanOrEqualsDecimalRequirementProvider(),
            LessThanLongRequirementProvider(),
            LessThanDecimalRequirementProvider(),
            LessThanOrEqualsLongRequirementProvider(),
            LessThanOrEqualsDecimalRequirementProvider(),

            PositiveRequirementProvider(),
            NonPositiveRequirementProvider(),
            NegativeRequirementProvider(),
            NonNegativeRequirementProvider(),
            NonZeroRequirementProvider(),
        )

        val statementsProducer = StatementsProducer(requirementProviders)
        moduleFragment.transformChildrenVoid(ValueParameterCheckStatementsProducer(context, statementsProducer))
        moduleFragment.transformChildrenVoid(LocalVariablesCheckProducer(context, statementsProducer))
    }
}
