package com.kitakkun.kondition.compiler.backend.requirement

import com.kitakkun.kondition.compiler.common.KonditionConsts
import com.kitakkun.kondition.compiler.backend.KonditionIrContext
import org.jetbrains.kotlin.ir.builders.IrBuilderWithScope
import org.jetbrains.kotlin.ir.builders.irCall
import org.jetbrains.kotlin.ir.builders.irConcat
import org.jetbrains.kotlin.ir.builders.irEquals
import org.jetbrains.kotlin.ir.builders.irGet
import org.jetbrains.kotlin.ir.builders.irString
import org.jetbrains.kotlin.ir.declarations.IrFunction
import org.jetbrains.kotlin.ir.declarations.IrValueDeclaration
import org.jetbrains.kotlin.ir.expressions.IrConstructorCall
import org.jetbrains.kotlin.ir.expressions.IrExpression
import org.jetbrains.kotlin.ir.expressions.addArgument
import org.jetbrains.kotlin.name.ClassId

class LowerCasedRequirementProvider : RequirementProvider {
    override val annotationClassId: ClassId = KonditionConsts.LowerCasedClassId
    override fun IrBuilderWithScope.produceErrorMessage(
        irContext: KonditionIrContext,
        parentDeclaration: IrFunction,
        value: IrValueDeclaration,
        annotation: IrConstructorCall
    ): IrExpression? {
        return irConcat().apply {
            addArgument(irString("${value.name} in ${parentDeclaration.name}"))
            addArgument(irString("must be lowercased. "))
            addArgument(irString("But the actual value is "))
            addArgument(irGet(value))
        }
    }

    override fun IrBuilderWithScope.produceRequiredCondition(
        irContext: KonditionIrContext,
        parentDeclaration: IrFunction,
        value: IrValueDeclaration,
        annotation: IrConstructorCall
    ): IrExpression {
        val function = when (value.type) {
            irContext.irBuiltIns.stringType -> irContext.lowercaseString
            irContext.irBuiltIns.charType -> irContext.lowercaseChar
            else -> error("Unsupported type ${value.type}")
        }

        return irEquals(
            irCall(function).apply {
                extensionReceiver = irGet(value)
            },
            irGet(value)
        )
    }
}
