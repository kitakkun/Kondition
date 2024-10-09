package io.github.kitakkun.kondition.compiler.ir.transformer

import io.github.kitakkun.kondition.compiler.ir.KonditionIrContext
import org.jetbrains.kotlin.ir.IrStatement
import org.jetbrains.kotlin.ir.builders.IrBuilderWithScope
import org.jetbrains.kotlin.ir.builders.irCall
import org.jetbrains.kotlin.ir.builders.irGet
import org.jetbrains.kotlin.ir.declarations.IrFunction
import org.jetbrains.kotlin.ir.declarations.IrValueParameter
import org.jetbrains.kotlin.ir.expressions.IrConstructorCall
import org.jetbrains.kotlin.ir.symbols.IrClassSymbol

/**
 * Transformer for [io.github.kitakkun.kondition.core.annotation.NonBlank] annotation.
 */
class NonBlankTransformer(irContext: KonditionIrContext) : ValueParameterCheckStatementsProducer(irContext) {
    override val annotationClassSymbol: IrClassSymbol = irContext.nonBlankAnnotation

    override fun IrBuilderWithScope.produceRequiredFunctionCall(
        irContext: KonditionIrContext,
        parentDeclaration: IrFunction,
        valueParameter: IrValueParameter,
        annotation: IrConstructorCall?,
    ): IrStatement = irCall(irContext.requireFunction).apply {
        putValueArgument(
            index = 0,
            valueArgument = irCall(irContext.isNotBlankFunction).apply {
                extensionReceiver = irGet(valueParameter)
            },
        )
    }
}
