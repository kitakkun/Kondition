package io.github.kitakkun.kondition.test

import io.github.kitakkun.kondition.core.annotation.Negative
import io.github.kitakkun.kondition.core.annotation.NonNegative
import io.github.kitakkun.kondition.core.annotation.NonPositive
import io.github.kitakkun.kondition.core.annotation.NonZero
import io.github.kitakkun.kondition.core.annotation.Positive
import io.github.kitakkun.kondition.core.annotation.RangeRule
import io.github.kitakkun.kondition.core.annotation.RangedLong
import kotlin.test.Test
import kotlin.test.assertFailsWith

class LongTest {
    @Test
    fun testRanged_InclusiveInclusive() {
        fun ranged(@RangedLong(start = 0, end = 10) value: Long) {
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
        fun ranged(@RangedLong(start = 0, end = 10, rule = RangeRule.InclusiveExclusive) value: Long) {
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
        fun ranged(@RangedLong(start = 0, end = 10, rule = RangeRule.ExclusiveInclusive) value: Long) {
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
        fun ranged(@RangedLong(start = 0, end = 10, rule = RangeRule.ExclusiveExclusive) value: Long) {
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
}
