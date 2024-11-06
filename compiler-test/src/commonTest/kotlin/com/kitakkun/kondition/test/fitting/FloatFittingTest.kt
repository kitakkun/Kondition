package com.kitakkun.kondition.test.fitting

import com.kitakkun.kondition.core.annotation.CoerceAtLeast
import com.kitakkun.kondition.core.annotation.CoerceAtLeastDecimal
import com.kitakkun.kondition.core.annotation.CoerceAtMost
import com.kitakkun.kondition.core.annotation.CoerceAtMostDecimal
import com.kitakkun.kondition.core.annotation.CoerceIn
import com.kitakkun.kondition.core.annotation.CoerceInDecimal
import kotlin.test.Test
import kotlin.test.assertEquals

class FloatFittingTest {
    @Test
    fun testCoerceIn() {
        fun coerceInUpper(@CoerceIn(0, 10) value: Float) {
            assertEquals(10.0f, value)
        }

        fun coerceInLower(@CoerceIn(0, 10) value: Float) {
            assertEquals(0.0f, value)
        }

        coerceInUpper(50.0f)
        coerceInLower(-50.0f)
    }

    @Test
    fun testCoerceAtMost() {
        fun coerceAtMost(@CoerceAtMost(0) value: Float) {
            assertEquals(0.0f, value)
        }

        coerceAtMost(50.0f)
    }

    @Test
    fun testCoerceAtLeast() {
        fun coerceAtLeast(@CoerceAtLeast(0) value: Float) {
            assertEquals(0.0f, value)
        }

        coerceAtLeast(-50.0f)
    }

    @Test
    fun testCoerceInDecimal() {
        fun coerceInUpper(@CoerceInDecimal(0.0, 10.0) value: Float) {
            assertEquals(10.0f, value)
        }

        fun coerceInLower(@CoerceInDecimal(0.0, 10.0) value: Float) {
            assertEquals(0.0f, value)
        }

        coerceInUpper(10.1f)
        coerceInLower(-0.1f)
    }

    @Test
    fun testCoerceAtMostDecimal() {
        fun coerceAtMost(@CoerceAtMostDecimal(0.0) value: Float) {
            assertEquals(0.0f, value)
        }

        coerceAtMost(0.1f)
    }

    @Test
    fun testCoerceAtLeastDecimal() {
        fun coerceAtLeast(@CoerceAtLeastDecimal(0.0) value: Float) {
            assertEquals(0.0f, value)
        }

        coerceAtLeast(-0.1f)
    }
}
