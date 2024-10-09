package io.github.kitakkun.kondition.compiler.ir.transformer

import io.github.kitakkun.kondition.compiler.ir.KonditionIrContext
import org.jetbrains.kotlin.backend.common.lower.createIrBuilder
import org.jetbrains.kotlin.backend.common.lower.irBlockBody
import org.jetbrains.kotlin.ir.IrStatement
import org.jetbrains.kotlin.ir.builders.IrBuilderWithScope
import org.jetbrains.kotlin.ir.builders.irCall
import org.jetbrains.kotlin.ir.declarations.IrFunction
import org.jetbrains.kotlin.ir.declarations.IrValueParameter
import org.jetbrains.kotlin.ir.expressions.IrBlockBody
import org.jetbrains.kotlin.ir.expressions.IrConstructorCall
import org.jetbrains.kotlin.ir.expressions.IrExpression
import org.jetbrains.kotlin.ir.expressions.IrExpressionBody
import org.jetbrains.kotlin.ir.symbols.IrClassSymbol
import org.jetbrains.kotlin.ir.types.classOrNull
import org.jetbrains.kotlin.ir.util.hasAnnotation
import org.jetbrains.kotlin.ir.visitors.IrElementTransformerVoid

abstract class ValueParameterCheckStatementsProducer(
    private val irContext: KonditionIrContext
) : IrElementTransformerVoid() {
    abstract val annotationClassSymbol: IrClassSymbol

    abstract fun IrBuilderWithScope.generateMessageExpression(
        parentDeclaration: IrFunction,
        valueParameter: IrValueParameter,
        annotation: IrConstructorCall?,
    ): IrExpression?

    abstract fun IrBuilderWithScope.produceRequiredCondition(
        irContext: KonditionIrContext,
        parentDeclaration: IrFunction,
        valueParameter: IrValueParameter,
        annotation: IrConstructorCall?,
    ): IrExpression?

    final override fun visitFunction(declaration: IrFunction): IrStatement {
        val annotatedValueParameters = declaration.valueParameters.filter { it.hasAnnotation(annotationClassSymbol) }
        if (annotatedValueParameters.isEmpty()) return super.visitFunction(declaration)

        val irBuilder = irContext.irBuiltIns.createIrBuilder(declaration.symbol)
        val statementsToAdd = annotatedValueParameters.map { valueParameter ->
            val annotation = valueParameter.annotations.find { it.type.classOrNull == annotationClassSymbol }
            val condition = irBuilder.produceRequiredCondition(
                irContext = irContext,
                parentDeclaration = declaration,
                valueParameter = valueParameter,
                annotation = annotation,
            )
            val message = irBuilder.generateMessageExpression(declaration, valueParameter, annotation)
            if (condition != null) {
                irBuilder.irCall(irContext.requireFunction).apply {
                    putValueArgument(0, condition)
                    putValueArgument(1, message)
                }
            } else {
                null
            }
        }.filterNotNull()

        val body = declaration.body
        when (body) {
            is IrBlockBody -> body.statements.addAll(0, statementsToAdd)
            is IrExpressionBody -> irBuilder.irBlockBody(body.expression) { +statementsToAdd }
        }
        declaration.body = body

        return super.visitFunction(declaration)
    }
}
