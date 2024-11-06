package io.github.kitakkun.kondition.compiler.backend.util

import org.jetbrains.kotlin.ir.expressions.IrConst
import org.jetbrains.kotlin.ir.expressions.IrConstructorCall
import org.jetbrains.kotlin.ir.expressions.IrGetEnumValue
import org.jetbrains.kotlin.ir.symbols.UnsafeDuringIrConstructionAPI

@OptIn(UnsafeDuringIrConstructionAPI::class)
fun IrConstructorCall.getEnumNameOfArgument(index: Int): String? {
    val irGetEnumValue = getValueArgument(index) as? IrGetEnumValue ?: return null
    return irGetEnumValue.symbol.owner.name.asString()
}

/**
 * Primitive constants passed to annotation constructors have IrConst<*> type.
 * We can get its value directly from IrConst<*>.value, then cast it to the proper type.
 */
@Suppress("UNCHECKED_CAST")
fun <T> IrConstructorCall.getConstArgument(index: Int): T? {
    val irConst = getValueArgument(index) as? IrConst<*>
    return irConst?.value as? T
}
