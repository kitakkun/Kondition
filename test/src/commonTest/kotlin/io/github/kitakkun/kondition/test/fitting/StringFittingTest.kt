package io.github.kitakkun.kondition.test.fitting

import io.github.kitakkun.kondition.core.annotation.AddPrefix
import io.github.kitakkun.kondition.core.annotation.AddSuffix
import io.github.kitakkun.kondition.core.annotation.RemovePrefix
import io.github.kitakkun.kondition.core.annotation.RemoveSuffix
import kotlin.test.Test
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
}
