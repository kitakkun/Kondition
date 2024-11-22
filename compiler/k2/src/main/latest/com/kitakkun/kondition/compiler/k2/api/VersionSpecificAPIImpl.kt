package com.kitakkun.kondition.compiler.k2.api

import org.jetbrains.kotlin.fir.FirSession
import org.jetbrains.kotlin.fir.declarations.evaluateAs
import org.jetbrains.kotlin.fir.expressions.FirExpression
import org.jetbrains.kotlin.fir.expressions.FirLiteralExpression

object VersionSpecificAPIImpl : VersionSpecificAPI {
    override fun evaluateAsLiteralValue(originalExpression: FirExpression, session: FirSession): Any? {
        return originalExpression.evaluateAs<FirLiteralExpression>(session)?.value
    }
}
