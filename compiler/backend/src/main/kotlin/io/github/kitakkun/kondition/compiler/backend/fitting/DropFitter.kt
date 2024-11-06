package io.github.kitakkun.kondition.compiler.backend.fitting

import io.github.kitakkun.kondition.compiler.common.KonditionConsts
import io.github.kitakkun.kondition.compiler.backend.KonditionIrContext
import org.jetbrains.kotlin.backend.jvm.ir.getIntConstArgument
import org.jetbrains.kotlin.ir.builders.IrBuilderWithScope
import org.jetbrains.kotlin.ir.builders.irCall
import org.jetbrains.kotlin.ir.builders.irInt
import org.jetbrains.kotlin.ir.declarations.IrDeclarationParent
import org.jetbrains.kotlin.ir.expressions.IrConstructorCall
import org.jetbrains.kotlin.ir.expressions.IrExpression
import org.jetbrains.kotlin.name.ClassId

class DropFitter : ValueFitter {
    override val annotationClassId: ClassId = KonditionConsts.DropClassId
    override fun IrBuilderWithScope.produceFitVariable(
        irContext: KonditionIrContext,
        parentDeclaration: IrDeclarationParent,
        originalValue: IrExpression,
        annotation: IrConstructorCall,
    ): IrExpression {
        val length = annotation.getIntConstArgument(0)

        return irCall(irContext.drop).apply {
            extensionReceiver = originalValue
            putValueArgument(0, irInt(length))
        }
    }
}
