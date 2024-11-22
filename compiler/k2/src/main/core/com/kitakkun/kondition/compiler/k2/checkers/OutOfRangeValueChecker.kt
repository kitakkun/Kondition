package com.kitakkun.kondition.compiler.k2.checkers

import com.kitakkun.kondition.compiler.common.KonditionConsts
import com.kitakkun.kondition.compiler.k2.api.VersionSpecificAPI
import com.kitakkun.kondition.compiler.k2.diagnostics.KonditionErrors
import org.jetbrains.kotlin.diagnostics.DiagnosticReporter
import org.jetbrains.kotlin.diagnostics.reportOn
import org.jetbrains.kotlin.fir.FirSession
import org.jetbrains.kotlin.fir.analysis.checkers.MppCheckerKind
import org.jetbrains.kotlin.fir.analysis.checkers.context.CheckerContext
import org.jetbrains.kotlin.fir.analysis.checkers.context.findClosest
import org.jetbrains.kotlin.fir.analysis.checkers.expression.FirAnnotationChecker
import org.jetbrains.kotlin.fir.declarations.FirVariable
import org.jetbrains.kotlin.fir.declarations.toAnnotationClassLikeSymbol
import org.jetbrains.kotlin.fir.expressions.FirAnnotation
import org.jetbrains.kotlin.fir.types.classId
import org.jetbrains.kotlin.fir.types.coneType
import org.jetbrains.kotlin.name.ClassId
import kotlin.math.absoluteValue

object OutOfRangeValueChecker : FirAnnotationChecker(mppKind = MppCheckerKind.Common) {
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

    override fun check(expression: FirAnnotation, context: CheckerContext, reporter: DiagnosticReporter) {
        val annotationClassSymbol = expression.toAnnotationClassLikeSymbol(context.session) ?: return
        if (annotationClassSymbol.classId !in numberAnnotations) return

        val variable = context.findClosest<FirVariable>() ?: return
        val variableClassId = variable.returnTypeRef.coneType.classId ?: return

        expression.argumentMapping.mapping.values
            .forEach { literalExpression ->
                val value = VersionSpecificAPI.INSTANCE.evaluateAsLiteralValue(literalExpression, context.session)
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
