package io.github.kitakkun.kondition.compiler.backend.fitting

import io.github.kitakkun.kondition.compiler.common.KonditionConsts
import io.github.kitakkun.kondition.compiler.backend.KonditionIrContext
import org.jetbrains.kotlin.ir.builders.IrBuilderWithScope
import org.jetbrains.kotlin.ir.builders.irCall
import org.jetbrains.kotlin.ir.declarations.IrDeclarationParent
import org.jetbrains.kotlin.ir.expressions.IrConstructorCall
import org.jetbrains.kotlin.ir.expressions.IrExpression
import org.jetbrains.kotlin.name.ClassId

class ToLowerCaseFitter : ValueFitter {
    override val annotationClassId: ClassId = KonditionConsts.ToLowerCaseClassId
    override fun IrBuilderWithScope.produceFitVariable(
        irContext: KonditionIrContext,
        parentDeclaration: IrDeclarationParent,
        originalValue: IrExpression,
        annotation: IrConstructorCall,
    ): IrExpression {
        val function = when (originalValue.type) {
            irContext.irBuiltIns.charType -> irContext.lowercaseChar
            irContext.irBuiltIns.stringType -> irContext.lowercaseString
            else -> error("Unsupported type ${originalValue.type}")
        }
        return irCall(function).apply {
            extensionReceiver = originalValue
        }
    }
}
