package io.github.kitakkun.kondition.compiler.backend.fitting

import io.github.kitakkun.kondition.compiler.backend.KonditionIrContext
import io.github.kitakkun.kondition.compiler.backend.util.getConstArgument
import io.github.kitakkun.kondition.compiler.common.KonditionConsts
import org.jetbrains.kotlin.ir.builders.IrBuilderWithScope
import org.jetbrains.kotlin.ir.builders.irCall
import org.jetbrains.kotlin.ir.declarations.IrDeclarationParent
import org.jetbrains.kotlin.ir.expressions.IrConstructorCall
import org.jetbrains.kotlin.ir.expressions.IrExpression
import org.jetbrains.kotlin.ir.util.toIrConst
import org.jetbrains.kotlin.name.ClassId

class CoerceAtLeastDecimalFitter : ValueFitter {
    override val annotationClassId: ClassId = KonditionConsts.CoerceAtLeastDecimalClassId
    override fun IrBuilderWithScope.produceFitVariable(
        irContext: KonditionIrContext,
        parentDeclaration: IrDeclarationParent,
        originalValue: IrExpression,
        annotation: IrConstructorCall,
    ): IrExpression {
        val minimumValue = annotation.getConstArgument<Double>(0)

        return irCall(irContext.coerceAtLeast).apply {
            extensionReceiver = originalValue
            putTypeArgument(0, originalValue.type)
            putValueArgument(0, minimumValue.toIrConst(originalValue.type))
        }
    }
}
