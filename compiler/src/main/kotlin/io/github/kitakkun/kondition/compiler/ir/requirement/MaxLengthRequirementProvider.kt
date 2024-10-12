package io.github.kitakkun.kondition.compiler.ir.requirement

import io.github.kitakkun.kondition.compiler.ir.KonditionIrContext
import io.github.kitakkun.kondition.compiler.ir.util.getConstArgument
import io.github.kitakkun.kondition.core.annotation.MaxLength
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

class MaxLengthRequirementProvider : RequirementProvider {
    override val annotationClassId: ClassId = MaxLength::class.java.classId

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