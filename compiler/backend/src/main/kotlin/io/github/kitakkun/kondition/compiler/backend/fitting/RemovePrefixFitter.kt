package io.github.kitakkun.kondition.compiler.backend.fitting

import io.github.kitakkun.kondition.compiler.common.KonditionConsts
import io.github.kitakkun.kondition.compiler.backend.KonditionIrContext
import org.jetbrains.kotlin.backend.jvm.ir.getStringConstArgument
import org.jetbrains.kotlin.ir.builders.IrBuilderWithScope
import org.jetbrains.kotlin.ir.builders.irCall
import org.jetbrains.kotlin.ir.builders.irString
import org.jetbrains.kotlin.ir.declarations.IrDeclarationParent
import org.jetbrains.kotlin.ir.expressions.IrConstructorCall
import org.jetbrains.kotlin.ir.expressions.IrExpression
import org.jetbrains.kotlin.name.ClassId

class RemovePrefixFitter : ValueFitter {
    override val annotationClassId: ClassId = KonditionConsts.RemovePrefixClassId

    override fun IrBuilderWithScope.produceFitVariable(
        irContext: KonditionIrContext,
        parentDeclaration: IrDeclarationParent,
        originalValue: IrExpression,
        annotation: IrConstructorCall,
    ): IrExpression {
        val prefix = annotation.getStringConstArgument(0)

        return irCall(irContext.removePrefix).apply {
            extensionReceiver = originalValue
            putValueArgument(0, irString(prefix))
        }
    }
}
