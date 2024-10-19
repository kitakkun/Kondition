package io.github.kitakkun.kondition.compiler.ir.requirement

import io.github.kitakkun.kondition.compiler.KonditionConsts
import io.github.kitakkun.kondition.compiler.ir.KonditionIrContext
import org.jetbrains.kotlin.backend.jvm.ir.getStringConstArgument
import org.jetbrains.kotlin.ir.builders.IrBuilderWithScope
import org.jetbrains.kotlin.ir.builders.irCall
import org.jetbrains.kotlin.ir.builders.irConcat
import org.jetbrains.kotlin.ir.builders.irGet
import org.jetbrains.kotlin.ir.builders.irString
import org.jetbrains.kotlin.ir.declarations.IrFunction
import org.jetbrains.kotlin.ir.declarations.IrValueDeclaration
import org.jetbrains.kotlin.ir.expressions.IrConstructorCall
import org.jetbrains.kotlin.ir.expressions.IrExpression
import org.jetbrains.kotlin.ir.expressions.addArgument
import org.jetbrains.kotlin.name.ClassId

class SuffixedRequirementProvider : RequirementProvider {
    override val annotationClassId: ClassId = KonditionConsts.SuffixedClassId
    override fun IrBuilderWithScope.produceErrorMessage(
        irContext: KonditionIrContext,
        parentDeclaration: IrFunction,
        value: IrValueDeclaration,
        annotation: IrConstructorCall
    ): IrExpression? {
        val suffix = annotation.getStringConstArgument(0)
        return irConcat().apply {
            addArgument(irString("${value.name} in ${parentDeclaration.name}"))
            addArgument(irString("must have a suffix \"$suffix\". "))
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
        val suffix = annotation.getStringConstArgument(0)
        return irCall(irContext.endsWith).apply {
            extensionReceiver = irGet(value)
            putValueArgument(0, irString(suffix))
        }
    }
}
