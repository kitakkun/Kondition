@file:Suppress("UNUSED")

package com.kitakkun.kondition.core.annotation

enum class AbortStrategy {
    ThrowError,
    ReturnWithNull,
    Return,
}

annotation class AbortWith(val strategy: AbortStrategy)
