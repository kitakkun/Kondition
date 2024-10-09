package io.github.kitakkun.kondition.compiler.ir.transformer

import io.github.kitakkun.kondition.compiler.ir.KonditionIrContext
import org.jetbrains.kotlin.backend.common.lower.createIrBuilder
import org.jetbrains.kotlin.backend.common.lower.irBlockBody
import org.jetbrains.kotlin.ir.IrStatement
import org.jetbrains.kotlin.ir.builders.IrBuilderWithScope
import org.jetbrains.kotlin.ir.declarations.IrFunction
import org.jetbrains.kotlin.ir.declarations.IrValueParameter
import org.jetbrains.kotlin.ir.expressions.IrBlockBody
import org.jetbrains.kotlin.ir.expressions.IrConstructorCall
import org.jetbrains.kotlin.ir.expressions.IrExpressionBody
import org.jetbrains.kotlin.ir.symbols.IrClassSymbol
import org.jetbrains.kotlin.ir.types.classOrNull
import org.jetbrains.kotlin.ir.util.hasAnnotation
import org.jetbrains.kotlin.ir.visitors.IrElementTransformerVoid

abstract class ValueParameterCheckStatementsProducer(private val irContext: KonditionIrContext) : IrElementTransformerVoid() {
    abstract val annotationClassSymbol: IrClassSymbol

    abstract fun IrBuilderWithScope.produceRequiredFunctionCall(
        irContext: KonditionIrContext,
        parentDeclaration: IrFunction,
        valueParameter: IrValueParameter,
        annotation: IrConstructorCall?,
    ): IrStatement?

    override fun visitFunction(declaration: IrFunction): IrStatement {
        val annotatedValueParameters = declaration.valueParameters.filter { it.hasAnnotation(annotationClassSymbol) }
        if (annotatedValueParameters.isEmpty()) return super.visitFunction(declaration)

        val irBuilder = irContext.irBuiltIns.createIrBuilder(declaration.symbol)
        val statementsToAdd = annotatedValueParameters.map { valueParameter ->
            irBuilder.produceRequiredFunctionCall(
                irContext = irContext,
                parentDeclaration = declaration,
                valueParameter = valueParameter,
                annotation = valueParameter.annotations.find { it.type.classOrNull == annotationClassSymbol },
            )
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
