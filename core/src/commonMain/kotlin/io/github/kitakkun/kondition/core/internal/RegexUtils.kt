@file:Suppress("UNUSED")

package io.github.kitakkun.kondition.core.internal

@KonditionInternalApi
fun String.matchRegex(pattern: String): Boolean = Regex(pattern).matches(this)
