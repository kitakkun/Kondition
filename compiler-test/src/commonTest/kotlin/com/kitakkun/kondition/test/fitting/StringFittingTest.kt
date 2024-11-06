package com.kitakkun.kondition.test.fitting

import com.kitakkun.kondition.core.annotation.AddPrefix
import com.kitakkun.kondition.core.annotation.AddSuffix
import com.kitakkun.kondition.core.annotation.Drop
import com.kitakkun.kondition.core.annotation.DropLast
import com.kitakkun.kondition.core.annotation.RemovePrefix
import com.kitakkun.kondition.core.annotation.RemoveSuffix
import com.kitakkun.kondition.core.annotation.Take
import com.kitakkun.kondition.core.annotation.TakeLast
import com.kitakkun.kondition.core.annotation.ToLowerCase
import com.kitakkun.kondition.core.annotation.ToUpperCase
import com.kitakkun.kondition.core.annotation.Trim
import com.kitakkun.kondition.core.annotation.TrimEnd
import com.kitakkun.kondition.core.annotation.TrimStart
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
        fun myFunction(@ToUpperCase value: String) {
            assertTrue { value.all { it.isUpperCase() } }
        }

        myFunction("lowercase")
    }

    @Test
    fun lowerCaseTest() {
        fun myFunction(@ToLowerCase value: String) {
            assertTrue { value.all { it.isLowerCase() } }
        }

        myFunction("UPPERCASE")
    }
}
