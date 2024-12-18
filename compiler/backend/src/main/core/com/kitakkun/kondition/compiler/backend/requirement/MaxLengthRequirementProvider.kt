package com.kitakkun.kondition.compiler.backend.requirement

import com.kitakkun.kondition.compiler.backend.KonditionIrContext
import com.kitakkun.kondition.compiler.backend.util.getConstArgument
import com.kitakkun.kondition.compiler.common.KonditionConsts
import org.jetbrains.kotlin.ir.builders.IrBuilderWithScope
import org.jetbrains.kotlin.ir.builders.irCall
import org.jetbrains.kotlin.ir.builders.irGet
import org.jetbrains.kotlin.ir.builders.irInt
import org.jetbrains.kotlin.ir.builders.irString
import org.jetbrains.kotlin.ir.declarations.IrFunction
import org.jetbrains.kotlin.ir.declarations.IrValueDeclaration
import org.jetbrains.kotlin.ir.expressions.IrConstructorCall
import org.jetbrains.kotlin.ir.expressions.IrExpression
import org.jetbrains.kotlin.name.ClassId

class MaxLengthRequirementProvider : RequirementProvider {
    override val annotationClassId: ClassId = KonditionConsts.MaxLengthClassId

    override fun IrBuilderWithScope.produceErrorMessage(
        irContext: KonditionIrContext,
        parentDeclaration: IrFunction,
        value: IrValueDeclaration,
        annotation: IrConstructorCall,
    ): IrExpression? {
        val length = annotation.getConstArgument<Int>(0) ?: error("couldn't parse length")
        return irString("${value.name} in ${parentDeclaration.name} must be at most $length long")
    }

    override fun IrBuilderWithScope.produceRequiredCondition(
        irContext: KonditionIrContext,
        parentDeclaration: IrFunction,
        value: IrValueDeclaration,
        annotation: IrConstructorCall,
    ): IrExpression {
        val length = annotation.getConstArgument<Int>(0) ?: error("couldn't parse length")
        return irCall(irContext.lengthLessThanOrEquals).apply {
            extensionReceiver = irGet(value)
            putValueArgument(0, irInt(length))
        }
    }
}
