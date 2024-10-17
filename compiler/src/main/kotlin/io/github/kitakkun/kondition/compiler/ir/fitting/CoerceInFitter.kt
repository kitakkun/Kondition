package io.github.kitakkun.kondition.compiler.ir.fitting

import io.github.kitakkun.kondition.compiler.KonditionConsts
import io.github.kitakkun.kondition.compiler.ir.KonditionIrContext
import io.github.kitakkun.kondition.compiler.ir.util.getConstArgument
import org.jetbrains.kotlin.ir.builders.IrBuilderWithScope
import org.jetbrains.kotlin.ir.builders.irCall
import org.jetbrains.kotlin.ir.declarations.IrDeclarationParent
import org.jetbrains.kotlin.ir.expressions.IrConstructorCall
import org.jetbrains.kotlin.ir.expressions.IrExpression
import org.jetbrains.kotlin.ir.util.toIrConst
import org.jetbrains.kotlin.name.ClassId

class CoerceInFitter : ValueFitter {
    override val annotationClassId: ClassId = KonditionConsts.CoerceInClassId
    override fun IrBuilderWithScope.produceFitVariable(
        irContext: KonditionIrContext,
        parentDeclaration: IrDeclarationParent,
        originalValue: IrExpression,
        annotation: IrConstructorCall,
    ): IrExpression {
        val minimumValue = annotation.getConstArgument<Long>(0)
        val maximumValue = annotation.getConstArgument<Long>(1)

        return irCall(irContext.coerceIn).apply {
            extensionReceiver = originalValue
            putTypeArgument(0, originalValue.type)
            putValueArgument(0, minimumValue.toIrConst(originalValue.type))
            putValueArgument(1, maximumValue.toIrConst(originalValue.type))
        }
    }
}
