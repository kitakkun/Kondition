package com.kitakkun.kondition.compiler.backend

import com.kitakkun.kondition.compiler.backend.api.VersionSpecificAPI
import com.kitakkun.kondition.compiler.backend.api.VersionSpecificAPIImpl
import com.kitakkun.kondition.compiler.backend.fitting.AddPrefixFitter
import com.kitakkun.kondition.compiler.backend.fitting.AddSuffixFitter
import com.kitakkun.kondition.compiler.backend.fitting.CoerceAtLeastDecimalFitter
import com.kitakkun.kondition.compiler.backend.fitting.CoerceAtLeastFitter
import com.kitakkun.kondition.compiler.backend.fitting.CoerceAtMostDecimalFitter
import com.kitakkun.kondition.compiler.backend.fitting.CoerceAtMostFitter
import com.kitakkun.kondition.compiler.backend.fitting.CoerceInDecimalFitter
import com.kitakkun.kondition.compiler.backend.fitting.CoerceInFitter
import com.kitakkun.kondition.compiler.backend.fitting.DropFitter
import com.kitakkun.kondition.compiler.backend.fitting.DropLastFitter
import com.kitakkun.kondition.compiler.backend.fitting.RemovePrefixFitter
import com.kitakkun.kondition.compiler.backend.fitting.RemoveSuffixFitter
import com.kitakkun.kondition.compiler.backend.fitting.TakeFitter
import com.kitakkun.kondition.compiler.backend.fitting.TakeLastFitter
import com.kitakkun.kondition.compiler.backend.fitting.ToLowerCaseFitter
import com.kitakkun.kondition.compiler.backend.fitting.ToUpperCaseFitter
import com.kitakkun.kondition.compiler.backend.fitting.TrimEndFitter
import com.kitakkun.kondition.compiler.backend.fitting.TrimFitter
import com.kitakkun.kondition.compiler.backend.fitting.TrimStartFitter
import com.kitakkun.kondition.compiler.backend.requirement.AlphabeticRequirementProvider
import com.kitakkun.kondition.compiler.backend.requirement.GreaterThanDecimalRequirementProvider
import com.kitakkun.kondition.compiler.backend.requirement.GreaterThanLongRequirementProvider
import com.kitakkun.kondition.compiler.backend.requirement.GreaterThanOrEqualsDecimalRequirementProvider
import com.kitakkun.kondition.compiler.backend.requirement.GreaterThanOrEqualsLongRequirementProvider
import com.kitakkun.kondition.compiler.backend.requirement.LengthRequirementProvider
import com.kitakkun.kondition.compiler.backend.requirement.LessThanDecimalRequirementProvider
import com.kitakkun.kondition.compiler.backend.requirement.LessThanLongRequirementProvider
import com.kitakkun.kondition.compiler.backend.requirement.LessThanOrEqualsDecimalRequirementProvider
import com.kitakkun.kondition.compiler.backend.requirement.LessThanOrEqualsLongRequirementProvider
import com.kitakkun.kondition.compiler.backend.requirement.LowerCasedRequirementProvider
import com.kitakkun.kondition.compiler.backend.requirement.MatchRegexRequirementProvider
import com.kitakkun.kondition.compiler.backend.requirement.MaxLengthRequirementProvider
import com.kitakkun.kondition.compiler.backend.requirement.MinLengthRequirementProvider
import com.kitakkun.kondition.compiler.backend.requirement.NegativeRequirementProvider
import com.kitakkun.kondition.compiler.backend.requirement.NonBlankRequirementProvider
import com.kitakkun.kondition.compiler.backend.requirement.NonEmptyRequirementProvider
import com.kitakkun.kondition.compiler.backend.requirement.NonNegativeRequirementProvider
import com.kitakkun.kondition.compiler.backend.requirement.NonPositiveRequirementProvider
import com.kitakkun.kondition.compiler.backend.requirement.NonZeroRequirementProvider
import com.kitakkun.kondition.compiler.backend.requirement.NumericRequirementProvider
import com.kitakkun.kondition.compiler.backend.requirement.PositiveRequirementProvider
import com.kitakkun.kondition.compiler.backend.requirement.PrefixedRequirementProvider
import com.kitakkun.kondition.compiler.backend.requirement.RangedDecimalRequirementProvider
import com.kitakkun.kondition.compiler.backend.requirement.RangedLongRequirementProvider
import com.kitakkun.kondition.compiler.backend.requirement.SuffixedRequirementProvider
import com.kitakkun.kondition.compiler.backend.requirement.UpperCasedRequirementProvider
import com.kitakkun.kondition.compiler.backend.statement.FitValueProducer
import com.kitakkun.kondition.compiler.backend.statement.StatementsProducer
import com.kitakkun.kondition.compiler.backend.transformer.LocalVariablesCheckProducer
import com.kitakkun.kondition.compiler.backend.transformer.ValueParameterCheckStatementsProducer
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
        VersionSpecificAPI.INSTANCE = VersionSpecificAPIImpl
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
