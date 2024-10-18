package io.github.kitakkun.kondition.test.fitting

import io.github.kitakkun.kondition.core.annotation.LowerCase
import io.github.kitakkun.kondition.core.annotation.UpperCase
import kotlin.test.Test
import kotlin.test.assertTrue

class CharFittingTest {
    @Test
    fun upperCaseTest() {
        fun myFunction(@UpperCase value: Char) {
            assertTrue(value.isUpperCase())
        }

        myFunction('l')
    }

    @Test
    fun lowerCaseTest() {
        fun myFunction(@LowerCase value: Char) {
            assertTrue(value.isLowerCase())
        }

        myFunction('U')
    }
}
