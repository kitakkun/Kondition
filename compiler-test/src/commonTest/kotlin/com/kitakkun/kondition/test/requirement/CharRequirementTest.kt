package com.kitakkun.kondition.test.requirement

import com.kitakkun.kondition.core.annotation.LowerCased
import com.kitakkun.kondition.core.annotation.UpperCased
import kotlin.test.Test
import kotlin.test.assertFailsWith

class CharRequirementTest {
    @Test
    fun testLowerCased() {
        fun myFunction(@LowerCased value: Char) {}

        myFunction('a')
        assertFailsWith<IllegalArgumentException> {
            myFunction('A')
        }
    }

    @Test
    fun testUpperCased() {
        fun myFunction(@UpperCased value: Char) {}

        myFunction('A')
        assertFailsWith<IllegalArgumentException> {
            myFunction('a')
        }
    }
}
