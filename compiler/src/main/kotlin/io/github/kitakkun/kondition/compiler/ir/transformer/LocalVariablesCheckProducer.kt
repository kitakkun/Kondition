package io.github.kitakkun.kondition.compiler.ir.transformer

import io.github.kitakkun.kondition.compiler.ir.KonditionIrContext
import io.github.kitakkun.kondition.compiler.ir.statement.FitValueProducer
import io.github.kitakkun.kondition.compiler.ir.statement.StatementsProducer
import org.jetbrains.kotlin.backend.common.lower.createIrBuilder
import org.jetbrains.kotlin.backend.common.lower.irComposite
import org.jetbrains.kotlin.ir.IrStatement
import org.jetbrains.kotlin.ir.builders.irComposite
import org.jetbrains.kotlin.ir.declarations.IrFunction
import org.jetbrains.kotlin.ir.declarations.IrVariable
import org.jetbrains.kotlin.ir.expressions.IrExpression
import org.jetbrains.kotlin.ir.expressions.IrSetValue
import org.jetbrains.kotlin.ir.symbols.UnsafeDuringIrConstructionAPI
import org.jetbrains.kotlin.ir.util.isLocal
import org.jetbrains.kotlin.ir.visitors.IrElementTransformerVoid

class LocalVariablesCheckProducer(
    private val irContext: KonditionIrContext,
    private val statementsProducer: StatementsProducer,
    private val fitValueProducer: FitValueProducer,
) : IrElementTransformerVoid() {
    override fun visitVariable(declaration: IrVariable): IrStatement {
        if (!declaration.isLocal) return super.visitVariable(declaration)
        // If the initializer is null, the variable is not initialized at this point thus we can't check requirements.
        // ex)
        // val variable: Int <--- declaration
        // variable = 10 <--- initialized with different statement(will be detected as IrSetValue)
        val initializer = declaration.initializer ?: return super.visitVariable(declaration)

        val parent = declaration.parent as? IrFunction ?: return super.visitVariable(declaration)

        val irBuilder = irContext.irBuiltIns.createIrBuilder(declaration.symbol)

        val checkStatements = with(statementsProducer) {
            irBuilder.produce(
                irContext = irContext,
                value = declaration,
                parentDeclaration = parent,
            )
        }

        val fittedInitializer = with(fitValueProducer) {
            irBuilder.fitExpression(
                irContext = irContext,
                expression = initializer,
                parentDeclaration = parent,
                annotations = declaration.annotations,
            )
        }

        declaration.initializer = fittedInitializer

        if (checkStatements.isEmpty()) return super.visitVariable(declaration)

        return irBuilder.irComposite(declaration.startOffset, declaration.endOffset) {
            +declaration
            +checkStatements
        }
    }

    @OptIn(UnsafeDuringIrConstructionAPI::class)
    override fun visitSetValue(expression: IrSetValue): IrExpression {
        if (!expression.symbol.owner.isLocal) return super.visitSetValue(expression)

        val parent = expression.symbol.owner.parent as? IrFunction ?: return super.visitSetValue(expression)

        val irBuilder = irContext.irBuiltIns.createIrBuilder(expression.symbol)

        val checkStatements = with(statementsProducer) {
            irBuilder.produce(
                irContext = irContext,
                value = expression.symbol.owner,
                parentDeclaration = parent,
            )
        }

        val fittedValue = with(fitValueProducer) {
            irBuilder.fitExpression(
                irContext = irContext,
                expression = expression.value,
                parentDeclaration = parent,
                annotations = expression.symbol.owner.annotations,
            )
        }

        expression.value = fittedValue

        if (checkStatements.isEmpty()) return super.visitSetValue(expression)

        return irBuilder.irComposite(expression) {
            +expression
            +checkStatements
        }
    }
}
