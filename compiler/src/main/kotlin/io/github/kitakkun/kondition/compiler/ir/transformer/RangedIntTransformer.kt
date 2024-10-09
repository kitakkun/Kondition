package io.github.kitakkun.kondition.compiler.ir.transformer

import io.github.kitakkun.kondition.compiler.ir.KonditionIrContext
import io.github.kitakkun.kondition.core.annotation.RangeRule
import org.jetbrains.kotlin.backend.jvm.ir.getIntConstArgument
import org.jetbrains.kotlin.ir.IrStatement
import org.jetbrains.kotlin.ir.builders.IrBuilderWithScope
import org.jetbrains.kotlin.ir.builders.irCall
import org.jetbrains.kotlin.ir.builders.irGet
import org.jetbrains.kotlin.ir.builders.irInt
import org.jetbrains.kotlin.ir.declarations.IrFunction
import org.jetbrains.kotlin.ir.declarations.IrValueParameter
import org.jetbrains.kotlin.ir.expressions.IrConstructorCall
import org.jetbrains.kotlin.ir.expressions.IrGetEnumValue
import org.jetbrains.kotlin.ir.symbols.IrClassSymbol
import org.jetbrains.kotlin.ir.symbols.UnsafeDuringIrConstructionAPI
import org.jetbrains.kotlin.ir.util.getValueArgument

/**
 * Transformer for [io.github.kitakkun.kondition.core.annotation.RangedInt] annotation.
 */
class RangedIntTransformer(irContext: KonditionIrContext) : ValueParameterCheckStatementsProducer(irContext) {
    override val annotationClassSymbol: IrClassSymbol = irContext.rangedIntAnnotation

    @OptIn(UnsafeDuringIrConstructionAPI::class)
    override fun IrBuilderWithScope.produceRequiredFunctionCall(
        irContext: KonditionIrContext,
        parentDeclaration: IrFunction,
        valueParameter: IrValueParameter,
        annotation: IrConstructorCall?,
    ): IrStatement? {
        annotation ?: return null

        val start = annotation.getIntConstArgument(0)
        val end = annotation.getIntConstArgument(1)
        val rangeRuleArgument = annotation.getValueArgument(2) as? IrGetEnumValue
        val rangeRule = RangeRule.entries.find { it.name == rangeRuleArgument?.symbol?.owner?.name?.asString() } ?: RangeRule.InclusiveInclusive

        val irGetValueParameter = irGet(valueParameter)
        val irStartInt = irInt(start)
        val irEndInt = irInt(end)

        val startCompareFunction = when (rangeRule) {
            RangeRule.InclusiveInclusive -> irContext.greaterThanOrEquals
            RangeRule.ExclusiveExclusive -> irContext.greaterThan
            RangeRule.ExclusiveInclusive -> irContext.greaterThan
            RangeRule.InclusiveExclusive -> irContext.greaterThanOrEquals
        }
        val endCompareFunction = when (rangeRule) {
            RangeRule.InclusiveInclusive -> irContext.lessThanOrEquals
            RangeRule.ExclusiveExclusive -> irContext.lessThan
            RangeRule.ExclusiveInclusive -> irContext.lessThanOrEquals
            RangeRule.InclusiveExclusive -> irContext.lessThan
        }

        val irCompareStart = irCall(startCompareFunction).apply {
            extensionReceiver = irGetValueParameter
            putValueArgument(0, irStartInt)
        }

        val irCompareEnd = irCall(endCompareFunction).apply {
            extensionReceiver = irGetValueParameter
            putValueArgument(0, irEndInt)
        }

        val irInRange = irCall(irContext.booleanAndFunction).apply {
            dispatchReceiver = irCompareStart
            putValueArgument(0, irCompareEnd)
        }

        return irCall(irContext.requireFunction).apply {
            putValueArgument(0, irInRange)
        }
    }
}
