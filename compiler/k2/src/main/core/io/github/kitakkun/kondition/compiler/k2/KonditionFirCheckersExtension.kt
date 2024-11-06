package io.github.kitakkun.kondition.compiler.k2

import org.jetbrains.kotlin.fir.FirSession
import org.jetbrains.kotlin.fir.analysis.checkers.declaration.DeclarationCheckers
import org.jetbrains.kotlin.fir.analysis.checkers.declaration.FirCallableDeclarationChecker
import org.jetbrains.kotlin.fir.analysis.checkers.declaration.FirSimpleFunctionChecker
import org.jetbrains.kotlin.fir.analysis.checkers.expression.ExpressionCheckers
import org.jetbrains.kotlin.fir.analysis.checkers.expression.FirAnnotationChecker
import org.jetbrains.kotlin.fir.analysis.extensions.FirAdditionalCheckersExtension

class KonditionFirCheckersExtension(session: FirSession) : FirAdditionalCheckersExtension(session) {
    override val declarationCheckers = object : DeclarationCheckers() {
        override val simpleFunctionCheckers: Set<FirSimpleFunctionChecker> = setOf(AbortStrategyChecker)
        override val callableDeclarationCheckers: Set<FirCallableDeclarationChecker> = setOf(NonApplicableAnnotationUsageChecker)
    }
    override val expressionCheckers = object : ExpressionCheckers() {
        override val annotationCheckers: Set<FirAnnotationChecker> = setOf(OutOfRangeValueChecker)
    }
}
