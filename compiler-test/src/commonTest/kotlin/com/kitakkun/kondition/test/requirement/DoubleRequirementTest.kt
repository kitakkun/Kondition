package com.kitakkun.kondition.test.requirement

import com.kitakkun.kondition.core.annotation.GreaterThanDecimal
import com.kitakkun.kondition.core.annotation.GreaterThanOrEqualsDecimal
import com.kitakkun.kondition.core.annotation.LessThanDecimal
import com.kitakkun.kondition.core.annotation.LessThanOrEqualsDecimal
import com.kitakkun.kondition.core.annotation.Negative
import com.kitakkun.kondition.core.annotation.NonNegative
import com.kitakkun.kondition.core.annotation.NonPositive
import com.kitakkun.kondition.core.annotation.NonZero
import com.kitakkun.kondition.core.annotation.Positive
import com.kitakkun.kondition.core.annotation.RangeRule
import com.kitakkun.kondition.core.annotation.RangedDecimal
import kotlin.test.Test
import kotlin.test.assertFailsWith

class DoubleRequirementTest {
    @Test
    fun testRanged_InclusiveInclusive() {
        fun ranged(@RangedDecimal(start = 0.0, end = 10.0) value: Double) {
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
        fun ranged(@RangedDecimal(start = 0.0, end = 10.0, rule = RangeRule.InclusiveExclusive) value: Double) {
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
        fun ranged(@RangedDecimal(start = 0.0, end = 10.0, rule = RangeRule.ExclusiveInclusive) value: Double) {
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
        fun ranged(@RangedDecimal(start = 0.0, end = 10.0, rule = RangeRule.ExclusiveExclusive) value: Double) {
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

    @Test
    fun testGreaterThan() {
        fun greaterThan(@GreaterThanDecimal(0.0) value: Double) {
            // compiler will generate:
            // require(value > 0.0) { ... }
        }

        greaterThan(10.0)
        assertFailsWith(IllegalArgumentException::class) {
            greaterThan(0.0)
        }
        assertFailsWith(IllegalArgumentException::class) {
            greaterThan(-1.0)
        }
    }

    @Test
    fun testGreaterThanOrEquals() {
        fun greaterThanOrEquals(@GreaterThanOrEqualsDecimal(0.0) value: Double) {
            // compiler will generate:
            // require(value >= 0.0) { ... }
        }

        greaterThanOrEquals(10.0)
        greaterThanOrEquals(0.0)
        assertFailsWith(IllegalArgumentException::class) {
            greaterThanOrEquals(-1.0)
        }
    }

    @Test
    fun testLessThan() {
        fun lessThan(@LessThanDecimal(0.0) value: Double) {
            // compiler will generate:
            // require(value < 0.0) { ... }
        }

        lessThan(-10.0)
        assertFailsWith(IllegalArgumentException::class) {
            lessThan(0.0)
        }
        assertFailsWith(IllegalArgumentException::class) {
            lessThan(1.0)
        }
    }

    @Test
    fun testLessThanOrEquals() {
        fun lessThanOrEquals(@LessThanOrEqualsDecimal(0.0) value: Double) {
            // compiler will generate:
            // require(value < 0.0) { ... }
        }

        lessThanOrEquals(-10.0)
        lessThanOrEquals(0.0)
        assertFailsWith(IllegalArgumentException::class) {
            lessThanOrEquals(1.0)
        }
    }
}
