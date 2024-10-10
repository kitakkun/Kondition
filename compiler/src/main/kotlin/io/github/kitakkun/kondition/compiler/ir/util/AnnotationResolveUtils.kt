package io.github.kitakkun.kondition.compiler.ir.util

import org.jetbrains.kotlin.ir.expressions.IrConst
import org.jetbrains.kotlin.ir.expressions.IrConstructorCall
import org.jetbrains.kotlin.ir.expressions.IrGetEnumValue
import org.jetbrains.kotlin.ir.symbols.UnsafeDuringIrConstructionAPI
import kotlin.enums.EnumEntries

/**
 * When an annotation constructor has an enum value argument,
 * passed expression should have type of `IrGetEnumValue`.
 * We can extract the enum entry's name from it, then convert it to the actual enum value.
 */
@OptIn(UnsafeDuringIrConstructionAPI::class)
inline fun <reified E : Enum<E>> IrConstructorCall.getEnumValueArgument(index: Int, enumEntries: EnumEntries<E>, defaultValue: E): E {
    val enumName = (getValueArgument(index) as? IrGetEnumValue)?.symbol?.owner?.name?.asString()
    return enumEntries.find { it.name == enumName } ?: defaultValue
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
