package com.kitakkun.kondition.compiler.k2

import com.kitakkun.kondition.compiler.common.KonditionConsts
import org.jetbrains.kotlin.diagnostics.DiagnosticReporter
import org.jetbrains.kotlin.diagnostics.reportOn
import org.jetbrains.kotlin.fir.analysis.checkers.MppCheckerKind
import org.jetbrains.kotlin.fir.analysis.checkers.context.CheckerContext
import org.jetbrains.kotlin.fir.analysis.checkers.expression.FirAnnotationChecker
import org.jetbrains.kotlin.fir.declarations.toAnnotationClassId
import org.jetbrains.kotlin.fir.expressions.FirAnnotation

object InvalidRangeChecker : FirAnnotationChecker(MppCheckerKind.Common) {
    private val rangedAnnotationClassIds = listOf(
        KonditionConsts.RangedClassId,
        KonditionConsts.RangedDecimalClassId,
        KonditionConsts.CoerceInClassId,
        KonditionConsts.CoerceInDecimalClassId,
    )

    override fun check(expression: FirAnnotation, context: CheckerContext, reporter: DiagnosticReporter) {
        val annotationClassId = expression.toAnnotationClassId(context.session) ?: return

        if (annotationClassId !in rangedAnnotationClassIds) return

        val (start, end) = expression.argumentMapping.mapping.values
            .map { VersionSpecificAPI.INSTANCE.evaluateAsLiteralValue(it, context.session) as? Number }
            .map { it?.toDouble() }

        if (start == null || end == null) return

        if (start > end) {
            reporter.reportOn(
                source = expression.source,
                factory = KonditionErrors.INVALID_RANGE,
                a = "($start, $end)",
                b = annotationClassId,
                context = context,
            )
        }
    }
}
