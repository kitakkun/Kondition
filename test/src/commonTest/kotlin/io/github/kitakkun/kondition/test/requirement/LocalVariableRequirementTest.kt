package io.github.kitakkun.kondition.test.requirement

import io.github.kitakkun.kondition.core.annotation.Numeric
import kotlin.test.Test
import kotlin.test.assertFailsWith

class LocalVariableRequirementTest {
    @Test
    fun mutableLocalVariablePassTest() {
        fun mutable() {
            @Numeric
            var mutable = "1234"
            mutable = "5678"
        }

        mutable()
    }

    @Test
    fun mutableLocalVariableFailTest() {
        fun mutable() {
            @Numeric
            var mutable = "1234"
            mutable = "abcd"
        }

        assertFailsWith<IllegalStateException> {
            mutable()
        }
    }

    @Test
    fun immutableLocalVariableTest() {
        fun immutable() {
            @Numeric
            val immutable = "1234"
        }

        immutable()
    }

    @Test
    fun immutableLocalVariableFailTest() {
        fun immutable() {
            @Numeric
            val immutable = "abcd"
        }

        assertFailsWith<IllegalStateException> {
            immutable()
        }
    }

    @Test
    fun immutableLocalVariableLateInitPassTest() {
        fun myFunction() {
            @Numeric
            val immutable: String
            immutable = "1234"
        }

        myFunction()
    }

    @Test
    fun immutableLocalVariableLateInitFailTest() {
        fun myFunction() {
            @Numeric
            val immutable: String
            immutable = "abcd"
        }

        assertFailsWith<IllegalStateException> {
            myFunction()
        }
    }
}
