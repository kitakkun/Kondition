package io.github.kitakkun.kondition.compiler.ir.transformer

import io.github.kitakkun.kondition.compiler.ir.KonditionIrContext
import io.github.kitakkun.kondition.core.annotation.RangeRule
import org.jetbrains.kotlin.backend.jvm.ir.getIntConstArgument
import org.jetbrains.kotlin.ir.builders.IrBuilderWithScope
import org.jetbrains.kotlin.ir.builders.irCall
import org.jetbrains.kotlin.ir.builders.irGet
import org.jetbrains.kotlin.ir.builders.irInt
import org.jetbrains.kotlin.ir.builders.irString
import org.jetbrains.kotlin.ir.declarations.IrFunction
import org.jetbrains.kotlin.ir.declarations.IrValueParameter
import org.jetbrains.kotlin.ir.expressions.IrConstructorCall
import org.jetbrains.kotlin.ir.expressions.IrExpression
import org.jetbrains.kotlin.ir.expressions.IrGetEnumValue
import org.jetbrains.kotlin.ir.symbols.IrClassSymbol
import org.jetbrains.kotlin.ir.symbols.UnsafeDuringIrConstructionAPI

/**
 * Transformer for [io.github.kitakkun.kondition.core.annotation.RangedInt] annotation.
 */
class RangedIntTransformer(irContext: KonditionIrContext) : ValueParameterCheckStatementsProducer(irContext) {
    override val annotationClassSymbol: IrClassSymbol = irContext.rangedIntAnnotation

    private data class AnnotationValue(
        val start: Int,
        val end: Int,
        val rule: RangeRule,
    ) {
        companion object {
            @OptIn(UnsafeDuringIrConstructionAPI::class)
            fun convert(annotation: IrConstructorCall): AnnotationValue {
                return AnnotationValue(
                    start = annotation.getIntConstArgument(0),
                    end = annotation.getIntConstArgument(1),
                    rule = RangeRule.entries.find {
                        it.name == (annotation.getValueArgument(2) as? IrGetEnumValue)?.symbol?.owner?.name?.asString()
                    } ?: RangeRule.InclusiveInclusive
                )
            }
        }
    }

    @OptIn(UnsafeDuringIrConstructionAPI::class)
    override fun IrBuilderWithScope.generateMessageExpression(
        parentDeclaration: IrFunction,
        valueParameter: IrValueParameter,
        annotation: IrConstructorCall?
    ): IrExpression? {
        val (start, end, rangeRule) = annotation?.let { AnnotationValue.convert(it) } ?: return null

        val suffix = when (rangeRule) {
            RangeRule.InclusiveInclusive -> "must be in range ${start}..${end}"
            RangeRule.ExclusiveExclusive -> "must be greater than $start and less than $end"
            RangeRule.ExclusiveInclusive -> "must be grater than $start and less than or equals to $end"
            RangeRule.InclusiveExclusive -> "must be greater than or equals to $start and less than $end"
        }

        return irString("${valueParameter.name} in ${parentDeclaration.name} $suffix")
    }

    @OptIn(UnsafeDuringIrConstructionAPI::class)
    override fun IrBuilderWithScope.produceRequiredCondition(
        irContext: KonditionIrContext,
        parentDeclaration: IrFunction,
        valueParameter: IrValueParameter,
        annotation: IrConstructorCall?,
    ): IrExpression? {
        val (start, end, rangeRule) = annotation?.let { AnnotationValue.convert(it) } ?: return null

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

        return irCall(irContext.booleanAndFunction).apply {
            dispatchReceiver = irCompareStart
            putValueArgument(0, irCompareEnd)
        }
    }
}
