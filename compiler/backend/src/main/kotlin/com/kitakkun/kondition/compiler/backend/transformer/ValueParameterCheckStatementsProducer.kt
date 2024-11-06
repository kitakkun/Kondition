package com.kitakkun.kondition.compiler.backend.transformer

import com.kitakkun.kondition.compiler.backend.KonditionIrContext
import com.kitakkun.kondition.compiler.backend.statement.FitValueProducer
import com.kitakkun.kondition.compiler.backend.statement.StatementsProducer
import org.jetbrains.kotlin.backend.common.lower.createIrBuilder
import org.jetbrains.kotlin.backend.common.lower.irBlockBody
import org.jetbrains.kotlin.ir.IrStatement
import org.jetbrains.kotlin.ir.builders.irGet
import org.jetbrains.kotlin.ir.declarations.IrFunction
import org.jetbrains.kotlin.ir.declarations.IrValueDeclaration
import org.jetbrains.kotlin.ir.expressions.IrBlockBody
import org.jetbrains.kotlin.ir.expressions.IrExpressionBody
import org.jetbrains.kotlin.ir.expressions.IrGetValue
import org.jetbrains.kotlin.ir.symbols.IrValueSymbol
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

        val body = declaration.body

        val replacementMap: Map<IrValueSymbol, IrValueDeclaration> = with(fitValueProducer) {
            declaration.valueParameters
                .mapNotNull {
                    val fittedValue = irBuilder.fitExpression(
                        irContext = irContext,
                        expression = irBuilder.irGet(it),
                        parentDeclaration = declaration,
                        annotations = it.annotations,
                    )
                    if (fittedValue is IrGetValue && fittedValue.symbol == it.symbol) return@mapNotNull null
                    val fittedValueDeclaration = irBuilder.scope.createTemporaryVariableDeclaration(
                        irType = fittedValue.type,
                        startOffset = irBuilder.startOffset,
                        endOffset = irBuilder.endOffset,
                    ).apply {
                        this.initializer = fittedValue
                        this.parent = declaration
                    }
                    it.symbol to fittedValueDeclaration
                }.toMap()
        }
        body?.transformChildrenVoid(ValueReplacer(irContext, replacementMap))

        when (body) {
            is IrBlockBody -> body.statements.addAll(0, additionalStatements + replacementMap.values.filterNotNull())
            is IrExpressionBody -> irBuilder.irBlockBody(body.expression) {
                +(additionalStatements + replacementMap.values)
                +body.expression
            }
        }

        declaration.body = body

        return super.visitFunction(declaration)
    }
}
