package io.github.kitakkun.kondition.compiler.backend

import io.github.kitakkun.kondition.compiler.backend.fitting.AddPrefixFitter
import io.github.kitakkun.kondition.compiler.backend.fitting.AddSuffixFitter
import io.github.kitakkun.kondition.compiler.backend.fitting.CoerceAtLeastDecimalFitter
import io.github.kitakkun.kondition.compiler.backend.fitting.CoerceAtLeastFitter
import io.github.kitakkun.kondition.compiler.backend.fitting.CoerceAtMostDecimalFitter
import io.github.kitakkun.kondition.compiler.backend.fitting.CoerceAtMostFitter
import io.github.kitakkun.kondition.compiler.backend.fitting.CoerceInDecimalFitter
import io.github.kitakkun.kondition.compiler.backend.fitting.CoerceInFitter
import io.github.kitakkun.kondition.compiler.backend.fitting.DropFitter
import io.github.kitakkun.kondition.compiler.backend.fitting.DropLastFitter
import io.github.kitakkun.kondition.compiler.backend.fitting.RemovePrefixFitter
import io.github.kitakkun.kondition.compiler.backend.fitting.RemoveSuffixFitter
import io.github.kitakkun.kondition.compiler.backend.fitting.TakeFitter
import io.github.kitakkun.kondition.compiler.backend.fitting.TakeLastFitter
import io.github.kitakkun.kondition.compiler.backend.fitting.ToLowerCaseFitter
import io.github.kitakkun.kondition.compiler.backend.fitting.ToUpperCaseFitter
import io.github.kitakkun.kondition.compiler.backend.fitting.TrimEndFitter
import io.github.kitakkun.kondition.compiler.backend.fitting.TrimFitter
import io.github.kitakkun.kondition.compiler.backend.fitting.TrimStartFitter
import io.github.kitakkun.kondition.compiler.backend.requirement.AlphabeticRequirementProvider
import io.github.kitakkun.kondition.compiler.backend.requirement.GreaterThanDecimalRequirementProvider
import io.github.kitakkun.kondition.compiler.backend.requirement.GreaterThanLongRequirementProvider
import io.github.kitakkun.kondition.compiler.backend.requirement.GreaterThanOrEqualsDecimalRequirementProvider
import io.github.kitakkun.kondition.compiler.backend.requirement.GreaterThanOrEqualsLongRequirementProvider
import io.github.kitakkun.kondition.compiler.backend.requirement.LengthRequirementProvider
import io.github.kitakkun.kondition.compiler.backend.requirement.LessThanDecimalRequirementProvider
import io.github.kitakkun.kondition.compiler.backend.requirement.LessThanLongRequirementProvider
import io.github.kitakkun.kondition.compiler.backend.requirement.LessThanOrEqualsDecimalRequirementProvider
import io.github.kitakkun.kondition.compiler.backend.requirement.LessThanOrEqualsLongRequirementProvider
import io.github.kitakkun.kondition.compiler.backend.requirement.LowerCasedRequirementProvider
import io.github.kitakkun.kondition.compiler.backend.requirement.MatchRegexRequirementProvider
import io.github.kitakkun.kondition.compiler.backend.requirement.MaxLengthRequirementProvider
import io.github.kitakkun.kondition.compiler.backend.requirement.MinLengthRequirementProvider
import io.github.kitakkun.kondition.compiler.backend.requirement.NegativeRequirementProvider
import io.github.kitakkun.kondition.compiler.backend.requirement.NonBlankRequirementProvider
import io.github.kitakkun.kondition.compiler.backend.requirement.NonEmptyRequirementProvider
import io.github.kitakkun.kondition.compiler.backend.requirement.NonNegativeRequirementProvider
import io.github.kitakkun.kondition.compiler.backend.requirement.NonPositiveRequirementProvider
import io.github.kitakkun.kondition.compiler.backend.requirement.NonZeroRequirementProvider
import io.github.kitakkun.kondition.compiler.backend.requirement.NumericRequirementProvider
import io.github.kitakkun.kondition.compiler.backend.requirement.PositiveRequirementProvider
import io.github.kitakkun.kondition.compiler.backend.requirement.PrefixedRequirementProvider
import io.github.kitakkun.kondition.compiler.backend.requirement.RangedDecimalRequirementProvider
import io.github.kitakkun.kondition.compiler.backend.requirement.RangedLongRequirementProvider
import io.github.kitakkun.kondition.compiler.backend.requirement.SuffixedRequirementProvider
import io.github.kitakkun.kondition.compiler.backend.requirement.UpperCasedRequirementProvider
import io.github.kitakkun.kondition.compiler.backend.statement.FitValueProducer
import io.github.kitakkun.kondition.compiler.backend.statement.StatementsProducer
import io.github.kitakkun.kondition.compiler.backend.transformer.LocalVariablesCheckProducer
import io.github.kitakkun.kondition.compiler.backend.transformer.ValueParameterCheckStatementsProducer
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

            PrefixedRequirementProvider(),
            SuffixedRequirementProvider(),
            UpperCasedRequirementProvider(),
            LowerCasedRequirementProvider(),

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

        val valueFitters = listOf(
            CoerceAtLeastFitter(),
            CoerceAtMostFitter(),
            CoerceInFitter(),
            CoerceAtLeastDecimalFitter(),
            CoerceAtMostDecimalFitter(),
            CoerceInDecimalFitter(),
            RemoveSuffixFitter(),
            AddSuffixFitter(),
            RemovePrefixFitter(),
            AddPrefixFitter(),
            TrimFitter(),
            TrimStartFitter(),
            TrimEndFitter(),
            TakeFitter(),
            TakeLastFitter(),
            DropFitter(),
            DropLastFitter(),
            ToUpperCaseFitter(),
            ToLowerCaseFitter(),
        )

        val statementsProducer = StatementsProducer(requirementProviders)
        val fitValueProducer = FitValueProducer(valueFitters)
        moduleFragment.transformChildrenVoid(ValueParameterCheckStatementsProducer(context, statementsProducer, fitValueProducer))
        moduleFragment.transformChildrenVoid(LocalVariablesCheckProducer(context, statementsProducer, fitValueProducer))
    }
}
