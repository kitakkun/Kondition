package com.kitakkun.kondition.test.fitting

import com.kitakkun.kondition.core.annotation.CoerceAtLeast
import com.kitakkun.kondition.core.annotation.CoerceAtLeastDecimal
import com.kitakkun.kondition.core.annotation.CoerceAtMost
import com.kitakkun.kondition.core.annotation.CoerceAtMostDecimal
import com.kitakkun.kondition.core.annotation.CoerceIn
import com.kitakkun.kondition.core.annotation.CoerceInDecimal
import kotlin.test.Test
import kotlin.test.assertEquals

class IntFittingTest {
    @Test
    fun testCoerceIn() {
        fun coerceInUpper(@CoerceIn(0, 10) value: Int) {
            assertEquals(10, value)
        }

        fun coerceInLower(@CoerceIn(0, 10) value: Int) {
            assertEquals(0, value)
        }

        coerceInUpper(50)
        coerceInLower(-50)
    }

    @Test
    fun testCoerceAtMost() {
        fun coerceAtMost(@CoerceAtMost(0) value: Int) {
            assertEquals(0, value)
        }

        coerceAtMost(50)
    }

    @Test
    fun testCoerceAtLeast() {
        fun coerceAtLeast(@CoerceAtLeast(0) value: Int) {
            assertEquals(0, value)
        }

        coerceAtLeast(-50)
    }

    @Test
    fun testCoerceInDecimal() {
        fun coerceInUpper(@CoerceInDecimal(0.0, 10.0) value: Int) {
            assertEquals(10, value)
        }

        fun coerceInLower(@CoerceInDecimal(0.0, 10.0) value: Int) {
            assertEquals(0, value)
        }

        coerceInUpper(50)
        coerceInLower(-50)
    }

    @Test
    fun testCoerceAtMostDecimal() {
        fun coerceAtMost(@CoerceAtMostDecimal(0.0) value: Int) {
            assertEquals(0, value)
        }

        coerceAtMost(50)
    }

    @Test
    fun testCoerceAtLeastDecimal() {
        fun coerceAtLeast(@CoerceAtLeastDecimal(0.0) value: Int) {
            assertEquals(0, value)
        }

        coerceAtLeast(-50)
    }
}
