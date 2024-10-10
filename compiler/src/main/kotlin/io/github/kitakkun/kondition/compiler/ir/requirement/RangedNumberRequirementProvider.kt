package io.github.kitakkun.kondition.compiler.ir.requirement

import io.github.kitakkun.kondition.compiler.ir.KonditionIrContext
import io.github.kitakkun.kondition.compiler.ir.util.irDouble
import io.github.kitakkun.kondition.compiler.ir.util.irFloat
import io.github.kitakkun.kondition.core.annotation.RangeRule
import io.github.kitakkun.kondition.core.annotation.RangedByte
import io.github.kitakkun.kondition.core.annotation.RangedDouble
import io.github.kitakkun.kondition.core.annotation.RangedFloat
import io.github.kitakkun.kondition.core.annotation.RangedInt
import io.github.kitakkun.kondition.core.annotation.RangedLong
import io.github.kitakkun.kondition.core.annotation.RangedShort
import org.jetbrains.kotlin.descriptors.runtime.structure.classId
import org.jetbrains.kotlin.ir.builders.IrBuilderWithScope
import org.jetbrains.kotlin.ir.builders.irByte
import org.jetbrains.kotlin.ir.builders.irCall
import org.jetbrains.kotlin.ir.builders.irGet
import org.jetbrains.kotlin.ir.builders.irInt
import org.jetbrains.kotlin.ir.builders.irLong
import org.jetbrains.kotlin.ir.builders.irShort
import org.jetbrains.kotlin.ir.builders.irString
import org.jetbrains.kotlin.ir.declarations.IrFunction
import org.jetbrains.kotlin.ir.declarations.IrValueDeclaration
import org.jetbrains.kotlin.ir.expressions.IrConst
import org.jetbrains.kotlin.ir.expressions.IrConstructorCall
import org.jetbrains.kotlin.ir.expressions.IrExpression
import org.jetbrains.kotlin.ir.expressions.IrGetEnumValue
import org.jetbrains.kotlin.ir.symbols.UnsafeDuringIrConstructionAPI
import org.jetbrains.kotlin.name.ClassId

class RangedIntRequirementProvider : RangedNumberRequirementProvider<Int>(RangedInt::class.java.classId)
class RangedFloatRequirementProvider : RangedNumberRequirementProvider<Float>(RangedFloat::class.java.classId)
class RangedDoubleRequirementProvider : RangedNumberRequirementProvider<Double>(RangedDouble::class.java.classId)
class RangedLongRequirementProvider : RangedNumberRequirementProvider<Long>(RangedLong::class.java.classId)
class RangedShortRequirementProvider : RangedNumberRequirementProvider<Short>(RangedShort::class.java.classId)
class RangedByteRequirementProvider : RangedNumberRequirementProvider<Byte>(RangedByte::class.java.classId)

sealed class RangedNumberRequirementProvider<T : Number>(override val annotationClassId: ClassId) : RequirementProvider {
    private data class AnnotationValue<T : Number>(
        val start: T,
        val end: T,
        val rule: RangeRule,
    ) {
        companion object {
            @Suppress("UNCHECKED_CAST")
            @OptIn(UnsafeDuringIrConstructionAPI::class)
            fun <T : Number> convert(annotation: IrConstructorCall): AnnotationValue<T> {
                val start = (annotation.getValueArgument(0) as? IrConst<*>)?.value as T
                val end = (annotation.getValueArgument(1) as? IrConst<*>)?.value as T
                return AnnotationValue(
                    start = start,
                    end = end,
                    rule = RangeRule.entries.find {
                        it.name == (annotation.getValueArgument(2) as? IrGetEnumValue)?.symbol?.owner?.name?.asString()
                    } ?: RangeRule.InclusiveInclusive,
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
        val (start, end, rangeRule) = AnnotationValue.convert<T>(annotation)

        val suffix = when (rangeRule) {
            RangeRule.InclusiveInclusive -> "must be in range $start..$end"
            RangeRule.ExclusiveExclusive -> "must be greater than $start and less than $end"
            RangeRule.ExclusiveInclusive -> "must be grater than $start and less than or equals to $end"
            RangeRule.InclusiveExclusive -> "must be greater than or equals to $start and less than $end"
        }

        return irString("${value.name} in ${parentDeclaration.name} $suffix")
    }

    override fun IrBuilderWithScope.produceRequiredCondition(
        irContext: KonditionIrContext,
        parentDeclaration: IrFunction,
        value: IrValueDeclaration,
        annotation: IrConstructorCall,
    ): IrExpression {
        val (start, end, rangeRule) = AnnotationValue.convert<T>(annotation)

        val irGetValueParameter = irGet(value)

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
            putValueArgument(0, getConstExpression(start))
        }

        val irCompareEnd = irCall(endCompareFunction).apply {
            extensionReceiver = irGetValueParameter
            putValueArgument(0, getConstExpression(end))
        }

        return irCall(irContext.booleanAndFunction).apply {
            dispatchReceiver = irCompareStart
            putValueArgument(0, irCompareEnd)
        }
    }

    private fun <T> IrBuilderWithScope.getConstExpression(value: T): IrExpression = when (value) {
        is Int -> irInt(value)
        is Long -> irLong(value)
        is Double -> irDouble(value)
        is Short -> irShort(value)
        is Byte -> irByte(value)
        is Float -> irFloat(value)
        else -> error("Unexpected type")
    }
}
