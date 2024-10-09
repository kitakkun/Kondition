package io.github.kitakkun.kondition.compiler.ir.transformer

import io.github.kitakkun.kondition.compiler.ir.KonditionIrContext
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
 * Transformer for [io.github.kitakkun.kondition.core.annotation.NonEmpty] annotation.
 */
class NonEmptyTransformer(irContext: KonditionIrContext) : ValueParameterCheckStatementsProducer(irContext) {
    override val annotationClassSymbol: IrClassSymbol = irContext.nonEmptyAnnotation
    override fun IrBuilderWithScope.generateMessageExpression(
        parentDeclaration: IrFunction,
        valueParameter: IrValueParameter,
        annotation: IrConstructorCall?
    ): IrExpression? {
        return irString("${valueParameter.name} in ${parentDeclaration.name} can't be empty.")
    }

    override fun IrBuilderWithScope.produceRequiredCondition(
        irContext: KonditionIrContext,
        parentDeclaration: IrFunction,
        valueParameter: IrValueParameter,
        annotation: IrConstructorCall?,
    ): IrExpression {
        return irCall(irContext.isNotEmptyFunction).apply {
            extensionReceiver = irGet(valueParameter)
        }
    }
}
