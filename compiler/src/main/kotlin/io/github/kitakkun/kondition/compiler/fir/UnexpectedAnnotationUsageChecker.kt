package io.github.kitakkun.kondition.compiler.fir

import io.github.kitakkun.kondition.core.annotation.Alphabetic
import io.github.kitakkun.kondition.core.annotation.GreaterThan
import io.github.kitakkun.kondition.core.annotation.GreaterThanDecimal
import io.github.kitakkun.kondition.core.annotation.GreaterThanOrEquals
import io.github.kitakkun.kondition.core.annotation.GreaterThanOrEqualsDecimal
import io.github.kitakkun.kondition.core.annotation.Length
import io.github.kitakkun.kondition.core.annotation.LessThan
import io.github.kitakkun.kondition.core.annotation.LessThanDecimal
import io.github.kitakkun.kondition.core.annotation.LessThanOrEquals
import io.github.kitakkun.kondition.core.annotation.LessThanOrEqualsDecimal
import io.github.kitakkun.kondition.core.annotation.MatchRegex
import io.github.kitakkun.kondition.core.annotation.MaxLength
import io.github.kitakkun.kondition.core.annotation.MinLength
import io.github.kitakkun.kondition.core.annotation.NonBlank
import io.github.kitakkun.kondition.core.annotation.NonEmpty
import io.github.kitakkun.kondition.core.annotation.Numeric
import io.github.kitakkun.kondition.core.annotation.Ranged
import io.github.kitakkun.kondition.core.annotation.RangedDecimal
import org.jetbrains.kotlin.descriptors.runtime.structure.classId
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
        Ranged::class.java.classId,
        RangedDecimal::class.java.classId,
        GreaterThan::class.java.classId,
        GreaterThanDecimal::class.java.classId,
        GreaterThanOrEquals::class.java.classId,
        GreaterThanOrEqualsDecimal::class.java.classId,
        LessThan::class.java.classId,
        LessThanDecimal::class.java.classId,
        LessThanOrEquals::class.java.classId,
        LessThanOrEqualsDecimal::class.java.classId,
    )

    private val stringAnnotations = setOf(
        NonEmpty::class.java.classId,
        NonBlank::class.java.classId,
        Length::class.java.classId,
        MaxLength::class.java.classId,
        MinLength::class.java.classId,
        Alphabetic::class.java.classId,
        Numeric::class.java.classId,
        MatchRegex::class.java.classId,
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
