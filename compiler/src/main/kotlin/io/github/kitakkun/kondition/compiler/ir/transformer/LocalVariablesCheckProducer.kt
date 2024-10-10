package io.github.kitakkun.kondition.compiler.ir.transformer

import io.github.kitakkun.kondition.compiler.ir.KonditionIrContext
import io.github.kitakkun.kondition.compiler.ir.statement.StatementsProducer
import io.github.kitakkun.kondition.compiler.ir.visitor.LocalVariableCollector
import org.jetbrains.kotlin.backend.common.lower.createIrBuilder
import org.jetbrains.kotlin.backend.common.lower.irComposite
import org.jetbrains.kotlin.ir.IrStatement
import org.jetbrains.kotlin.ir.declarations.IrFunction
import org.jetbrains.kotlin.ir.declarations.IrVariable
import org.jetbrains.kotlin.ir.expressions.IrExpression
import org.jetbrains.kotlin.ir.expressions.IrSetValue
import org.jetbrains.kotlin.ir.symbols.UnsafeDuringIrConstructionAPI
import org.jetbrains.kotlin.ir.visitors.IrElementTransformerVoid
import org.jetbrains.kotlin.ir.visitors.transformChildrenVoid

class LocalVariablesCheckProducer(
    private val irContext: KonditionIrContext,
    private val statementsProducer: StatementsProducer,
) : IrElementTransformerVoid() {
    private val localVariableCollector = LocalVariableCollector()

    @OptIn(UnsafeDuringIrConstructionAPI::class)
    override fun visitFunction(declaration: IrFunction): IrStatement {
        val localVariables = localVariableCollector.collectFunctionLocalVariables(declaration)

        val body = declaration.body ?: return super.visitFunction(declaration)

        // to reference function declaration inside child transformer...
        val function = declaration

        body.transformChildrenVoid(
            object : IrElementTransformerVoid() {
                override fun visitSetValue(expression: IrSetValue): IrExpression {
                    val correspondingLocalVariable = localVariables.find { it.symbol == expression.symbol } ?: return super.visitSetValue(expression)

                    return with(irContext.irBuiltIns.createIrBuilder(expression.symbol)) {
                        irComposite(expression) {
                            +expression
                            +with(statementsProducer) {
                                produce(
                                    irContext = irContext,
                                    value = correspondingLocalVariable,
                                    parentDeclaration = declaration,
                                )
                            }
                        }
                    }
                }

                override fun visitVariable(declaration: IrVariable): IrStatement {
                    val correspondingLocalVariable = localVariables.find { it.symbol == declaration.symbol } ?: return super.visitVariable(declaration)
                    val initializer = correspondingLocalVariable.initializer ?: return super.visitVariable(declaration)

                    return with(irContext.irBuiltIns.createIrBuilder(correspondingLocalVariable.symbol)) {
                        irComposite(initializer) {
                            +declaration
                            +with(statementsProducer) {
                                produce(
                                    irContext = irContext,
                                    value = correspondingLocalVariable,
                                    parentDeclaration = function,
                                )
                            }
                        }
                    }
                }
            },
        )

        return super.visitFunction(declaration)
    }
}
