package io.github.kitakkun.kondition.compiler.ir.fitting

import io.github.kitakkun.kondition.compiler.KonditionConsts
import io.github.kitakkun.kondition.compiler.ir.KonditionIrContext
import org.jetbrains.kotlin.backend.jvm.ir.getStringConstArgument
import org.jetbrains.kotlin.ir.builders.IrBuilderWithScope
import org.jetbrains.kotlin.ir.builders.irCall
import org.jetbrains.kotlin.ir.builders.irString
import org.jetbrains.kotlin.ir.declarations.IrDeclarationParent
import org.jetbrains.kotlin.ir.expressions.IrConstructorCall
import org.jetbrains.kotlin.ir.expressions.IrExpression
import org.jetbrains.kotlin.name.ClassId

class RemoveSuffixFitter : ValueFitter {
    override val annotationClassId: ClassId = KonditionConsts.RemoveSuffixClassId

    override fun IrBuilderWithScope.produceFitVariable(
        irContext: KonditionIrContext,
        parentDeclaration: IrDeclarationParent,
        originalValue: IrExpression,
        annotation: IrConstructorCall,
    ): IrExpression {
        val suffix = annotation.getStringConstArgument(0)

        return irCall(irContext.removeSuffix).apply {
            extensionReceiver = originalValue
            putValueArgument(0, irString(suffix))
        }
    }
}
