package com.kitakkun.kondition.compiler.backend.api

import org.jetbrains.kotlin.ir.expressions.IrConstructorCall

interface VersionSpecificAPI {
    companion object {
        lateinit var INSTANCE: VersionSpecificAPI
    }

    fun <T> getConstArgument(irConstructorCall: IrConstructorCall, index: Int): T?
}
