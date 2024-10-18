package io.github.kitakkun.kondition.test.fitting

import io.github.kitakkun.kondition.core.annotation.ToLowerCase
import io.github.kitakkun.kondition.core.annotation.ToUpperCase
import kotlin.test.Test
import kotlin.test.assertTrue

class CharFittingTest {
    @Test
    fun upperCaseTest() {
        fun myFunction(@ToUpperCase value: Char) {
            assertTrue(value.isUpperCase())
        }

        myFunction('l')
    }

    @Test
    fun lowerCaseTest() {
        fun myFunction(@ToLowerCase value: Char) {
            assertTrue(value.isLowerCase())
        }

        myFunction('U')
    }
}
