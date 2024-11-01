package io.github.kitakkun.kondition.compiler.fir.checker

import io.github.kitakkun.kondition.compiler.fir.KonditionErrorMessages
import io.github.kitakkun.kondition.compiler.fir.KonditionErrors
import org.jetbrains.kotlin.diagnostics.AbstractKtDiagnosticFactory
import kotlin.reflect.KProperty
import kotlin.test.Test
import kotlin.test.assertTrue

class KonditionErrorMessageTest {
    @Test
    fun ensureAllMessagesPresent() {
        KonditionErrors::class.members.filterIsInstance<KProperty<AbstractKtDiagnosticFactory>>().forEach {
            assertTrue("The error message for ${it.name} is missing") {
                KonditionErrorMessages.MAP.containsKey(it.call(KonditionErrors))
            }
        }
    }
}
