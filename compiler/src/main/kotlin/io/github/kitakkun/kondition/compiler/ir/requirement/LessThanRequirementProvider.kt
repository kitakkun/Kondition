package io.github.kitakkun.kondition.compiler.ir.requirement

import io.github.kitakkun.kondition.compiler.ir.KonditionIrContext
import io.github.kitakkun.kondition.compiler.ir.util.getConstArgument
import io.github.kitakkun.kondition.core.annotation.LessThan
import io.github.kitakkun.kondition.core.annotation.LessThanDecimal
import org.jetbrains.kotlin.descriptors.runtime.structure.classId
import org.jetbrains.kotlin.ir.builders.IrBuilderWithScope
import org.jetbrains.kotlin.ir.builders.irCall
import org.jetbrains.kotlin.ir.builders.irGet
import org.jetbrains.kotlin.ir.builders.irString
import org.jetbrains.kotlin.ir.declarations.IrFunction
import org.jetbrains.kotlin.ir.declarations.IrValueDeclaration
import org.jetbrains.kotlin.ir.expressions.IrConstructorCall
import org.jetbrains.kotlin.ir.expressions.IrExpression
import org.jetbrains.kotlin.ir.util.toIrConst
import org.jetbrains.kotlin.name.ClassId

class LessThanLongRequirementProvider : LessThanRequirementProvider<Long>(LessThan::class.java.classId)
class LessThanDecimalRequirementProvider : LessThanRequirementProvider<Double>(LessThanDecimal::class.java.classId)

sealed class LessThanRequirementProvider<T : Number>(
    override val annotationClassId: ClassId,
) : RequirementProvider {
    override fun IrBuilderWithScope.produceErrorMessage(
        irContext: KonditionIrContext,
        parentDeclaration: IrFunction,
        value: IrValueDeclaration,
        annotation: IrConstructorCall,
    ): IrExpression? {
        val threshold = annotation.getConstArgument<T>(0) ?: error("can't get argument of annotation.")
        return irString("${value.name} in ${parentDeclaration.name} must be less than $threshold")
    }

    override fun IrBuilderWithScope.produceRequiredCondition(
        irContext: KonditionIrContext,
        parentDeclaration: IrFunction,
        value: IrValueDeclaration,
        annotation: IrConstructorCall,
    ): IrExpression {
        val threshold = annotation.getConstArgument<T>(0) ?: error("can't get argument of annotation.")

        return irCall(irContext.lessThan).apply {
            extensionReceiver = irGet(value)
            putValueArgument(0, threshold.toIrConst(value.type))
        }
    }
}
