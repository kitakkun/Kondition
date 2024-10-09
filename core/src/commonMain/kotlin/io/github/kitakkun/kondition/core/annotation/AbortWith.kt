@file:Suppress("UNUSED")

package io.github.kitakkun.kondition.core.annotation

enum class AbortStrategy {
    ThrowError,
    ReturnWithNull,
    Return,
}

annotation class AbortWith(val strategy: AbortStrategy)
