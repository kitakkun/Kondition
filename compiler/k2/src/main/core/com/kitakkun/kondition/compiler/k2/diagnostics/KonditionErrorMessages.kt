package com.kitakkun.kondition.compiler.k2.diagnostics

import org.jetbrains.kotlin.diagnostics.KtDiagnosticFactoryToRendererMap
import org.jetbrains.kotlin.diagnostics.KtDiagnosticRenderers.TO_STRING
import org.jetbrains.kotlin.diagnostics.rendering.BaseDiagnosticRendererFactory

object KonditionErrorMessages : BaseDiagnosticRendererFactory() {
    @Suppress("ktlint:standard:property-naming")
    override val MAP: KtDiagnosticFactoryToRendererMap = KtDiagnosticFactoryToRendererMap("kondition").apply {
        put(
            KonditionErrors.KONDITION_ANNOTATION_USED_AGAINST_NON_APPLICABLE_TYPE,
            "Annotation {0} cannot be applied to the type {1}. Applicable types are: {2}",
            rendererA = TO_STRING,
            rendererB = TO_STRING,
            rendererC = TO_STRING,
        )
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
            KonditionErrors.INVALID_RANGE,
            "The specified range {0} is invalid for {1}. Applying this setting may lead to unexpected behavior or could be ignored.",
            rendererA = TO_STRING,
            rendererB = TO_STRING,
        )
        put(
            KonditionErrors.CANT_HAVE_BOTH_VALIDATOR_AND_FITTER,
            "Custom Konditions can't have both validator and fitter.",
            rendererA = TO_STRING,
        )
    }
}
