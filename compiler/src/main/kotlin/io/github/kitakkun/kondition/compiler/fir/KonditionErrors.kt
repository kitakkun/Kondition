package io.github.kitakkun.kondition.compiler.fir

import org.jetbrains.kotlin.com.intellij.psi.PsiElement
import org.jetbrains.kotlin.diagnostics.error1
import org.jetbrains.kotlin.diagnostics.rendering.RootDiagnosticRendererFactory

object KonditionErrors {
    val RETURN_IMPOSSIBLE_FOR_NON_UNIT_TYPE by error1<PsiElement, String>()

    init {
        RootDiagnosticRendererFactory.registerFactory(KonditionErrorMessages)
    }
}
