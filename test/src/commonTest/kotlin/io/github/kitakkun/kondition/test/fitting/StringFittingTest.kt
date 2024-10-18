package io.github.kitakkun.kondition.test.fitting

import io.github.kitakkun.kondition.core.annotation.AddPrefix
import io.github.kitakkun.kondition.core.annotation.AddSuffix
import io.github.kitakkun.kondition.core.annotation.Drop
import io.github.kitakkun.kondition.core.annotation.DropLast
import io.github.kitakkun.kondition.core.annotation.LowerCase
import io.github.kitakkun.kondition.core.annotation.RemovePrefix
import io.github.kitakkun.kondition.core.annotation.RemoveSuffix
import io.github.kitakkun.kondition.core.annotation.Take
import io.github.kitakkun.kondition.core.annotation.TakeLast
import io.github.kitakkun.kondition.core.annotation.Trim
import io.github.kitakkun.kondition.core.annotation.TrimEnd
import io.github.kitakkun.kondition.core.annotation.TrimStart
import io.github.kitakkun.kondition.core.annotation.UpperCase
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class StringFittingTest {
    @Test
    fun addPrefixTest() {
        fun myFunction(@AddPrefix("prefix_") value: String) {
            assertTrue { value.startsWith("prefix_") }
        }

        myFunction("test")
    }

    @Test
    fun addSuffixTest() {
        fun myFunction(@AddSuffix("_suffix") value: String) {
            assertTrue { value.endsWith("_suffix") }
        }

        myFunction("test")
    }

    @Test
    fun removePrefixTest() {
        fun myFunction(@RemovePrefix("prefix_") value: String) {
            assertFalse { value.startsWith("prefix_") }
        }

        myFunction("prefix_test")
    }

    @Test
    fun removeSuffixTest() {
        fun myFunction(@RemoveSuffix("_suffix") value: String) {
            assertFalse { value.endsWith("_suffix") }
        }

        myFunction("test_suffix")
    }

    @Test
    fun trimTest() {
        fun myFunction(@Trim value: String) {
            assertTrue { value.takeWhile { it.isWhitespace() }.isEmpty() }
            assertTrue { value.takeLastWhile { it.isWhitespace() }.isEmpty() }
        }

        myFunction("     test     ")
    }

    @Test
    fun trimStartTest() {
        fun myFunction(@TrimStart value: String) {
            assertTrue { value.takeWhile { it.isWhitespace() }.isEmpty() }
            assertTrue { value.takeLastWhile { it.isWhitespace() }.isNotEmpty() }
        }

        myFunction("     test     ")
    }

    @Test
    fun trimEndTest() {
        fun myFunction(@TrimEnd value: String) {
            assertTrue { value.takeWhile { it.isWhitespace() }.isNotEmpty() }
            assertTrue { value.takeLastWhile { it.isWhitespace() }.isEmpty() }
        }

        myFunction("     test     ")
    }

    @Test
    fun takeTest() {
        fun myFunction(@Take(5) value: String) {
            assertEquals("12345", value)
        }

        myFunction("1234567890")
    }

    @Test
    fun takeLastTest() {
        fun myFunction(@TakeLast(5) value: String) {
            assertEquals("67890", value)
        }

        myFunction("1234567890")
    }

    @Test
    fun dropTest() {
        fun myFunction(@Drop(5) value: String) {
            assertEquals("67890", value)
        }

        myFunction("1234567890")
    }

    @Test
    fun dropLastTest() {
        fun myFunction(@DropLast(5) value: String) {
            assertEquals("12345", value)
        }

        myFunction("1234567890")
    }

    @Test
    fun upperCaseTest() {
        fun myFunction(@UpperCase value: String) {
            assertTrue { value.all { it.isUpperCase() } }
        }

        myFunction("lowercase")
    }

    @Test
    fun lowerCaseTest() {
        fun myFunction(@LowerCase value: String) {
            assertTrue { value.all { it.isLowerCase() } }
        }

        myFunction("UPPERCASE")
    }
}
