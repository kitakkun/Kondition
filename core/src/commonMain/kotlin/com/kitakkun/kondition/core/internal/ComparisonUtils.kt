@file:Suppress("UNUSED")

package com.kitakkun.kondition.core.internal

// this > other
@KonditionInternalApi
internal fun <T> Comparable<T>.greaterThan(other: T): Boolean = compareTo(other) > 0

// this < other
@KonditionInternalApi
internal fun <T> Comparable<T>.lessThan(other: T): Boolean = compareTo(other) < 0

// this >= other
@KonditionInternalApi
internal fun <T> Comparable<T>.greaterThanOrEquals(other: T): Boolean = compareTo(other) >= 0

// this <= other
@KonditionInternalApi
internal fun <T> Comparable<T>.lessThanOrEquals(other: T): Boolean = compareTo(other) <= 0
