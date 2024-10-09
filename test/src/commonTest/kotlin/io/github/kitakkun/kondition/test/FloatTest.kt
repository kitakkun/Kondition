package io.github.kitakkun.kondition.test

import io.github.kitakkun.kondition.core.annotation.RangeRule
import io.github.kitakkun.kondition.core.annotation.RangedFloat
import kotlin.test.Test
import kotlin.test.assertFailsWith

class FloatTest {
    @Test
    fun testRanged_InclusiveInclusive() {
        fun ranged(@RangedFloat(start = 0.0f, end = 10.0f) value: Float) {
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
        fun ranged(@RangedFloat(start = 0.0f, end = 10.0f, rule = RangeRule.InclusiveExclusive) value: Float) {
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
        fun ranged(@RangedFloat(start = 0.0f, end = 10.0f, rule = RangeRule.ExclusiveInclusive) value: Float) {
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
        fun ranged(@RangedFloat(start = 0.0f, end = 10.0f, rule = RangeRule.ExclusiveExclusive) value: Float) {
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
}
