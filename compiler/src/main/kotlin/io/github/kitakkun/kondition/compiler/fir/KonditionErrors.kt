package io.github.kitakkun.kondition.compiler.fir

import org.jetbrains.kotlin.com.intellij.psi.PsiElement
import org.jetbrains.kotlin.diagnostics.error0
import org.jetbrains.kotlin.diagnostics.error1
import org.jetbrains.kotlin.diagnostics.error2
import org.jetbrains.kotlin.diagnostics.rendering.RootDiagnosticRendererFactory
import org.jetbrains.kotlin.name.ClassId

object KonditionErrors {
    val RETURN_IMPOSSIBLE_FOR_NON_UNIT_TYPE by error1<PsiElement, String>()
    val OUT_OF_RANGE by error2<PsiElement, Number, ClassId>()
    val INVALID_STRING_ANNOTATION_USAGE by error0<PsiElement>()
    val INVALID_NUMBER_ANNOTATION_USAGE by error0<PsiElement>()

    init {
        RootDiagnosticRendererFactory.registerFactory(KonditionErrorMessages)
    }
}
