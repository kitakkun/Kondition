package io.github.kitakkun.kondition.test

import io.github.kitakkun.kondition.core.annotation.RangeRule
import io.github.kitakkun.kondition.core.annotation.RangedInt
import kotlin.test.Test
import kotlin.test.assertFailsWith

class IntTest {
    @Test
    fun testRanged_InclusiveInclusive() {
        fun rangedInt(@RangedInt(start = 0, end = 10) value: Int) {
            // compiler will generate:
            // require(value in 0..10)
        }

        // start
        rangedInt(0)
        assertFailsWith(IllegalArgumentException::class) {
            rangedInt(-1)
        }

        // end
        rangedInt(10)
        assertFailsWith(IllegalArgumentException::class) {
            rangedInt(11)
        }
    }

    @Test
    fun testRanged_InclusiveExclusive() {
        fun rangedInt(@RangedInt(start = 0, end = 10, rule = RangeRule.InclusiveExclusive) value: Int) {
            // compiler will generate:
            // require(value in 0 until 10)
        }

        // start
        rangedInt(0)
        assertFailsWith(IllegalArgumentException::class) {
            rangedInt(-1)
        }

        // end
        rangedInt(9)
        assertFailsWith(IllegalArgumentException::class) {
            rangedInt(10)
        }
    }

    @Test
    fun testRanged_ExclusiveInclusive() {
        fun rangedInt(@RangedInt(start = 0, end = 10, rule = RangeRule.ExclusiveInclusive) value: Int) {
            // compiler will generate:
            // require(value > 0 && value <= 10)
        }

        // start
        rangedInt(1)
        assertFailsWith(IllegalArgumentException::class) {
            rangedInt(0)
        }

        // end
        rangedInt(10)
        assertFailsWith(IllegalArgumentException::class) {
            rangedInt(11)
        }
    }

    @Test
    fun testRanged_ExclusiveExclusive() {
        fun rangedInt(@RangedInt(start = 0, end = 10, rule = RangeRule.ExclusiveExclusive) value: Int) {
            // compiler will generate:
            // require(value > 0 && value < 10)
        }

        // start
        rangedInt(1)
        assertFailsWith(IllegalArgumentException::class) {
            rangedInt(0)
        }

        // end
        rangedInt(9)
        assertFailsWith(IllegalArgumentException::class) {
            rangedInt(10)
        }
    }
}
