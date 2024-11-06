@file:Suppress("UNUSED")

package com.kitakkun.kondition.core.internal

@KonditionInternalApi
internal fun String.matchRegex(pattern: String): Boolean = Regex(pattern).matches(this)

@KonditionInternalApi
internal fun String.isAlpha(): Boolean = this.all { it.isLetter() }

@KonditionInternalApi
internal fun String.isNumeric(): Boolean = this.all { it.isDigit() }

@KonditionInternalApi
internal fun String.lengthEquals(n: Int): Boolean = length == n

@KonditionInternalApi
internal fun String.lengthLessThanOrEquals(n: Int): Boolean = length <= n

@KonditionInternalApi
internal fun String.lengthGreaterThanOrEquals(n: Int): Boolean = length >= n
