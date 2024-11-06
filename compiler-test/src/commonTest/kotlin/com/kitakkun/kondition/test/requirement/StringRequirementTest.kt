package com.kitakkun.kondition.test.requirement

import com.kitakkun.kondition.core.annotation.Alphabetic
import com.kitakkun.kondition.core.annotation.Length
import com.kitakkun.kondition.core.annotation.LowerCased
import com.kitakkun.kondition.core.annotation.MatchRegex
import com.kitakkun.kondition.core.annotation.MaxLength
import com.kitakkun.kondition.core.annotation.MinLength
import com.kitakkun.kondition.core.annotation.NonBlank
import com.kitakkun.kondition.core.annotation.NonEmpty
import com.kitakkun.kondition.core.annotation.Numeric
import com.kitakkun.kondition.core.annotation.Prefixed
import com.kitakkun.kondition.core.annotation.Suffixed
import com.kitakkun.kondition.core.annotation.UpperCased
import kotlin.test.Test
import kotlin.test.assertFailsWith

class StringRequirementTest {
    @Test
    fun testNonEmpty() {
        fun nonEmptyString(@NonEmpty value: String) {
            // compiler will generate:
            // require(value.isNotEmpty())
        }

        assertFailsWith(IllegalArgumentException::class) {
            nonEmptyString("")
        }
    }

    @Test
    fun testNonBlank() {
        fun nonBlankString(@NonBlank value: String) {
            // compiler will generate:
            // require(value.isNotBlank())
        }

        assertFailsWith(IllegalArgumentException::class) {
            nonBlankString("")
        }
        assertFailsWith(IllegalArgumentException::class) {
            nonBlankString(" ")
        }
    }

    @Test
    fun testMatchRegex() {
        // email regex pattern
        fun matchRegex(@MatchRegex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\$") value: String) {
            // compiler will generate:
            // require(value.matchRegex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\$")
        }

        matchRegex("test@example.com")
        assertFailsWith(IllegalArgumentException::class) {
            matchRegex("non email pattern string")
        }
    }

    @Test
    fun testAlphabetic() {
        fun alpha(@Alphabetic value: String) {
            // compiler will generate:
            // require(value.isAlpha()) { ... }
        }

        alpha("abcdefg")
        alpha("日本語")

        assertFailsWith(IllegalArgumentException::class) {
            alpha("12days")
        }
    }

    @Test
    fun testNumeric() {
        fun numeric(@Numeric value: String) {
            // compiler will generate:
            // require(value.isNumeric()) { ... }
        }

        numeric("123456")

        assertFailsWith(IllegalArgumentException::class) {
            numeric("12days")
        }
    }

    @Test
    fun testLength() {
        fun length(@Length(10) value: String) {
            // compiler will generate:
            // require(value.length == 10) { ... }
        }

        length("0123456789")

        assertFailsWith(IllegalArgumentException::class) {
            length("hogehgoe")
        }
    }

    @Test
    fun testMinLength() {
        fun minLength(@MinLength(10) value: String) {
            // compiler will generate:
            // require(value.length => 10) { ... }
        }

        minLength("0123456789")
        minLength("0123456789012345")

        assertFailsWith(IllegalArgumentException::class) {
            minLength("012345678")
        }
    }

    @Test
    fun testMaxLength() {
        fun maxLength(@MaxLength(10) value: String) {
            // compiler will generate:
            // require(value.length <= 10) { ... }
        }

        maxLength("01234")
        maxLength("0123456789")

        assertFailsWith(IllegalArgumentException::class) {
            maxLength("01234567890")
        }
    }

    @Test
    fun testPrefixed() {
        fun prefixed(@Prefixed("prefix_") value: String) {}

        prefixed("prefix_value")
        assertFailsWith(IllegalArgumentException::class) {
            prefixed("non_prefix_value")
        }
    }

    @Test
    fun testSuffixed() {
        fun suffixed(@Suffixed("_suffix") value: String) {}

        suffixed("value_suffix")
        assertFailsWith(IllegalArgumentException::class) {
            suffixed("value_suffix_not")
        }
    }

    @Test
    fun testLowerCased() {
        fun lowerCased(@LowerCased value: String) {}

        lowerCased("value")
        assertFailsWith(IllegalArgumentException::class) {
            lowerCased("Value")
        }
    }

    @Test
    fun testUpperCased() {
        fun upperCased(@UpperCased value: String) {}

        upperCased("VALUE")
        assertFailsWith(IllegalArgumentException::class) {
            upperCased("value")
        }
    }
}
