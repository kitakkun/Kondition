@file:Suppress("UNUSED")

package io.github.kitakkun.kondition.core.internal

@KonditionInternalApi
internal fun require(condition: Boolean, message: String? = null) {
    if (message != null) {
        require(value = condition) { message }
    } else {
        require(value = condition)
    }
}
