package io.github.kitakkun.kondition.test

import io.github.kitakkun.kondition.core.annotation.Negative
import io.github.kitakkun.kondition.core.annotation.NonNegative
import io.github.kitakkun.kondition.core.annotation.NonPositive
import io.github.kitakkun.kondition.core.annotation.NonZero
import io.github.kitakkun.kondition.core.annotation.Positive
import io.github.kitakkun.kondition.core.annotation.RangeRule
import io.github.kitakkun.kondition.core.annotation.RangedDouble
import kotlin.test.Test
import kotlin.test.assertFailsWith

class DoubleTest {
    @Test
    fun testRanged_InclusiveInclusive() {
        fun ranged(@RangedDouble(start = 0.0, end = 10.0) value: Double) {
            // compiler will generate:
            // require(value in 0.0..10.0)
        }

        // start
        ranged(0.0)
        assertFailsWith(IllegalArgumentException::class) {
            ranged(-1.0)
        }

        // end
        ranged(10.0)
        assertFailsWith(IllegalArgumentException::class) {
            ranged(11.0)
        }
    }

    @Test
    fun testRanged_InclusiveExclusive() {
        fun ranged(@RangedDouble(start = 0.0, end = 10.0, rule = RangeRule.InclusiveExclusive) value: Double) {
            // compiler will generate:
            // require(value in 0.0 until 10.0)
        }

        // start
        ranged(0.0)
        assertFailsWith(IllegalArgumentException::class) {
            ranged(-1.0)
        }

        // end
        ranged(9.0)
        assertFailsWith(IllegalArgumentException::class) {
            ranged(10.0)
        }
    }

    @Test
    fun testRanged_ExclusiveInclusive() {
        fun ranged(@RangedDouble(start = 0.0, end = 10.0, rule = RangeRule.ExclusiveInclusive) value: Double) {
            // compiler will generate:
            // require(value > 0.0 && value <= 10.0)
        }

        // start
        ranged(1.0)
        assertFailsWith(IllegalArgumentException::class) {
            ranged(0.0)
        }

        // end
        ranged(10.0)
        assertFailsWith(IllegalArgumentException::class) {
            ranged(11.0)
        }
    }

    @Test
    fun testRanged_ExclusiveExclusive() {
        fun ranged(@RangedDouble(start = 0.0, end = 10.0, rule = RangeRule.ExclusiveExclusive) value: Double) {
            // compiler will generate:
            // require(value > 0.0 && value < 10.0)
        }

        // start
        ranged(1.0)
        assertFailsWith(IllegalArgumentException::class) {
            ranged(0.0)
        }

        // end
        ranged(9.0)
        assertFailsWith(IllegalArgumentException::class) {
            ranged(10.0)
        }
    }

    @Test
    fun testNegative() {
        fun negative(@Negative value: Double) {
            // compiler will generate:
            // require(value < 0) { ... }
        }

        negative(-1.0)
        assertFailsWith(IllegalArgumentException::class) {
            negative(0.0)
        }
        assertFailsWith(IllegalArgumentException::class) {
            negative(1.0)
        }
    }

    @Test
    fun testNonNegative() {
        fun nonNegative(@NonNegative value: Double) {
            // compiler will generate:
            // require(value >= 0) { ... }
        }

        nonNegative(0.0)
        nonNegative(1.0)
        assertFailsWith(IllegalArgumentException::class) {
            nonNegative(-1.0)
        }
    }

    @Test
    fun testPositive() {
        fun positive(@Positive value: Double) {
            // compiler will generate:
            // require(value > 0) { ... }
        }

        positive(1.0)
        assertFailsWith(IllegalArgumentException::class) {
            positive(0.0)
        }
        assertFailsWith(IllegalArgumentException::class) {
            positive(-1.0)
        }
    }

    @Test
    fun testNonPositive() {
        fun nonPositive(@NonPositive value: Double) {
            // compiler will generate:
            // require(value <= 0) { ... }
        }

        nonPositive(0.0)
        nonPositive(-1.0)
        assertFailsWith(IllegalArgumentException::class) {
            nonPositive(1.0)
        }
    }

    @Test
    fun testNonZero() {
        fun nonZero(@NonZero value: Double) {
            // compiler will generate:
            // require(value != 0.0) { ... }
        }

        nonZero(1.0)
        nonZero(-1.0)
        assertFailsWith(IllegalArgumentException::class) {
            nonZero(0.0)
        }
    }
}
