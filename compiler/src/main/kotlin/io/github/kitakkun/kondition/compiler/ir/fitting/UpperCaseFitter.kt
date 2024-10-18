package io.github.kitakkun.kondition.compiler.ir.fitting

import io.github.kitakkun.kondition.compiler.KonditionConsts
import io.github.kitakkun.kondition.compiler.ir.KonditionIrContext
import org.jetbrains.kotlin.ir.builders.IrBuilderWithScope
import org.jetbrains.kotlin.ir.builders.irCall
import org.jetbrains.kotlin.ir.declarations.IrDeclarationParent
import org.jetbrains.kotlin.ir.expressions.IrConstructorCall
import org.jetbrains.kotlin.ir.expressions.IrExpression
import org.jetbrains.kotlin.name.ClassId

class UpperCaseFitter : ValueFitter {
    override val annotationClassId: ClassId = KonditionConsts.UpperCaseClassId
    override fun IrBuilderWithScope.produceFitVariable(
        irContext: KonditionIrContext,
        parentDeclaration: IrDeclarationParent,
        originalValue: IrExpression,
        annotation: IrConstructorCall
    ): IrExpression {
        val function = when (originalValue.type) {
            irContext.irBuiltIns.charType -> irContext.uppercaseChar
            irContext.irBuiltIns.stringType -> irContext.uppercaseString
            else -> error("Unsupported type ${originalValue.type}")
        }
        return irCall(function).apply {
            extensionReceiver = originalValue
        }
    }
}
