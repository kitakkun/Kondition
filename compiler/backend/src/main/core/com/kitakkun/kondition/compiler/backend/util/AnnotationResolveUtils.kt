package com.kitakkun.kondition.compiler.backend.util

import com.kitakkun.kondition.compiler.backend.api.VersionSpecificAPI
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
    return VersionSpecificAPI.INSTANCE.getConstArgument<T>(this, index)
}
