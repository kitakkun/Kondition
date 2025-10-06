package com.kitakkun.kondition.compiler.backend.fitting

import com.kitakkun.kondition.compiler.backend.KonditionIrContext
import com.kitakkun.kondition.compiler.backend.util.getConstArgument
import com.kitakkun.kondition.compiler.backend.util.setExtensionReceiver
import com.kitakkun.kondition.compiler.backend.util.setTypeArgument
import com.kitakkun.kondition.compiler.backend.util.setValueArgument
import com.kitakkun.kondition.compiler.common.KonditionConsts
import org.jetbrains.kotlin.ir.builders.IrBuilderWithScope
import org.jetbrains.kotlin.ir.builders.irCall
import org.jetbrains.kotlin.ir.declarations.IrDeclarationParent
import org.jetbrains.kotlin.ir.expressions.IrConstructorCall
import org.jetbrains.kotlin.ir.expressions.IrExpression
import org.jetbrains.kotlin.ir.util.toIrConst
import org.jetbrains.kotlin.name.ClassId

class CoerceAtMostFitter : ValueFitter {
    override val annotationClassId: ClassId = KonditionConsts.CoerceAtMostClassId
    override fun IrBuilderWithScope.produceFitVariable(
        irContext: KonditionIrContext,
        parentDeclaration: IrDeclarationParent,
        originalValue: IrExpression,
        annotation: IrConstructorCall,
    ): IrExpression {
        val minimumValue = annotation.getConstArgument<Long>(0)

        return irCall(irContext.coerceAtMost).apply {
            setExtensionReceiver(originalValue)
            setTypeArgument(0, originalValue.type)
            setValueArgument(0, minimumValue.toIrConst(originalValue.type))
        }
    }
}
