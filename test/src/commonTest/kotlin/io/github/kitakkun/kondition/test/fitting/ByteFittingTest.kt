package io.github.kitakkun.kondition.test.fitting

import io.github.kitakkun.kondition.core.annotation.CoerceAtLeast
import io.github.kitakkun.kondition.core.annotation.CoerceAtLeastDecimal
import io.github.kitakkun.kondition.core.annotation.CoerceAtMost
import io.github.kitakkun.kondition.core.annotation.CoerceAtMostDecimal
import io.github.kitakkun.kondition.core.annotation.CoerceIn
import io.github.kitakkun.kondition.core.annotation.CoerceInDecimal
import kotlin.test.Test
import kotlin.test.assertEquals

class ByteFittingTest {
    @Test
    fun testCoerceIn() {
        fun coerceInUpper(@CoerceIn(0, 10) value: Byte) {
            assertEquals(10, value)
        }

        fun coerceInLower(@CoerceIn(0, 10) value: Byte) {
            assertEquals(0, value)
        }

        coerceInUpper(50)
        coerceInLower(-50)
    }

    @Test
    fun testCoerceAtMost() {
        fun coerceAtMost(@CoerceAtMost(0) value: Byte) {
            assertEquals(0, value)
        }

        coerceAtMost(50)
    }

    @Test
    fun testCoerceAtLeast() {
        fun coerceAtLeast(@CoerceAtLeast(0) value: Byte) {
            assertEquals(0, value)
        }

        coerceAtLeast(-50)
    }

    @Test
    fun testCoerceInDecimal() {
        fun coerceInUpper(@CoerceInDecimal(0.0, 10.0) value: Byte) {
            assertEquals(10, value)
        }

        fun coerceInLower(@CoerceInDecimal(0.0, 10.0) value: Byte) {
            assertEquals(0, value)
        }

        coerceInUpper(50)
        coerceInLower(-50)
    }

    @Test
    fun testCoerceAtMostDecimal() {
        fun coerceAtMost(@CoerceAtMostDecimal(0.0) value: Byte) {
            assertEquals(0, value)
        }

        coerceAtMost(50)
    }

    @Test
    fun testCoerceAtLeastDecimal() {
        fun coerceAtLeast(@CoerceAtLeastDecimal(0.0) value: Byte) {
            assertEquals(0, value)
        }

        coerceAtLeast(-50)
    }
}
