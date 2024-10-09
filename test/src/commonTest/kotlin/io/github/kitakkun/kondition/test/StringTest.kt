package io.github.kitakkun.kondition.test

import io.github.kitakkun.kondition.core.annotation.Alphabetic
import io.github.kitakkun.kondition.core.annotation.MatchRegex
import io.github.kitakkun.kondition.core.annotation.NonBlank
import io.github.kitakkun.kondition.core.annotation.NonEmpty
import io.github.kitakkun.kondition.core.annotation.Numeric
import kotlin.test.Test
import kotlin.test.assertFailsWith

class StringTest {
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
}
