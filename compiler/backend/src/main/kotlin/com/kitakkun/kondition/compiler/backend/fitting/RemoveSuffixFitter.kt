package com.kitakkun.kondition.compiler.backend.fitting

import com.kitakkun.kondition.compiler.backend.KonditionIrContext
import com.kitakkun.kondition.compiler.backend.util.setExtensionReceiver
import com.kitakkun.kondition.compiler.backend.util.setValueArgument
import com.kitakkun.kondition.compiler.common.KonditionConsts
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
            setExtensionReceiver(originalValue)
            setValueArgument(0, irString(suffix))
        }
    }
}
