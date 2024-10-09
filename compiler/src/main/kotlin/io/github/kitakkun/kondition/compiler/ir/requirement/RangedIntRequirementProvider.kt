package io.github.kitakkun.kondition.compiler.ir.requirement

import io.github.kitakkun.kondition.compiler.ir.KonditionIrContext
import io.github.kitakkun.kondition.core.annotation.RangeRule
import io.github.kitakkun.kondition.core.annotation.RangedInt
import org.jetbrains.kotlin.backend.jvm.ir.getIntConstArgument
import org.jetbrains.kotlin.descriptors.runtime.structure.classId
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
import org.jetbrains.kotlin.ir.symbols.UnsafeDuringIrConstructionAPI
import org.jetbrains.kotlin.name.ClassId

class RangedIntRequirementProvider : RequirementProvider {
    override val annotationClassId: ClassId = RangedInt::class.java.classId

    private data class AnnotationValue(
        val start: Int,
        val end: Int,
        val rule: RangeRule,
    ) {
        companion object {
            @OptIn(UnsafeDuringIrConstructionAPI::class)
            fun convert(annotation: IrConstructorCall): AnnotationValue = AnnotationValue(
                start = annotation.getIntConstArgument(0),
                end = annotation.getIntConstArgument(1),
                rule = RangeRule.entries.find {
                    it.name == (annotation.getValueArgument(2) as? IrGetEnumValue)?.symbol?.owner?.name?.asString()
                } ?: RangeRule.InclusiveInclusive,
            )
        }
    }

    override fun IrBuilderWithScope.produceErrorMessage(irContext: KonditionIrContext, parentDeclaration: IrFunction, valueParameter: IrValueParameter, annotation: IrConstructorCall): IrExpression? {
        val (start, end, rangeRule) = AnnotationValue.convert(annotation)

        val suffix = when (rangeRule) {
            RangeRule.InclusiveInclusive -> "must be in range $start..$end"
            RangeRule.ExclusiveExclusive -> "must be greater than $start and less than $end"
            RangeRule.ExclusiveInclusive -> "must be grater than $start and less than or equals to $end"
            RangeRule.InclusiveExclusive -> "must be greater than or equals to $start and less than $end"
        }

        return irString("${valueParameter.name} in ${parentDeclaration.name} $suffix")
    }

    override fun IrBuilderWithScope.produceRequiredCondition(
        irContext: KonditionIrContext,
        parentDeclaration: IrFunction,
        valueParameter: IrValueParameter,
        annotation: IrConstructorCall,
    ): IrExpression {
        val (start, end, rangeRule) = AnnotationValue.convert(annotation)

        val irGetValueParameter = irGet(valueParameter)

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
            putValueArgument(0, irInt(start))
        }

        val irCompareEnd = irCall(endCompareFunction).apply {
            extensionReceiver = irGetValueParameter
            putValueArgument(0, irInt(end))
        }

        return irCall(irContext.booleanAndFunction).apply {
            dispatchReceiver = irCompareStart
            putValueArgument(0, irCompareEnd)
        }
    }
}
