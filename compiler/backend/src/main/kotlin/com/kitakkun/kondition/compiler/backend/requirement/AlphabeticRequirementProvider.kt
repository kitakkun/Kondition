package com.kitakkun.kondition.compiler.backend.requirement

import com.kitakkun.kondition.compiler.backend.KonditionIrContext
import com.kitakkun.kondition.compiler.backend.util.setExtensionReceiver
import com.kitakkun.kondition.compiler.common.KonditionConsts
import org.jetbrains.kotlin.ir.builders.IrBuilderWithScope
import org.jetbrains.kotlin.ir.builders.irCall
import org.jetbrains.kotlin.ir.builders.irGet
import org.jetbrains.kotlin.ir.builders.irString
import org.jetbrains.kotlin.ir.declarations.IrFunction
import org.jetbrains.kotlin.ir.declarations.IrValueDeclaration
import org.jetbrains.kotlin.ir.expressions.IrConstructorCall
import org.jetbrains.kotlin.ir.expressions.IrExpression
import org.jetbrains.kotlin.name.ClassId

class AlphabeticRequirementProvider : RequirementProvider {
    override val annotationClassId: ClassId = KonditionConsts.AlphabeticClassId

    override fun IrBuilderWithScope.produceErrorMessage(
        irContext: KonditionIrContext,
        parentDeclaration: IrFunction,
        valueParameter: IrValueDeclaration,
        annotation: IrConstructorCall,
    ): IrExpression? = irString("${valueParameter.name} in ${parentDeclaration.name} can't contain non-alphabetic characters.")

    override fun IrBuilderWithScope.produceRequiredCondition(
        irContext: KonditionIrContext,
        parentDeclaration: IrFunction,
        valueParameter: IrValueDeclaration,
        annotation: IrConstructorCall,
    ): IrExpression = irCall(irContext.isAlphaFunction).apply {
        setExtensionReceiver(irGet(valueParameter))
    }
}
