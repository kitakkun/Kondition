package com.kitakkun.kondition.compiler.backend.util

import org.jetbrains.kotlin.ir.declarations.IrParameterKind
import org.jetbrains.kotlin.ir.expressions.IrExpression
import org.jetbrains.kotlin.ir.expressions.IrMemberAccessExpression
import org.jetbrains.kotlin.ir.symbols.IrFunctionSymbol
import org.jetbrains.kotlin.ir.types.IrType

fun IrMemberAccessExpression<IrFunctionSymbol>.setExtensionReceiver(expression: IrExpression) {
    arguments[symbol.owner.parameters.indexOfFirst { it.kind == IrParameterKind.ExtensionReceiver }] = expression
}

fun IrMemberAccessExpression<IrFunctionSymbol>.getExtensionReceiverOrNull(): IrExpression? {
    val index = symbol.owner.parameters.indexOfFirst { it.kind == IrParameterKind.ExtensionReceiver }
    return arguments.getOrNull(index)
}

fun IrMemberAccessExpression<IrFunctionSymbol>.setValueArgument(index: Int, value: IrExpression?) {
    val index = symbol.owner.parameters.filter { it.kind == IrParameterKind.Regular }.getOrNull(index)?.indexInParameters
    if (index != null && index != -1) arguments[index] = value
}

fun IrMemberAccessExpression<IrFunctionSymbol>.setTypeArgument(index: Int, type: IrType) {
    this.typeArguments[index] = type
}
