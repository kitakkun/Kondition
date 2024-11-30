package com.kitakkun.kondition.compiler.backend.api

import org.jetbrains.kotlin.ir.expressions.IrConst
import org.jetbrains.kotlin.ir.expressions.IrConstructorCall

object VersionSpecificAPIImpl : VersionSpecificAPI {
    @Suppress("UNCHECKED_CAST")
    override fun <T> getConstArgument(irConstructorCall: IrConstructorCall, index: Int): T? {
        val irConst = irConstructorCall.getValueArgument(index) as? IrConst<*>
        return irConst?.value as? T
    }
}
