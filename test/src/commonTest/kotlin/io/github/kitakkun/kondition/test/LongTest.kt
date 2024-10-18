package io.github.kitakkun.kondition.test

import io.github.kitakkun.kondition.core.annotation.CoerceAtLeast
import io.github.kitakkun.kondition.core.annotation.CoerceAtLeastDecimal
import io.github.kitakkun.kondition.core.annotation.CoerceAtMost
import io.github.kitakkun.kondition.core.annotation.CoerceAtMostDecimal
import io.github.kitakkun.kondition.core.annotation.CoerceIn
import io.github.kitakkun.kondition.core.annotation.CoerceInDecimal
import io.github.kitakkun.kondition.core.annotation.GreaterThan
import io.github.kitakkun.kondition.core.annotation.GreaterThanOrEquals
import io.github.kitakkun.kondition.core.annotation.LessThan
import io.github.kitakkun.kondition.core.annotation.LessThanOrEquals
import io.github.kitakkun.kondition.core.annotation.Negative
import io.github.kitakkun.kondition.core.annotation.NonNegative
import io.github.kitakkun.kondition.core.annotation.NonPositive
import io.github.kitakkun.kondition.core.annotation.NonZero
import io.github.kitakkun.kondition.core.annotation.Positive
import io.github.kitakkun.kondition.core.annotation.RangeRule
import io.github.kitakkun.kondition.core.annotation.Ranged
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class LongTest {
    @Test
    fun testRanged_InclusiveInclusive() {
        fun ranged(@Ranged(start = 0, end = 10) value: Long) {
            // compiler will generate:
            // require(value in 0..10)
        }

        // start
        ranged(0)
        assertFailsWith(IllegalArgumentException::class) {
            ranged(-1)
        }

        // end
        ranged(10)
        assertFailsWith(IllegalArgumentException::class) {
            ranged(11)
        }
    }

    @Test
    fun testRanged_InclusiveExclusive() {
        fun ranged(@Ranged(start = 0, end = 10, rule = RangeRule.InclusiveExclusive) value: Long) {
            // compiler will generate:
            // require(value in 0 until 10)
        }

        // start
        ranged(0)
        assertFailsWith(IllegalArgumentException::class) {
            ranged(-1)
        }

        // end
        ranged(9)
        assertFailsWith(IllegalArgumentException::class) {
            ranged(10)
        }
    }

    @Test
    fun testRanged_ExclusiveInclusive() {
        fun ranged(@Ranged(start = 0, end = 10, rule = RangeRule.ExclusiveInclusive) value: Long) {
            // compiler will generate:
            // require(value > 0 && value <= 10)
        }

        // start
        ranged(1)
        assertFailsWith(IllegalArgumentException::class) {
            ranged(0)
        }

        // end
        ranged(10)
        assertFailsWith(IllegalArgumentException::class) {
            ranged(11)
        }
    }

    @Test
    fun testRanged_ExclusiveExclusive() {
        fun ranged(@Ranged(start = 0, end = 10, rule = RangeRule.ExclusiveExclusive) value: Long) {
            // compiler will generate:
            // require(value > 0 && value < 10)
        }

        // start
        ranged(1)
        assertFailsWith(IllegalArgumentException::class) {
            ranged(0)
        }

        // end
        ranged(9)
        assertFailsWith(IllegalArgumentException::class) {
            ranged(10)
        }
    }

    @Test
    fun testNegative() {
        fun negative(@Negative value: Long) {
            // compiler will generate:
            // require(value < 0) { ... }
        }

        negative(-1)
        assertFailsWith(IllegalArgumentException::class) {
            negative(0)
        }
        assertFailsWith(IllegalArgumentException::class) {
            negative(1)
        }
    }

    @Test
    fun testNonNegative() {
        fun nonNegative(@NonNegative value: Long) {
            // compiler will generate:
            // require(value >= 0) { ... }
        }

        nonNegative(0)
        nonNegative(1)
        assertFailsWith(IllegalArgumentException::class) {
            nonNegative(-1)
        }
    }

    @Test
    fun testPositive() {
        fun positive(@Positive value: Long) {
            // compiler will generate:
            // require(value > 0) { ... }
        }

        positive(1)
        assertFailsWith(IllegalArgumentException::class) {
            positive(0)
        }
        assertFailsWith(IllegalArgumentException::class) {
            positive(-1)
        }
    }

    @Test
    fun testNonPositive() {
        fun nonPositive(@NonPositive value: Long) {
            // compiler will generate:
            // require(value <= 0) { ... }
        }

        nonPositive(0)
        nonPositive(-1)
        assertFailsWith(IllegalArgumentException::class) {
            nonPositive(1)
        }
    }

    @Test
    fun testNonZero() {
        fun nonZero(@NonZero value: Long) {
            // compiler will generate:
            // require(value != 0) { ... }
        }

        nonZero(1)
        nonZero(-1)
        assertFailsWith(IllegalArgumentException::class) {
            nonZero(0)
        }
    }

    @Test
    fun testGreaterThan() {
        fun greaterThan(@GreaterThan(0) value: Long) {
            // compiler will generate:
            // require(value > 0) { ... }
        }

        greaterThan(10)
        assertFailsWith(IllegalArgumentException::class) {
            greaterThan(0)
        }
        assertFailsWith(IllegalArgumentException::class) {
            greaterThan(-1)
        }
    }

    @Test
    fun testGreaterThanOrEquals() {
        fun greaterThanOrEquals(@GreaterThanOrEquals(0) value: Long) {
            // compiler will generate:
            // require(value >= 0) { ... }
        }

        greaterThanOrEquals(10)
        greaterThanOrEquals(0)
        assertFailsWith(IllegalArgumentException::class) {
            greaterThanOrEquals(-1)
        }
    }

    @Test
    fun testLessThan() {
        fun lessThan(@LessThan(0) value: Long) {
            // compiler will generate:
            // require(value < 0) { ... }
        }

        lessThan(-10)
        assertFailsWith(IllegalArgumentException::class) {
            lessThan(0)
        }
        assertFailsWith(IllegalArgumentException::class) {
            lessThan(1)
        }
    }

    @Test
    fun testLessThanOrEquals() {
        fun lessThanOrEquals(@LessThanOrEquals(0) value: Long) {
            // compiler will generate:
            // require(value < 0) { ... }
        }

        lessThanOrEquals(-10)
        lessThanOrEquals(0)
        assertFailsWith(IllegalArgumentException::class) {
            lessThanOrEquals(1)
        }
    }

    @Test
    fun testCoerceIn() {
        fun coerceInUpper(@CoerceIn(0, 10) value: Long) {
            assertEquals(10, value)
        }

        fun coerceInLower(@CoerceIn(0, 10) value: Long) {
            assertEquals(0, value)
        }

        coerceInUpper(50)
        coerceInLower(-50)
    }

    @Test
    fun testCoerceAtMost() {
        fun coerceAtMost(@CoerceAtMost(0) value: Long) {
            assertEquals(0, value)
        }

        coerceAtMost(50)
    }

    @Test
    fun testCoerceAtLeast() {
        fun coerceAtLeast(@CoerceAtLeast(0) value: Long) {
            assertEquals(0, value)
        }

        coerceAtLeast(-50)
    }

    @Test
    fun testCoerceInDecimal() {
        fun coerceInUpper(@CoerceInDecimal(0.0, 10.0) value: Long) {
            assertEquals(10, value)
        }

        fun coerceInLower(@CoerceInDecimal(0.0, 10.0) value: Long) {
            assertEquals(0, value)
        }

        coerceInUpper(50)
        coerceInLower(-50)
    }

    @Test
    fun testCoerceAtMostDecimal() {
        fun coerceAtMost(@CoerceAtMostDecimal(0.0) value: Long) {
            assertEquals(0, value)
        }

        coerceAtMost(50)
    }

    @Test
    fun testCoerceAtLeastDecimal() {
        fun coerceAtLeast(@CoerceAtLeastDecimal(0.0) value: Long) {
            assertEquals(0, value)
        }

        coerceAtLeast(-50)
    }
}
