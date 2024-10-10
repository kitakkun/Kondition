package io.github.kitakkun.kondition.compiler.ir.transformer

import io.github.kitakkun.kondition.compiler.ir.KonditionIrContext
import io.github.kitakkun.kondition.compiler.ir.statement.StatementsProducer
import org.jetbrains.kotlin.backend.common.lower.createIrBuilder
import org.jetbrains.kotlin.backend.common.lower.irBlockBody
import org.jetbrains.kotlin.ir.IrStatement
import org.jetbrains.kotlin.ir.declarations.IrFunction
import org.jetbrains.kotlin.ir.expressions.IrBlockBody
import org.jetbrains.kotlin.ir.expressions.IrExpressionBody
import org.jetbrains.kotlin.ir.symbols.UnsafeDuringIrConstructionAPI
import org.jetbrains.kotlin.ir.visitors.IrElementTransformerVoid

class ValueParameterCheckStatementsProducer(
    private val irContext: KonditionIrContext,
    private val statementsProducer: StatementsProducer,
) : IrElementTransformerVoid() {
    @OptIn(UnsafeDuringIrConstructionAPI::class)
    override fun visitFunction(declaration: IrFunction): IrStatement {
        val irBuilder = irContext.irBuiltIns.createIrBuilder(declaration.symbol)

        val additionalStatements = with(statementsProducer) {
            declaration.valueParameters.flatMap {
                irBuilder.produce(
                    irContext = irContext,
                    parentDeclaration = declaration,
                    value = it,
                )
            }
        }

        val body = declaration.body
        when (body) {
            is IrBlockBody -> body.statements.addAll(0, additionalStatements)
            is IrExpressionBody -> irBuilder.irBlockBody(body.expression) {
                +body.expression
                +additionalStatements
            }
        }

        declaration.body = body

        return super.visitFunction(declaration)
    }
}
