package com.kitakkun.kondition.test.edge_cases

import com.kitakkun.kondition.core.annotation.CoerceAtMost
import com.kitakkun.kondition.core.annotation.NonEmpty
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class ClassConstructorTest {
    @Test
    fun testRequirement() {
        class Data(@NonEmpty val nonEmptyString: String)

        assertFailsWith<IllegalArgumentException> {
            Data("")
        }
        Data("non empty string")
    }

    @Test
    fun testFitting() {
        class Data(@CoerceAtMost(10) val intValue: Int)

        assertEquals(10, Data(20).intValue)
        assertEquals(0, Data(0).intValue)
    }
}
