package io.github.kitakkun.kondition.compiler.ir.statement

import io.github.kitakkun.kondition.compiler.ir.KonditionIrContext
import io.github.kitakkun.kondition.compiler.ir.fitting.ValueFitter
import org.jetbrains.kotlin.ir.builders.IrBuilderWithScope
import org.jetbrains.kotlin.ir.declarations.IrDeclarationParent
import org.jetbrains.kotlin.ir.expressions.IrConstructorCall
import org.jetbrains.kotlin.ir.expressions.IrExpression
import org.jetbrains.kotlin.ir.symbols.UnsafeDuringIrConstructionAPI
import org.jetbrains.kotlin.ir.types.classOrNull
import org.jetbrains.kotlin.ir.util.classId

class FitValueProducer(
    private val valueFitters: List<ValueFitter>,
) {
    @OptIn(UnsafeDuringIrConstructionAPI::class)
    fun IrBuilderWithScope.fitExpression(
        irContext: KonditionIrContext,
        expression: IrExpression,
        annotations: List<IrConstructorCall>,
        parentDeclaration: IrDeclarationParent,
    ): IrExpression {
        var fittedExpression = expression

        valueFitters.forEach { valueFitter ->
            val annotation = annotations.firstOrNull {
                it.type.classOrNull?.owner?.classId == valueFitter.annotationClassId
            } ?: return@forEach

            fittedExpression = with(valueFitter) {
                produceFitVariable(
                    irContext = irContext,
                    parentDeclaration = parentDeclaration,
                    originalValue = fittedExpression,
                    annotation = annotation,
                )
            }
        }

        return fittedExpression
    }
}
