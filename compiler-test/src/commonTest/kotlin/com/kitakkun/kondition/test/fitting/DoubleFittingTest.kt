package com.kitakkun.kondition.test.fitting

import com.kitakkun.kondition.core.annotation.CoerceAtLeast
import com.kitakkun.kondition.core.annotation.CoerceAtLeastDecimal
import com.kitakkun.kondition.core.annotation.CoerceAtMost
import com.kitakkun.kondition.core.annotation.CoerceAtMostDecimal
import com.kitakkun.kondition.core.annotation.CoerceIn
import com.kitakkun.kondition.core.annotation.CoerceInDecimal
import kotlin.test.Test
import kotlin.test.assertEquals

class DoubleFittingTest {
    @Test
    fun testCoerceIn() {
        fun coerceInUpper(@CoerceIn(0, 10) value: Double) {
            assertEquals(10.0, value)
        }

        fun coerceInLower(@CoerceIn(0, 10) value: Double) {
            assertEquals(0.0, value)
        }

        coerceInUpper(50.0)
        coerceInLower(-50.0)
    }

    @Test
    fun testCoerceAtMost() {
        fun coerceAtMost(@CoerceAtMost(0) value: Double) {
            assertEquals(0.0, value)
        }

        coerceAtMost(50.0)
    }

    @Test
    fun testCoerceAtLeast() {
        fun coerceAtLeast(@CoerceAtLeast(0) value: Double) {
            assertEquals(0.0, value)
        }

        coerceAtLeast(-50.0)
    }

    @Test
    fun testCoerceInDecimal() {
        fun coerceInUpper(@CoerceInDecimal(0.0, 10.0) value: Double) {
            assertEquals(10.0, value)
        }

        fun coerceInLower(@CoerceInDecimal(0.0, 10.0) value: Double) {
            assertEquals(0.0, value)
        }

        coerceInUpper(10.1)
        coerceInLower(-0.1)
    }

    @Test
    fun testCoerceAtMostDecimal() {
        fun coerceAtMost(@CoerceAtMostDecimal(0.0) value: Double) {
            assertEquals(0.0, value)
        }

        coerceAtMost(0.1)
    }

    @Test
    fun testCoerceAtLeastDecimal() {
        fun coerceAtLeast(@CoerceAtLeastDecimal(0.0) value: Double) {
            assertEquals(0.0, value)
        }

        coerceAtLeast(-0.1)
    }
}
