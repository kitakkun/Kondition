package io.github.kitakkun.kondition.compiler.fir

import org.jetbrains.kotlin.diagnostics.KtDiagnosticFactoryToRendererMap
import org.jetbrains.kotlin.diagnostics.KtDiagnosticRenderers.TO_STRING
import org.jetbrains.kotlin.diagnostics.rendering.BaseDiagnosticRendererFactory

object KonditionErrorMessages : BaseDiagnosticRendererFactory() {
    @Suppress("ktlint:standard:property-naming")
    override val MAP: KtDiagnosticFactoryToRendererMap = KtDiagnosticFactoryToRendererMap("kondition").apply {
        put(
            KonditionErrors.RETURN_IMPOSSIBLE_FOR_NON_UNIT_TYPE,
            "{0}: RETURN strategy can't be applied to the function with non-Unit return type.",
            rendererA = TO_STRING,
        )
        put(
            KonditionErrors.OUT_OF_RANGE,
            "{0} is out of range of {1} type. It may cause unexpected behavior.",
            rendererA = TO_STRING,
            rendererB = TO_STRING,
        )
        put(
            KonditionErrors.INVALID_NUMBER_ANNOTATION_USAGE,
            "Invalid usage of Number annotation",
        )
        put(
            KonditionErrors.INVALID_STRING_ANNOTATION_USAGE,
            "Invalid usage of String annotation",
        )
    }
}
