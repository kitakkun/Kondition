package com.kitakkun.kondition.compiler.k2.checkers

import com.kitakkun.kondition.compiler.common.KonditionConsts
import com.kitakkun.kondition.compiler.k2.diagnostics.KonditionErrors
import org.jetbrains.kotlin.diagnostics.DiagnosticReporter
import org.jetbrains.kotlin.diagnostics.reportOn
import org.jetbrains.kotlin.fir.analysis.checkers.MppCheckerKind
import org.jetbrains.kotlin.fir.analysis.checkers.context.CheckerContext
import org.jetbrains.kotlin.fir.analysis.checkers.expression.FirAnnotationChecker
import org.jetbrains.kotlin.fir.declarations.evaluateAs
import org.jetbrains.kotlin.fir.declarations.toAnnotationClassId
import org.jetbrains.kotlin.fir.expressions.FirAnnotation
import org.jetbrains.kotlin.fir.expressions.FirLiteralExpression

object InvalidRangeChecker : FirAnnotationChecker(MppCheckerKind.Common) {
    private val rangedAnnotationClassIds = listOf(
        KonditionConsts.RangedClassId,
        KonditionConsts.RangedDecimalClassId,
        KonditionConsts.CoerceInClassId,
        KonditionConsts.CoerceInDecimalClassId,
    )

    context(context: CheckerContext, reporter: DiagnosticReporter)
    override fun check(expression: FirAnnotation) {
        val annotationClassId = expression.toAnnotationClassId(context.session) ?: return

        if (annotationClassId !in rangedAnnotationClassIds) return

        val (start, end) = expression.argumentMapping.mapping.values
            .map { it.evaluateAs<FirLiteralExpression>(context.session)?.value as? Number }
            .map { it?.toDouble() }

        if (start == null || end == null) return

        if (start > end) {
            reporter.reportOn(
                source = expression.source,
                factory = KonditionErrors.INVALID_RANGE,
                a = "($start, $end)",
                b = annotationClassId,
            )
        }
    }
}
