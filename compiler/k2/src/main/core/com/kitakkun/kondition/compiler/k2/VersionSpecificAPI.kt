package com.kitakkun.kondition.compiler.k2

import org.jetbrains.kotlin.fir.FirSession
import org.jetbrains.kotlin.fir.expressions.FirExpression

interface VersionSpecificAPI {
    companion object {
        lateinit var INSTANCE: VersionSpecificAPI
    }

    fun evaluateAsLiteralValue(originalExpression: FirExpression, session: FirSession): Any?
}
