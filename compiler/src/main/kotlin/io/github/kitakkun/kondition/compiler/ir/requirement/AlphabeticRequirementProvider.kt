package io.github.kitakkun.kondition.compiler.ir.requirement

import io.github.kitakkun.kondition.compiler.ir.KonditionIrContext
import io.github.kitakkun.kondition.core.annotation.Alphabetic
import org.jetbrains.kotlin.descriptors.runtime.structure.classId
import org.jetbrains.kotlin.ir.builders.IrBuilderWithScope
import org.jetbrains.kotlin.ir.builders.irCall
import org.jetbrains.kotlin.ir.builders.irGet
import org.jetbrains.kotlin.ir.builders.irString
import org.jetbrains.kotlin.ir.declarations.IrFunction
import org.jetbrains.kotlin.ir.declarations.IrValueParameter
import org.jetbrains.kotlin.ir.expressions.IrConstructorCall
import org.jetbrains.kotlin.ir.expressions.IrExpression
import org.jetbrains.kotlin.name.ClassId

class AlphabeticRequirementProvider : RequirementProvider {
    override val annotationClassId: ClassId = Alphabetic::class.java.classId

    override fun IrBuilderWithScope.produceErrorMessage(
        irContext: KonditionIrContext,
        parentDeclaration: IrFunction,
        valueParameter: IrValueParameter,
        annotation: IrConstructorCall,
    ): IrExpression? = irString("${valueParameter.name} in ${parentDeclaration.name} can't contain non-alphabetic characters.")

    override fun IrBuilderWithScope.produceRequiredCondition(
        irContext: KonditionIrContext,
        parentDeclaration: IrFunction,
        valueParameter: IrValueParameter,
        annotation: IrConstructorCall,
    ): IrExpression = irCall(irContext.isAlphaFunction).apply {
        extensionReceiver = irGet(valueParameter)
    }
}
