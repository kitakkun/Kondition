package com.kitakkun.kondition.compiler.backend.requirement

import com.kitakkun.kondition.compiler.common.KonditionConsts
import com.kitakkun.kondition.compiler.backend.KonditionIrContext
import com.kitakkun.kondition.compiler.backend.util.getConstArgument
import com.kitakkun.kondition.compiler.backend.util.getEnumNameOfArgument
import org.jetbrains.kotlin.ir.builders.IrBuilderWithScope
import org.jetbrains.kotlin.ir.builders.irCall
import org.jetbrains.kotlin.ir.builders.irGet
import org.jetbrains.kotlin.ir.builders.irString
import org.jetbrains.kotlin.ir.declarations.IrFunction
import org.jetbrains.kotlin.ir.declarations.IrValueDeclaration
import org.jetbrains.kotlin.ir.expressions.IrConstructorCall
import org.jetbrains.kotlin.ir.expressions.IrExpression
import org.jetbrains.kotlin.ir.symbols.UnsafeDuringIrConstructionAPI
import org.jetbrains.kotlin.ir.util.toIrConst
import org.jetbrains.kotlin.name.ClassId

class RangedLongRequirementProvider : RangedNumberRequirementProvider<Long>(KonditionConsts.RangedClassId)
class RangedDecimalRequirementProvider : RangedNumberRequirementProvider<Double>(KonditionConsts.RangedDecimalClassId)

sealed class RangedNumberRequirementProvider<T : Number>(override val annotationClassId: ClassId) :
    RequirementProvider {
    private data class AnnotationValue<T : Number>(
        val start: T,
        val end: T,
        val rule: RangeRule,
    ) {
        enum class RangeRule {
            InclusiveInclusive,
            InclusiveExclusive,
            ExclusiveInclusive,
            ExclusiveExclusive,
        }

        companion object {
            @OptIn(UnsafeDuringIrConstructionAPI::class)
            fun <T : Number> convert(annotation: IrConstructorCall): AnnotationValue<T> {
                val start = annotation.getConstArgument<T>(0) ?: error("cannot convert $annotation")
                val end = annotation.getConstArgument<T>(1) ?: error("cannot convert $annotation")
                val rangeRuleName = annotation.getEnumNameOfArgument(2)
                return AnnotationValue(
                    start = start,
                    end = end,
                    rule = RangeRule.entries.find { it.name == rangeRuleName } ?: RangeRule.InclusiveInclusive,
                )
            }
        }
    }

    override fun IrBuilderWithScope.produceErrorMessage(
        irContext: KonditionIrContext,
        parentDeclaration: IrFunction,
        value: IrValueDeclaration,
        annotation: IrConstructorCall,
    ): IrExpression? {
        val (start, end, rangeRule) = AnnotationValue.convert<T>(
            annotation
        )

        val suffix = when (rangeRule) {
            AnnotationValue.RangeRule.InclusiveInclusive -> "must be in range $start..$end"
            AnnotationValue.RangeRule.ExclusiveExclusive -> "must be greater than $start and less than $end"
            AnnotationValue.RangeRule.ExclusiveInclusive -> "must be grater than $start and less than or equals to $end"
            AnnotationValue.RangeRule.InclusiveExclusive -> "must be greater than or equals to $start and less than $end"
        }

        return irString("${value.name} in ${parentDeclaration.name} $suffix")
    }

    override fun IrBuilderWithScope.produceRequiredCondition(
        irContext: KonditionIrContext,
        parentDeclaration: IrFunction,
        value: IrValueDeclaration,
        annotation: IrConstructorCall,
    ): IrExpression {
        val (start, end, rangeRule) = AnnotationValue.convert<T>(
            annotation
        )

        val valueType = value.type
        val irGetValueParameter = irGet(value)

        val startCompareFunction = when (rangeRule) {
            AnnotationValue.RangeRule.InclusiveInclusive -> irContext.greaterThanOrEquals
            AnnotationValue.RangeRule.ExclusiveExclusive -> irContext.greaterThan
            AnnotationValue.RangeRule.ExclusiveInclusive -> irContext.greaterThan
            AnnotationValue.RangeRule.InclusiveExclusive -> irContext.greaterThanOrEquals
        }
        val endCompareFunction = when (rangeRule) {
            AnnotationValue.RangeRule.InclusiveInclusive -> irContext.lessThanOrEquals
            AnnotationValue.RangeRule.ExclusiveExclusive -> irContext.lessThan
            AnnotationValue.RangeRule.ExclusiveInclusive -> irContext.lessThanOrEquals
            AnnotationValue.RangeRule.InclusiveExclusive -> irContext.lessThan
        }

        val irCompareStart = irCall(startCompareFunction).apply {
            extensionReceiver = irGetValueParameter
            putValueArgument(0, start.toIrConst(valueType))
        }

        val irCompareEnd = irCall(endCompareFunction).apply {
            extensionReceiver = irGetValueParameter
            putValueArgument(0, end.toIrConst(valueType))
        }

        return irCall(irContext.booleanAndFunction).apply {
            dispatchReceiver = irCompareStart
            putValueArgument(0, irCompareEnd)
        }
    }
}
