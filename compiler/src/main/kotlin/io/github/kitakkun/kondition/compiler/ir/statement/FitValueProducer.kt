package io.github.kitakkun.kondition.compiler.ir.statement

import io.github.kitakkun.kondition.compiler.ir.KonditionIrContext
import io.github.kitakkun.kondition.compiler.ir.fitting.ValueFitter
import org.jetbrains.kotlin.ir.builders.IrBuilderWithScope
import org.jetbrains.kotlin.ir.builders.irGet
import org.jetbrains.kotlin.ir.declarations.IrDeclarationParent
import org.jetbrains.kotlin.ir.declarations.IrValueDeclaration
import org.jetbrains.kotlin.ir.expressions.IrExpression
import org.jetbrains.kotlin.ir.expressions.IrGetValue
import org.jetbrains.kotlin.ir.symbols.UnsafeDuringIrConstructionAPI
import org.jetbrains.kotlin.ir.types.classOrNull
import org.jetbrains.kotlin.ir.util.classId

class FitValueProducer(
    private val valueFitters: List<ValueFitter>,
) {
    @OptIn(UnsafeDuringIrConstructionAPI::class)
    fun IrBuilderWithScope.produce(
        irContext: KonditionIrContext,
        parent: IrDeclarationParent,
        value: IrValueDeclaration,
    ): IrValueDeclaration? {
        var fittedValueInitializer: IrExpression = irGet(value)

        valueFitters.forEach { valueFitter ->
            val annotation = value.annotations.firstOrNull {
                it.type.classOrNull?.owner?.classId == valueFitter.annotationClassId
            } ?: return@forEach

            fittedValueInitializer = with(valueFitter) {
                produceFitVariable(
                    irContext = irContext,
                    parentDeclaration = value.parent,
                    originalValue = fittedValueInitializer,
                    annotation = annotation,
                )
            }
        }

        if (fittedValueInitializer is IrGetValue && fittedValueInitializer.symbol == value.symbol) return null

        return scope.createTemporaryVariableDeclaration(
            irType = value.type,
            startOffset = startOffset,
            endOffset = endOffset,
        ).apply {
            this.initializer = fittedValueInitializer
            this.parent = parent
        }
    }
}
