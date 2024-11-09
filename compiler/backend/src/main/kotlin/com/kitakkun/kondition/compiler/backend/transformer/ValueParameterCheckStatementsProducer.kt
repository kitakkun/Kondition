package com.kitakkun.kondition.compiler.backend.transformer

import com.kitakkun.kondition.compiler.backend.KonditionIrContext
import com.kitakkun.kondition.compiler.backend.statement.FitValueProducer
import com.kitakkun.kondition.compiler.backend.statement.StatementsProducer
import org.jetbrains.kotlin.backend.common.lower.createIrBuilder
import org.jetbrains.kotlin.backend.common.lower.irBlockBody
import org.jetbrains.kotlin.ir.IrStatement
import org.jetbrains.kotlin.ir.builders.irExprBody
import org.jetbrains.kotlin.ir.builders.irGet
import org.jetbrains.kotlin.ir.declarations.IrConstructor
import org.jetbrains.kotlin.ir.declarations.IrField
import org.jetbrains.kotlin.ir.declarations.IrFunction
import org.jetbrains.kotlin.ir.declarations.IrValueDeclaration
import org.jetbrains.kotlin.ir.expressions.*
import org.jetbrains.kotlin.ir.symbols.IrValueSymbol
import org.jetbrains.kotlin.ir.symbols.UnsafeDuringIrConstructionAPI
import org.jetbrains.kotlin.ir.visitors.IrElementTransformerVoid
import org.jetbrains.kotlin.ir.visitors.transformChildrenVoid

class ValueParameterCheckStatementsProducer(
    private val irContext: KonditionIrContext,
    private val statementsProducer: StatementsProducer,
    private val fitValueProducer: FitValueProducer,
) : IrElementTransformerVoid() {
    override fun visitFunction(declaration: IrFunction): IrStatement {

        val irBuilder = irContext.irBuiltIns.createIrBuilder(declaration.symbol)

        val requireStatements = with(statementsProducer) {
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
            if (declaration is IrConstructor) {
                // Value fittings for value parameters on constructor functions can't be done here.
                // So, we need transform them in visitField instead.
                emptyMap()
            } else {
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
        }

        body?.transformChildrenVoid(ValueReplacer(irContext, replacementMap))

        val additionalStatements = requireStatements + replacementMap.values

        when (body) {
            is IrBlockBody -> body.statements.addAll(0, additionalStatements)
            is IrExpressionBody -> irBuilder.irBlockBody(body.expression) {
                +additionalStatements
                +body.expression
            }
        }

        declaration.body = body

        return super.visitFunction(declaration)
    }

    @OptIn(UnsafeDuringIrConstructionAPI::class)
    override fun visitField(declaration: IrField): IrStatement {
        val initializer = declaration.initializer ?: return super.visitField(declaration)
        val expression = initializer.expression as? IrGetValue ?: return super.visitField(declaration)
        if (expression.origin != IrStatementOrigin.INITIALIZE_PROPERTY_FROM_PARAMETER) return super.visitField(declaration)

        val irBuilder = irContext.irBuiltIns.createIrBuilder(declaration.symbol)
        val referencedValue = expression.symbol.owner

        val newInitializer: IrExpression? = with(fitValueProducer) {
            val fittedValue = irBuilder.fitExpression(
                irContext = irContext,
                expression = irBuilder.irGet(referencedValue),
                parentDeclaration = declaration,
                annotations = referencedValue.annotations,
            )
            if (fittedValue is IrGetValue && fittedValue.symbol == referencedValue.symbol) return@with null
            fittedValue
        }

        if (newInitializer == null) return super.visitField(declaration)

        declaration.initializer = irBuilder.irExprBody(newInitializer)
        return super.visitField(declaration)
    }
}
