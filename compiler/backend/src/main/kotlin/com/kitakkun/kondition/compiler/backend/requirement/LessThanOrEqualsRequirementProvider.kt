package com.kitakkun.kondition.compiler.backend.requirement

import com.kitakkun.kondition.compiler.common.KonditionConsts
import com.kitakkun.kondition.compiler.backend.KonditionIrContext
import com.kitakkun.kondition.compiler.backend.util.getConstArgument
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

class LessThanOrEqualsLongRequirementProvider : LessThanOrEqualsRequirementProvider<Long>(KonditionConsts.LessThanOrEqualsClassId)

class LessThanOrEqualsDecimalRequirementProvider : LessThanOrEqualsRequirementProvider<Double>(KonditionConsts.LessThanOrEqualsDecimalClassId)

sealed class LessThanOrEqualsRequirementProvider<T : Number>(
    override val annotationClassId: ClassId,
) : RequirementProvider {
    override fun IrBuilderWithScope.produceErrorMessage(
        irContext: KonditionIrContext,
        parentDeclaration: IrFunction,
        value: IrValueDeclaration,
        annotation: IrConstructorCall,
    ): IrExpression? {
        val threshold = annotation.getConstArgument<T>(0) ?: error("can't get argument of annotation.")
        return irString("${value.name} in ${parentDeclaration.name} must be less than or equals to $threshold")
    }

    override fun IrBuilderWithScope.produceRequiredCondition(
        irContext: KonditionIrContext,
        parentDeclaration: IrFunction,
        value: IrValueDeclaration,
        annotation: IrConstructorCall,
    ): IrExpression {
        val threshold = annotation.getConstArgument<T>(0) ?: error("can't get argument of annotation.")

        return irCall(irContext.lessThanOrEquals).apply {
            extensionReceiver = irGet(value)
            putValueArgument(0, threshold.toIrConst(value.type))
        }
    }
}
