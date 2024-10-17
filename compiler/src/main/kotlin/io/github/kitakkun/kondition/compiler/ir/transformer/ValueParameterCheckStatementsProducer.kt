package io.github.kitakkun.kondition.compiler.ir.transformer

import io.github.kitakkun.kondition.compiler.ir.KonditionIrContext
import io.github.kitakkun.kondition.compiler.ir.statement.FitValueProducer
import io.github.kitakkun.kondition.compiler.ir.statement.StatementsProducer
import org.jetbrains.kotlin.backend.common.lower.createIrBuilder
import org.jetbrains.kotlin.backend.common.lower.irBlockBody
import org.jetbrains.kotlin.ir.IrStatement
import org.jetbrains.kotlin.ir.builders.irGet
import org.jetbrains.kotlin.ir.declarations.IrFunction
import org.jetbrains.kotlin.ir.expressions.IrBlockBody
import org.jetbrains.kotlin.ir.expressions.IrExpression
import org.jetbrains.kotlin.ir.expressions.IrExpressionBody
import org.jetbrains.kotlin.ir.expressions.IrGetValue
import org.jetbrains.kotlin.ir.symbols.UnsafeDuringIrConstructionAPI
import org.jetbrains.kotlin.ir.visitors.IrElementTransformerVoid
import org.jetbrains.kotlin.ir.visitors.transformChildrenVoid

class ValueParameterCheckStatementsProducer(
    private val irContext: KonditionIrContext,
    private val statementsProducer: StatementsProducer,
    private val fitValueProducer: FitValueProducer,
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

        val fittedValues = with(fitValueProducer) {
            declaration.valueParameters.associateWith {
                irBuilder.produce(
                    irContext = irContext,
                    parent = declaration,
                    value = it,
                )
            }
        }

        val body = declaration.body

        body?.transformChildrenVoid(
            object : IrElementTransformerVoid() {
                override fun visitGetValue(expression: IrGetValue): IrExpression {
                    val targetEntry = fittedValues.toList().firstOrNull { it.first.symbol == expression.symbol }
                    val replaceTarget = targetEntry?.second?.symbol
                    if (replaceTarget != null) {
                        return irBuilder.irGet(replaceTarget.owner)
                    }
                    return expression
                }
            }
        )

        when (body) {
            is IrBlockBody -> body.statements.addAll(0, additionalStatements + fittedValues.values.filterNotNull())
            is IrExpressionBody -> irBuilder.irBlockBody(body.expression) {
                +(additionalStatements + fittedValues.values.filterNotNull())
                +body.expression
            }
        }

        declaration.body = body

        return super.visitFunction(declaration)
    }
}
