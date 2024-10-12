package io.github.kitakkun.kondition.compiler.fir

import io.github.kitakkun.kondition.compiler.KonditionConsts
import org.jetbrains.kotlin.diagnostics.DiagnosticReporter
import org.jetbrains.kotlin.diagnostics.reportOn
import org.jetbrains.kotlin.fir.analysis.checkers.MppCheckerKind
import org.jetbrains.kotlin.fir.analysis.checkers.context.CheckerContext
import org.jetbrains.kotlin.fir.analysis.checkers.context.findClosest
import org.jetbrains.kotlin.fir.analysis.checkers.expression.FirAnnotationChecker
import org.jetbrains.kotlin.fir.declarations.FirDeclaration
import org.jetbrains.kotlin.fir.declarations.toAnnotationClassLikeSymbol
import org.jetbrains.kotlin.fir.expressions.FirAnnotation
import org.jetbrains.kotlin.fir.symbols.impl.FirCallableSymbol
import org.jetbrains.kotlin.fir.types.classId

object UnexpectedAnnotationUsageChecker : FirAnnotationChecker(mppKind = MppCheckerKind.Common) {
    private val numberAnnotations = setOf(
        KonditionConsts.RangedClassId,
        KonditionConsts.RangedDecimalClassId,
        KonditionConsts.GreaterThanClassId,
        KonditionConsts.GreaterThanDecimalClassId,
        KonditionConsts.GreaterThanOrEqualsClassId,
        KonditionConsts.GreaterThanOrEqualsDecimalClassId,
        KonditionConsts.LessThanClassId,
        KonditionConsts.LessThanDecimalClassId,
        KonditionConsts.LessThanOrEqualsClassId,
        KonditionConsts.LessThanOrEqualsDecimalClassId,
    )

    private val stringAnnotations = setOf(
        KonditionConsts.NonEmptyClassId,
        KonditionConsts.NonBlankClassId,
        KonditionConsts.LengthClassId,
        KonditionConsts.MaxLengthClassId,
        KonditionConsts.MinLengthClassId,
        KonditionConsts.AlphabeticClassId,
        KonditionConsts.NumericClassId,
        KonditionConsts.MatchRegexClassId,
    )

    override fun check(expression: FirAnnotation, context: CheckerContext, reporter: DiagnosticReporter) {
        val declaration = context.findClosest<FirDeclaration>() ?: return
        val symbol = declaration.symbol as? FirCallableSymbol ?: return
        val classId = symbol.resolvedReturnType.type.classId ?: return

        val annotationClassSymbol = expression.toAnnotationClassLikeSymbol(context.session) ?: return
        if (annotationClassSymbol.classId !in numberAnnotations + stringAnnotations) return

        if (classId == context.session.builtinTypes.stringType.type.classId && annotationClassSymbol.classId !in stringAnnotations) {
            reporter.reportOn(
                context = context,
                source = symbol.source,
                factory = KonditionErrors.INVALID_NUMBER_ANNOTATION_USAGE,
            )
        }

        val numberTypes = listOfNotNull(
            context.session.builtinTypes.longType.type.classId,
            context.session.builtinTypes.floatType.type.classId,
            context.session.builtinTypes.doubleType.type.classId,
            context.session.builtinTypes.byteType.type.classId,
            context.session.builtinTypes.intType.type.classId,
            context.session.builtinTypes.shortType.type.classId,
        )
        if (classId in numberTypes && annotationClassSymbol.classId !in numberAnnotations) {
            reporter.reportOn(
                context = context,
                source = symbol.source,
                factory = KonditionErrors.INVALID_STRING_ANNOTATION_USAGE,
            )
        }
    }
}
