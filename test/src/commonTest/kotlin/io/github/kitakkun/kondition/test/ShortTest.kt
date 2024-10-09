package io.github.kitakkun.kondition.test

import io.github.kitakkun.kondition.core.annotation.RangeRule
import io.github.kitakkun.kondition.core.annotation.RangedShort
import kotlin.test.Test
import kotlin.test.assertFailsWith

class ShortTest {
    @Test
    fun testRanged_InclusiveInclusive() {
        fun ranged(@RangedShort(start = 0, end = 10) value: Short) {
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
        fun ranged(@RangedShort(start = 0, end = 10, rule = RangeRule.InclusiveExclusive) value: Short) {
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
        fun ranged(@RangedShort(start = 0, end = 10, rule = RangeRule.ExclusiveInclusive) value: Short) {
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
        fun ranged(@RangedShort(start = 0, end = 10, rule = RangeRule.ExclusiveExclusive) value: Short) {
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
}
