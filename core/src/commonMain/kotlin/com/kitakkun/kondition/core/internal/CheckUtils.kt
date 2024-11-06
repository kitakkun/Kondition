@file:Suppress("UNUSED")

package com.kitakkun.kondition.core.internal

@KonditionInternalApi
internal fun require(condition: Boolean, message: String? = null) {
    if (message != null) {
        require(value = condition) { message }
    } else {
        require(value = condition)
    }
}

@KonditionInternalApi
internal fun check(condition: Boolean, message: String? = null) {
    if (message != null) {
        check(value = condition) { message }
    } else {
        check(value = condition)
    }
}
