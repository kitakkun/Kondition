package io.github.kitakkun.kondition.compiler.fir

import io.github.kitakkun.kondition.core.annotation.GreaterThan
import io.github.kitakkun.kondition.core.annotation.GreaterThanDecimal
import io.github.kitakkun.kondition.core.annotation.GreaterThanOrEquals
import io.github.kitakkun.kondition.core.annotation.GreaterThanOrEqualsDecimal
import io.github.kitakkun.kondition.core.annotation.LessThan
import io.github.kitakkun.kondition.core.annotation.LessThanDecimal
import io.github.kitakkun.kondition.core.annotation.LessThanOrEquals
import io.github.kitakkun.kondition.core.annotation.LessThanOrEqualsDecimal
import io.github.kitakkun.kondition.core.annotation.Ranged
import io.github.kitakkun.kondition.core.annotation.RangedDecimal
import org.jetbrains.kotlin.descriptors.runtime.structure.classId
import org.jetbrains.kotlin.diagnostics.DiagnosticReporter
import org.jetbrains.kotlin.diagnostics.reportOn
import org.jetbrains.kotlin.fir.FirSession
import org.jetbrains.kotlin.fir.analysis.checkers.MppCheckerKind
import org.jetbrains.kotlin.fir.analysis.checkers.context.CheckerContext
import org.jetbrains.kotlin.fir.analysis.checkers.context.findClosest
import org.jetbrains.kotlin.fir.analysis.checkers.expression.FirAnnotationChecker
import org.jetbrains.kotlin.fir.declarations.FirVariable
import org.jetbrains.kotlin.fir.declarations.evaluateAs
import org.jetbrains.kotlin.fir.declarations.toAnnotationClassLikeSymbol
import org.jetbrains.kotlin.fir.expressions.FirAnnotation
import org.jetbrains.kotlin.fir.expressions.FirLiteralExpression
import org.jetbrains.kotlin.fir.types.classId
import org.jetbrains.kotlin.fir.types.coneType
import org.jetbrains.kotlin.name.ClassId
import kotlin.math.absoluteValue

object OutOfRangeValueChecker : FirAnnotationChecker(mppKind = MppCheckerKind.Common) {
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

    override fun check(expression: FirAnnotation, context: CheckerContext, reporter: DiagnosticReporter) {
        val annotationClassSymbol = expression.toAnnotationClassLikeSymbol(context.session) ?: return
        if (annotationClassSymbol.classId !in numberAnnotations) return

        val variable = context.findClosest<FirVariable>() ?: return
        val variableClassId = variable.returnTypeRef.coneType.classId ?: return

        expression.argumentMapping.mapping.values
            .mapNotNull { it.evaluateAs<FirLiteralExpression<*>>(context.session) }
            .forEach { literalExpression ->
                val value = literalExpression.value
                when (value) {
                    is Long -> {
                        if (shouldShowOutOfRangeWarning(value, variableClassId, context.session)) {
                            reporter.reportOn(
                                context = context,
                                source = literalExpression.source,
                                factory = KonditionErrors.OUT_OF_RANGE,
                                a = value,
                                b = variableClassId,
                            )
                        }
                    }

                    is Double -> {
                        if (shouldShowOutOfRangeWarning(value, variableClassId, context.session)) {
                            reporter.reportOn(
                                context = context,
                                source = literalExpression.source,
                                factory = KonditionErrors.OUT_OF_RANGE,
                                a = value,
                                b = variableClassId,
                            )
                        }
                    }
                }
            }
    }

    private fun shouldShowOutOfRangeWarning(
        value: Long,
        classId: ClassId,
        session: FirSession,
    ): Boolean = when (classId) {
        session.builtinTypes.intType.coneType.classId -> value < Int.MIN_VALUE || value > Int.MAX_VALUE
        session.builtinTypes.byteType.coneType.classId -> value < Byte.MIN_VALUE || value > Byte.MAX_VALUE
        session.builtinTypes.shortType.coneType.classId -> value < Short.MIN_VALUE || value > Short.MAX_VALUE
        session.builtinTypes.floatType.coneType.classId -> (Float.MIN_VALUE - value).absoluteValue < Float.MIN_VALUE
        session.builtinTypes.doubleType.coneType.classId -> value < Double.MIN_VALUE
        else -> false
    }

    private fun shouldShowOutOfRangeWarning(
        value: Double,
        classId: ClassId,
        session: FirSession,
    ): Boolean = when (classId) {
        session.builtinTypes.intType.coneType.classId -> value < Int.MIN_VALUE || value > Int.MAX_VALUE
        session.builtinTypes.byteType.coneType.classId -> value < Byte.MIN_VALUE || value > Byte.MAX_VALUE
        session.builtinTypes.shortType.coneType.classId -> value < Short.MIN_VALUE || value > Short.MAX_VALUE
        session.builtinTypes.floatType.coneType.classId -> (Float.MIN_VALUE - value).absoluteValue < Float.MIN_VALUE
        session.builtinTypes.longType.coneType.classId -> value < Long.MIN_VALUE || value > Long.MAX_VALUE
        else -> false
    }
}
