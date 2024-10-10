package io.github.kitakkun.kondition.compiler.ir.requirement

import io.github.kitakkun.kondition.compiler.ir.KonditionIrContext
import io.github.kitakkun.kondition.compiler.ir.util.getConstArgument
import io.github.kitakkun.kondition.core.annotation.GreaterThan
import io.github.kitakkun.kondition.core.annotation.GreaterThanDecimal
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

class GreaterThanRequirementProvider : GreaterThanNumberRequirementProvider<Long>(GreaterThan::class.java.classId)
class GreaterThanDecimalRequirementProvider : GreaterThanNumberRequirementProvider<Long>(GreaterThanDecimal::class.java.classId)

sealed class GreaterThanNumberRequirementProvider<T : Number>(
    override val annotationClassId: ClassId,
) : RequirementProvider {
    override fun IrBuilderWithScope.produceErrorMessage(
        irContext: KonditionIrContext,
        parentDeclaration: IrFunction,
        value: IrValueDeclaration,
        annotation: IrConstructorCall,
    ): IrExpression? {
        val threshold = annotation.getConstArgument<T>(0) ?: error("can't get argument of annotation.")
        return irString("${value.name} in ${parentDeclaration.name} must be greater than $threshold")
    }

    override fun IrBuilderWithScope.produceRequiredCondition(
        irContext: KonditionIrContext,
        parentDeclaration: IrFunction,
        value: IrValueDeclaration,
        annotation: IrConstructorCall,
    ): IrExpression {
        val threshold = annotation.getConstArgument<T>(0) ?: error("can't get argument of annotation.")

        return irCall(irContext.greaterThan).apply {
            extensionReceiver = irGet(value)
            putValueArgument(0, threshold.toIrConst(value.type))
        }
    }
}
