package com.kitakkun.kondition.compiler.backend.util

import org.jetbrains.kotlin.ir.declarations.IrFunction
import org.jetbrains.kotlin.ir.declarations.IrParameterKind
import org.jetbrains.kotlin.ir.declarations.IrValueParameter

fun IrFunction.extensionReceiverParameterOrNull(): IrValueParameter? {
    return this.parameters.firstOrNull { it.kind == IrParameterKind.ExtensionReceiver }
}

fun IrFunction.dispatchReceiverParameterOrNull(): IrValueParameter? {
    return this.parameters.firstOrNull { it.kind == IrParameterKind.DispatchReceiver }
}

fun IrFunction.valueParameters(): List<IrValueParameter> {
    return this.parameters.filter { it.kind == IrParameterKind.Regular }
}
