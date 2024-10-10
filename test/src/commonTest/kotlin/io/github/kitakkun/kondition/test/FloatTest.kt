package io.github.kitakkun.kondition.test

import io.github.kitakkun.kondition.core.annotation.GreaterThanDecimal
import io.github.kitakkun.kondition.core.annotation.Negative
import io.github.kitakkun.kondition.core.annotation.NonNegative
import io.github.kitakkun.kondition.core.annotation.NonPositive
import io.github.kitakkun.kondition.core.annotation.NonZero
import io.github.kitakkun.kondition.core.annotation.Positive
import io.github.kitakkun.kondition.core.annotation.RangeRule
import io.github.kitakkun.kondition.core.annotation.RangedDecimal
import kotlin.test.Test
import kotlin.test.assertFailsWith

class FloatTest {
    @Test
    fun testRanged_InclusiveInclusive() {
        fun ranged(@RangedDecimal(start = 0.0, end = 10.0) value: Float) {
            // compiler will generate:
            // require(value in 0.0f..10.0f)
        }

        // start
        ranged(0.0f)
        assertFailsWith(IllegalArgumentException::class) {
            ranged(-1.0f)
        }

        // end
        ranged(10.0f)
        assertFailsWith(IllegalArgumentException::class) {
            ranged(11.0f)
        }
    }

    @Test
    fun testRanged_InclusiveExclusive() {
        fun ranged(@RangedDecimal(start = 0.0, end = 10.0, rule = RangeRule.InclusiveExclusive) value: Float) {
            // compiler will generate:
            // require(value in 0.0f until 10.0f)
        }

        // start
        ranged(0.0f)
        assertFailsWith(IllegalArgumentException::class) {
            ranged(-1.0f)
        }

        // end
        ranged(9.0f)
        assertFailsWith(IllegalArgumentException::class) {
            ranged(10.0f)
        }
    }

    @Test
    fun testRanged_ExclusiveInclusive() {
        fun ranged(@RangedDecimal(start = 0.0, end = 10.0, rule = RangeRule.ExclusiveInclusive) value: Float) {
            // compiler will generate:
            // require(value > 0.0f && value <= 10.0f)
        }

        // start
        ranged(1.0f)
        assertFailsWith(IllegalArgumentException::class) {
            ranged(0.0f)
        }

        // end
        ranged(10.0f)
        assertFailsWith(IllegalArgumentException::class) {
            ranged(11.0f)
        }
    }

    @Test
    fun testRanged_ExclusiveExclusive() {
        fun ranged(@RangedDecimal(start = 0.0, end = 10.0, rule = RangeRule.ExclusiveExclusive) value: Float) {
            // compiler will generate:
            // require(value > 0.0f && value < 10.0f)
        }

        // start
        ranged(1.0f)
        assertFailsWith(IllegalArgumentException::class) {
            ranged(0.0f)
        }

        // end
        ranged(9.0f)
        assertFailsWith(IllegalArgumentException::class) {
            ranged(10.0f)
        }
    }

    @Test
    fun testNegative() {
        fun negative(@Negative value: Float) {
            // compiler will generate:
            // require(value < 0) { ... }
        }

        negative(-1.0f)
        assertFailsWith(IllegalArgumentException::class) {
            negative(0.0f)
        }
        assertFailsWith(IllegalArgumentException::class) {
            negative(1.0f)
        }
    }

    @Test
    fun testNonNegative() {
        fun nonNegative(@NonNegative value: Float) {
            // compiler will generate:
            // require(value >= 0) { ... }
        }

        nonNegative(0.0f)
        nonNegative(1.0f)
        assertFailsWith(IllegalArgumentException::class) {
            nonNegative(-1.0f)
        }
    }

    @Test
    fun testPositive() {
        fun positive(@Positive value: Float) {
            // compiler will generate:
            // require(value > 0) { ... }
        }

        positive(1.0f)
        assertFailsWith(IllegalArgumentException::class) {
            positive(0.0f)
        }
        assertFailsWith(IllegalArgumentException::class) {
            positive(-1.0f)
        }
    }

    @Test
    fun testNonPositive() {
        fun nonPositive(@NonPositive value: Float) {
            // compiler will generate:
            // require(value <= 0) { ... }
        }

        nonPositive(0.0f)
        nonPositive(-1.0f)
        assertFailsWith(IllegalArgumentException::class) {
            nonPositive(1.0f)
        }
    }

    @Test
    fun testNonZero() {
        fun nonZero(@NonZero value: Float) {
            // compiler will generate:
            // require(value != 0.0f) { ... }
        }

        nonZero(1.0f)
        nonZero(-1.0f)
        assertFailsWith(IllegalArgumentException::class) {
            nonZero(0.0f)
        }
    }

    @Test
    fun testGreaterThan() {
        fun greaterThan(@GreaterThanDecimal(0.0) value: Float) {
            // compiler will generate:
            // require(value > 0.0f) { ... }
        }

        greaterThan(10.0f)
        assertFailsWith(IllegalArgumentException::class) {
            greaterThan(0.0f)
        }
        assertFailsWith(IllegalArgumentException::class) {
            greaterThan(-1.0f)
        }
    }
}
