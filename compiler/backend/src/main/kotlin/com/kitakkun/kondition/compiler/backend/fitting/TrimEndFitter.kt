package com.kitakkun.kondition.compiler.backend.fitting

import com.kitakkun.kondition.compiler.backend.KonditionIrContext
import com.kitakkun.kondition.compiler.backend.util.setExtensionReceiver
import com.kitakkun.kondition.compiler.common.KonditionConsts
import org.jetbrains.kotlin.ir.builders.IrBuilderWithScope
import org.jetbrains.kotlin.ir.builders.irCall
import org.jetbrains.kotlin.ir.declarations.IrDeclarationParent
import org.jetbrains.kotlin.ir.expressions.IrConstructorCall
import org.jetbrains.kotlin.ir.expressions.IrExpression
import org.jetbrains.kotlin.name.ClassId

class TrimEndFitter : ValueFitter {
    override val annotationClassId: ClassId = KonditionConsts.TrimEndClassId

    override fun IrBuilderWithScope.produceFitVariable(
        irContext: KonditionIrContext,
        parentDeclaration: IrDeclarationParent,
        originalValue: IrExpression,
        annotation: IrConstructorCall,
    ): IrExpression = irCall(irContext.trimEnd).apply {
        setExtensionReceiver(originalValue)
    }
}
