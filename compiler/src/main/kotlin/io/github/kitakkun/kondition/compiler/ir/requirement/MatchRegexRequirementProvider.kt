package io.github.kitakkun.kondition.compiler.ir.requirement

import io.github.kitakkun.kondition.compiler.ir.KonditionIrContext
import io.github.kitakkun.kondition.core.annotation.MatchRegex
import org.jetbrains.kotlin.backend.jvm.ir.getStringConstArgument
import org.jetbrains.kotlin.descriptors.runtime.structure.classId
import org.jetbrains.kotlin.ir.builders.IrBuilderWithScope
import org.jetbrains.kotlin.ir.builders.irCall
import org.jetbrains.kotlin.ir.builders.irGet
import org.jetbrains.kotlin.ir.builders.irString
import org.jetbrains.kotlin.ir.declarations.IrFunction
import org.jetbrains.kotlin.ir.declarations.IrValueDeclaration
import org.jetbrains.kotlin.ir.expressions.IrConstructorCall
import org.jetbrains.kotlin.ir.expressions.IrExpression
import org.jetbrains.kotlin.name.ClassId

class MatchRegexRequirementProvider : RequirementProvider {
    override val annotationClassId: ClassId = MatchRegex::class.java.classId

    override fun IrBuilderWithScope.produceErrorMessage(irContext: KonditionIrContext, parentDeclaration: IrFunction, value: IrValueDeclaration, annotation: IrConstructorCall): IrExpression? {
        val pattern = annotation.getStringConstArgument(0)
        return irString("${value.name} doesn't match the regex pattern \"$pattern\"")
    }

    override fun IrBuilderWithScope.produceRequiredCondition(
        irContext: KonditionIrContext,
        parentDeclaration: IrFunction,
        value: IrValueDeclaration,
        annotation: IrConstructorCall,
    ): IrExpression {
        val pattern = annotation.getStringConstArgument(0)

        return irCall(irContext.matchRegexFunction).apply {
            extensionReceiver = irGet(value)
            putValueArgument(0, irString(pattern))
        }
    }
}
