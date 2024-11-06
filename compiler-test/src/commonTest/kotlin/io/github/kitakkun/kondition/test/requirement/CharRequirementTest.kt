package io.github.kitakkun.kondition.test.requirement

import io.github.kitakkun.kondition.core.annotation.LowerCased
import io.github.kitakkun.kondition.core.annotation.UpperCased
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
