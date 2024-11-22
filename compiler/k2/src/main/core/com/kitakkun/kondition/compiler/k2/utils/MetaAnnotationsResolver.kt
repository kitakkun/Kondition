package com.kitakkun.kondition.compiler.k2.utils

import org.jetbrains.kotlin.fir.FirSession
import org.jetbrains.kotlin.fir.declarations.toAnnotationClass
import org.jetbrains.kotlin.fir.expressions.FirAnnotation

class MetaAnnotationsResolver private constructor() {
    private val metaAnnotations = mutableSetOf<FirAnnotation>()

    fun resolve(annotation: FirAnnotation, session: FirSession) {
        val localMetaAnnotations = annotation.toAnnotationClass(session)
            ?.annotations
            .orEmpty()
            .filter { it !in metaAnnotations }
        metaAnnotations += localMetaAnnotations
        localMetaAnnotations.forEach { resolve(it, session) }
    }

    companion object {
        fun resolveMetaAnnotationsRecursively(annotation: FirAnnotation, session: FirSession): Set<FirAnnotation> {
            val resolver = MetaAnnotationsResolver()
            resolver.resolve(annotation, session)
            return resolver.metaAnnotations
        }
    }
}
