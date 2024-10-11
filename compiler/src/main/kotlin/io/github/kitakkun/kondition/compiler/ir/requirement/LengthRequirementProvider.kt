package io.github.kitakkun.kondition.compiler.ir.requirement

import io.github.kitakkun.kondition.compiler.ir.KonditionIrContext
import io.github.kitakkun.kondition.compiler.ir.util.getConstArgument
import io.github.kitakkun.kondition.core.annotation.Length
import org.jetbrains.kotlin.descriptors.runtime.structure.classId
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

class LengthRequirementProvider : RequirementProvider {
    override val annotationClassId: ClassId = Length::class.java.classId

    override fun IrBuilderWithScope.produceErrorMessage(
        irContext: KonditionIrContext,
        parentDeclaration: IrFunction,
        value: IrValueDeclaration,
        annotation: IrConstructorCall,
    ): IrExpression? {
        val length = annotation.getConstArgument<Int>(0) ?: error("couldn't parse length")
        return irString("${value.name} in ${parentDeclaration.name} must have an exact length $length")
    }

    override fun IrBuilderWithScope.produceRequiredCondition(
        irContext: KonditionIrContext,
        parentDeclaration: IrFunction,
        value: IrValueDeclaration,
        annotation: IrConstructorCall,
    ): IrExpression {
        val length = annotation.getConstArgument<Int>(0) ?: error("couldn't parse length")
        return irCall(irContext.lengthEquals).apply {
            extensionReceiver = irGet(value)
            putValueArgument(0, irInt(length))
        }
    }
}
