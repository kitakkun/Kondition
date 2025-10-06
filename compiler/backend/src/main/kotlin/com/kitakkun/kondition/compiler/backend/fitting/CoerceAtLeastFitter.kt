package com.kitakkun.kondition.compiler.backend.fitting

import com.kitakkun.kondition.compiler.common.KonditionConsts
import com.kitakkun.kondition.compiler.backend.KonditionIrContext
import com.kitakkun.kondition.compiler.backend.util.getConstArgument
import org.jetbrains.kotlin.ir.builders.IrBuilderWithScope
import org.jetbrains.kotlin.ir.builders.irCall
import org.jetbrains.kotlin.ir.declarations.IrDeclarationParent
import org.jetbrains.kotlin.ir.expressions.IrConstructorCall
import org.jetbrains.kotlin.ir.expressions.IrExpression
import org.jetbrains.kotlin.ir.util.toIrConst
import org.jetbrains.kotlin.name.ClassId

class CoerceAtLeastFitter : ValueFitter {
    override val annotationClassId: ClassId = KonditionConsts.CoerceAtLeastClassId
    override fun IrBuilderWithScope.produceFitVariable(
        irContext: KonditionIrContext,
        parentDeclaration: IrDeclarationParent,
        originalValue: IrExpression,
        annotation: IrConstructorCall,
    ): IrExpression {
        val minimumValue = annotation.getConstArgument<Long>(0)

        return irCall(irContext.coerceAtLeast).apply {
            extensionReceiver = originalValue
            putTypeArgument(0, originalValue.type)
            putValueArgument(0, minimumValue.toIrConst(originalValue.type))
        }
    }
}
