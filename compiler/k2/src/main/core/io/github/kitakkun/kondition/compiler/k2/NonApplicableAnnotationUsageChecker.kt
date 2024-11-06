package io.github.kitakkun.kondition.compiler.k2

import org.jetbrains.kotlin.diagnostics.DiagnosticReporter
import org.jetbrains.kotlin.diagnostics.reportOn
import org.jetbrains.kotlin.fir.analysis.checkers.MppCheckerKind
import org.jetbrains.kotlin.fir.analysis.checkers.context.CheckerContext
import org.jetbrains.kotlin.fir.analysis.checkers.declaration.FirCallableDeclarationChecker
import org.jetbrains.kotlin.fir.declarations.FirCallableDeclaration
import org.jetbrains.kotlin.fir.declarations.evaluateAs
import org.jetbrains.kotlin.fir.declarations.findArgumentByName
import org.jetbrains.kotlin.fir.declarations.getTargetType
import org.jetbrains.kotlin.fir.declarations.toAnnotationClassId
import org.jetbrains.kotlin.fir.expressions.FirArrayLiteral
import org.jetbrains.kotlin.fir.expressions.FirGetClassCall
import org.jetbrains.kotlin.fir.expressions.arguments
import org.jetbrains.kotlin.fir.types.classId
import org.jetbrains.kotlin.fir.types.coneType
import org.jetbrains.kotlin.javac.resolve.classId
import org.jetbrains.kotlin.name.Name

object NonApplicableAnnotationUsageChecker :
    FirCallableDeclarationChecker(mppKind = MppCheckerKind.Common) {
    private val TypeSpecificAnnotationClassId =
        classId("io.github.kitakkun.kondition.core.annotation", "TypeSpecific")

    override fun check(
        declaration: FirCallableDeclaration,
        context: CheckerContext,
        reporter: DiagnosticReporter,
    ) {
        if (declaration.annotations.isEmpty()) return

        val applicableClassIdsMap = declaration.annotations
            .associateWith { annotation ->
                MetaAnnotationsResolver.resolveMetaAnnotationsRecursively(
                    annotation,
                    context.session
                )
                    .filter { metaAnnotation ->
                        metaAnnotation.toAnnotationClassId(context.session) == TypeSpecificAnnotationClassId
                    }
                    .map { typeSpecificAnnotation ->
                        val classes = typeSpecificAnnotation.findArgumentByName(
                            Name.identifier("classes"),
                            returnFirstWhenNotFound = false
                        )
                        val arrayLiteral = classes?.evaluateAs<FirArrayLiteral>(context.session)
                        arrayLiteral?.arguments.orEmpty().mapNotNull {
                            it.evaluateAs<FirGetClassCall>(context.session)
                                ?.getTargetType()?.classId
                        }
                    }
            }
            .mapNotNull { (annotation, metaAnnotations) ->
                if (metaAnnotations.isEmpty()) return@mapNotNull null
                val commonMetaAnnotations = metaAnnotations.reduce { a, b ->
                    a.intersect(b.toSet()).toList()
                }
                if (commonMetaAnnotations.isEmpty()) return@mapNotNull null
                annotation to commonMetaAnnotations
            }
            .toMap()

        val declarationClassId = declaration.returnTypeRef.coneType.classId ?: return

        applicableClassIdsMap.forEach { (annotation, classIds) ->
            if (declarationClassId !in classIds) {
                reporter.reportOn(
                    context = context,
                    source = declaration.source,
                    factory = KonditionErrors.KONDITION_ANNOTATION_USED_AGAINST_NON_APPLICABLE_TYPE,
                    a = annotation.toAnnotationClassId(context.session)!!,
                    b = declarationClassId,
                    c = classIds,
                )
            }
        }
    }
}
