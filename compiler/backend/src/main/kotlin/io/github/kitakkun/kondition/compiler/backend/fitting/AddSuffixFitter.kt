package io.github.kitakkun.kondition.compiler.backend.fitting

import io.github.kitakkun.kondition.compiler.backend.KonditionIrContext
import io.github.kitakkun.kondition.compiler.common.KonditionConsts
import org.jetbrains.kotlin.backend.jvm.ir.getStringConstArgument
import org.jetbrains.kotlin.ir.builders.IrBuilderWithScope
import org.jetbrains.kotlin.ir.builders.irConcat
import org.jetbrains.kotlin.ir.builders.irString
import org.jetbrains.kotlin.ir.declarations.IrDeclarationParent
import org.jetbrains.kotlin.ir.expressions.IrConstructorCall
import org.jetbrains.kotlin.ir.expressions.IrExpression
import org.jetbrains.kotlin.ir.expressions.addArgument
import org.jetbrains.kotlin.name.ClassId

class AddSuffixFitter : ValueFitter {
    override val annotationClassId: ClassId = KonditionConsts.AddSuffixClassId

    override fun IrBuilderWithScope.produceFitVariable(
        irContext: KonditionIrContext,
        parentDeclaration: IrDeclarationParent,
        originalValue: IrExpression,
        annotation: IrConstructorCall,
    ): IrExpression {
        val suffix = annotation.getStringConstArgument(0)

        return irConcat().apply {
            addArgument(originalValue)
            addArgument(irString(suffix))
        }
    }
}
