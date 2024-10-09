package io.github.kitakkun.kondition.test

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
}
