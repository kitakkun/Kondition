package io.github.kitakkun.kondition.compiler.fir

import io.github.kitakkun.kondition.core.annotation.AbortStrategy
import io.github.kitakkun.kondition.core.annotation.AbortWith
import org.jetbrains.kotlin.descriptors.runtime.structure.classId
import org.jetbrains.kotlin.diagnostics.DiagnosticReporter
import org.jetbrains.kotlin.diagnostics.reportOn
import org.jetbrains.kotlin.fir.analysis.checkers.MppCheckerKind
import org.jetbrains.kotlin.fir.analysis.checkers.context.CheckerContext
import org.jetbrains.kotlin.fir.analysis.checkers.declaration.FirFunctionChecker
import org.jetbrains.kotlin.fir.declarations.FirFunction
import org.jetbrains.kotlin.fir.declarations.extractEnumValueArgumentInfo
import org.jetbrains.kotlin.fir.declarations.getAnnotationByClassId
import org.jetbrains.kotlin.fir.declarations.utils.nameOrSpecialName

object AbortStrategyChecker : FirFunctionChecker(mppKind = MppCheckerKind.Common) {
    override fun check(declaration: FirFunction, context: CheckerContext, reporter: DiagnosticReporter) {
        val annotation = declaration.getAnnotationByClassId(AbortWith::class.java.classId, context.session) ?: return
        val enumValueArgumentInfo = annotation.argumentMapping.mapping.values.single().extractEnumValueArgumentInfo() ?: return
        val enumValue = AbortStrategy.entries.find { it.name == enumValueArgumentInfo.enumEntryName.asString() } ?: return

        when (enumValue) {
            AbortStrategy.ThrowError -> TODO()
            AbortStrategy.ReturnWithNull -> TODO()
            AbortStrategy.Return -> {
                reporter.reportOn(
                    context = context,
                    source = declaration.source,
                    factory = KonditionErrors.RETURN_IMPOSSIBLE_FOR_NON_UNIT_TYPE,
                    a = declaration.nameOrSpecialName.asString(),
                )
            }
        }
    }
}
