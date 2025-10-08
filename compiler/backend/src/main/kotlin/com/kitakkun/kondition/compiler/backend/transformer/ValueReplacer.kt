package com.kitakkun.kondition.compiler.backend.transformer

import com.kitakkun.kondition.compiler.backend.KonditionIrContext
import org.jetbrains.kotlin.backend.common.lower.createIrBuilder
import org.jetbrains.kotlin.ir.builders.irGet
import org.jetbrains.kotlin.ir.declarations.IrValueDeclaration
import org.jetbrains.kotlin.ir.expressions.IrExpression
import org.jetbrains.kotlin.ir.expressions.IrGetValue
import org.jetbrains.kotlin.ir.symbols.IrValueSymbol
import org.jetbrains.kotlin.ir.visitors.IrElementTransformerVoid

class ValueReplacer(
    private val irContext: KonditionIrContext,
    private val replacementMap: Map<IrValueSymbol, IrValueDeclaration>,
) : IrElementTransformerVoid() {
    override fun visitGetValue(expression: IrGetValue): IrExpression {
        replacementMap.toList().firstOrNull { it.first == expression.symbol }?.let {
            return irContext.irBuiltIns.createIrBuilder(expression.symbol).irGet(it.second)
        }
        return super.visitGetValue(expression)
    }
}
