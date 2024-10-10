package io.github.kitakkun.kondition.compiler.fir

import org.jetbrains.kotlin.com.intellij.psi.PsiElement
import org.jetbrains.kotlin.diagnostics.error1
import org.jetbrains.kotlin.diagnostics.error2
import org.jetbrains.kotlin.diagnostics.rendering.RootDiagnosticRendererFactory
import org.jetbrains.kotlin.name.ClassId

object KonditionErrors {
    val RETURN_IMPOSSIBLE_FOR_NON_UNIT_TYPE by error1<PsiElement, String>()
    val OUT_OF_RANGE by error2<PsiElement, Number, ClassId>()

    init {
        RootDiagnosticRendererFactory.registerFactory(KonditionErrorMessages)
    }
}
