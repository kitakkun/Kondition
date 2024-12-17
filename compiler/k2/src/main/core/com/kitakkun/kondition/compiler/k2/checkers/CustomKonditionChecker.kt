package com.kitakkun.kondition.compiler.k2.checkers

import com.kitakkun.kondition.compiler.common.KonditionConsts
import com.kitakkun.kondition.compiler.k2.KonditionErrors
import org.jetbrains.kotlin.descriptors.ClassKind
import org.jetbrains.kotlin.diagnostics.DiagnosticReporter
import org.jetbrains.kotlin.diagnostics.reportOn
import org.jetbrains.kotlin.fir.analysis.checkers.MppCheckerKind
import org.jetbrains.kotlin.fir.analysis.checkers.context.CheckerContext
import org.jetbrains.kotlin.fir.analysis.checkers.declaration.FirRegularClassChecker
import org.jetbrains.kotlin.fir.declarations.FirRegularClass
import org.jetbrains.kotlin.fir.declarations.getAnnotationByClassId
import org.jetbrains.kotlin.fir.declarations.getKClassArgument
import org.jetbrains.kotlin.fir.declarations.hasAnnotation
import org.jetbrains.kotlin.fir.types.toRegularClassSymbol
import org.jetbrains.kotlin.name.Name

object CustomKonditionChecker : FirRegularClassChecker(MppCheckerKind.Common) {
    override fun check(declaration: FirRegularClass, context: CheckerContext, reporter: DiagnosticReporter) {
        if (declaration.classKind != ClassKind.ANNOTATION_CLASS) return

        if (!declaration.hasAnnotation(KonditionConsts.KonditionClassId, context.session)) return

        val konditionAnnotation = declaration.getAnnotationByClassId(KonditionConsts.KonditionClassId, context.session) ?: return
        val implementationClass = konditionAnnotation.getKClassArgument(Name.identifier("implementationClass"), context.session)
        val implementationClassSymbol = implementationClass?.toRegularClassSymbol(context.session) ?: return

        val validatorFunction = implementationClassSymbol.declarationSymbols.find { it.hasAnnotation(KonditionConsts.ValidatorClassId, context.session) }
        val fitterFunction = implementationClassSymbol.declarationSymbols.find { it.hasAnnotation(KonditionConsts.FitterClassId, context.session) }
        val errorMessageProviderFunction = implementationClassSymbol.declarationSymbols.find {
            it.hasAnnotation(
                KonditionConsts.ErrorMessageProviderClassId, context.session
            )
        }

        if (validatorFunction != null && fitterFunction != null) {
            reporter.reportOn(
                source = implementationClassSymbol.source,
                context = context,
                factory = KonditionErrors.CANT_HAVE_BOTH_VALIDATOR_AND_FITTER,
                a = implementationClassSymbol.classId,
            )
        }
    }
}
