@file:Suppress("UNUSED")

package io.github.kitakkun.kondition.core.internal

@KonditionInternalApi
internal fun String.matchRegex(pattern: String): Boolean = Regex(pattern).matches(this)

@KonditionInternalApi
internal fun String.isAlpha(): Boolean = this.all { it.isLetter() }

@KonditionInternalApi
internal fun String.isNumeric(): Boolean = this.all { it.isDigit() }
