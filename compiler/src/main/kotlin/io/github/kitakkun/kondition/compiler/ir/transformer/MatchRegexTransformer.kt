package io.github.kitakkun.kondition.compiler.ir.transformer

import io.github.kitakkun.kondition.compiler.ir.KonditionIrContext
import org.jetbrains.kotlin.backend.jvm.ir.getStringConstArgument
import org.jetbrains.kotlin.ir.builders.IrBuilderWithScope
import org.jetbrains.kotlin.ir.builders.irCall
import org.jetbrains.kotlin.ir.builders.irGet
import org.jetbrains.kotlin.ir.builders.irString
import org.jetbrains.kotlin.ir.declarations.IrFunction
import org.jetbrains.kotlin.ir.declarations.IrValueParameter
import org.jetbrains.kotlin.ir.expressions.IrConstructorCall
import org.jetbrains.kotlin.ir.expressions.IrExpression
import org.jetbrains.kotlin.ir.symbols.IrClassSymbol

/**
 * Transformer for [io.github.kitakkun.kondition.core.annotation.MatchRegex] annotation.
 */
class MatchRegexTransformer(irContext: KonditionIrContext) : ValueParameterCheckStatementsProducer(irContext) {
    override val annotationClassSymbol: IrClassSymbol = irContext.matchRegexAnnotation

    override fun IrBuilderWithScope.generateMessageExpression(
        parentDeclaration: IrFunction,
        valueParameter: IrValueParameter,
        annotation: IrConstructorCall?
    ): IrExpression? {
        val pattern = annotation?.getStringConstArgument(0) ?: return null
        return irString("${valueParameter.name} doesn't match the regex pattern \"$pattern\"")
    }

    override fun IrBuilderWithScope.produceRequiredCondition(
        irContext: KonditionIrContext,
        parentDeclaration: IrFunction,
        valueParameter: IrValueParameter,
        annotation: IrConstructorCall?,
    ): IrExpression? {
        val pattern = annotation?.getStringConstArgument(0) ?: return null

        return irCall(irContext.matchRegexFunction).apply {
            extensionReceiver = irGet(valueParameter)
            putValueArgument(0, irString(pattern))
        }
    }
}
