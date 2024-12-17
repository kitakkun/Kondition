package com.kitakkun.kondition.test.edgecases

import com.kitakkun.kondition.core.annotation.CoerceAtMost
import com.kitakkun.kondition.core.annotation.FittingKondition
import com.kitakkun.kondition.core.annotation.NonEmpty
import com.kitakkun.kondition.core.annotation.RequirementKondition
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

@FittingKondition(implementationClass = FitterImpl::class)
annotation class Fitter

class FitterImpl : FitterKondition {
    override fun fit() {

    }
}

@RequirementKondition(implementationClass = HogeImpl::class)
annotation class Hoge(val a: Int, val b: Int)

class HogeImpl : HogeKondition {
    override fun validate(value: Any, a: Int, b: Int): Boolean {
        return false
    }

    override fun produceErrorMessage(value: Any, a: Int, b: Int): String {
        return ""
    }
}
