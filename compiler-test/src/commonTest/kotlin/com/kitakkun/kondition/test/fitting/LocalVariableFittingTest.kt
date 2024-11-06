package com.kitakkun.kondition.test.fitting

import com.kitakkun.kondition.core.annotation.ToUpperCase
import kotlin.test.Test
import kotlin.test.assertTrue

class LocalVariableFittingTest {
    @Test
    fun testImmutableVariable() {
        fun myFunction() {
            @ToUpperCase
            val value = "lowercase"
            assertTrue(value.all { it.isUpperCase() })
        }

        myFunction()
    }

    @Test
    fun testImmutableLateInitVariable() {
        fun myFunction() {
            @ToUpperCase
            val value: String
            value = "lowercase"
            assertTrue(value.all { it.isUpperCase() })
        }

        myFunction()
    }

    @Test
    fun testMutableVariable() {
        fun myFunction() {
            @ToUpperCase
            var value = "lowercase"
            assertTrue(value.all { it.isUpperCase() })
            value = "secondPatternOfLowercase"
            assertTrue(value.all { it.isUpperCase() })
        }

        myFunction()
    }

    @Test
    fun testMutableLateInitVariable() {
        fun myFunction() {
            @ToUpperCase
            var value: String
            value = "lowercase"
            assertTrue(value.all { it.isUpperCase() })
            value = "secondPatternOfLowercase"
            assertTrue(value.all { it.isUpperCase() })
        }

        myFunction()
    }
}
