@file:Suppress("UNUSED")

package io.github.kitakkun.kondition.core.annotation

@Target(AnnotationTarget.VALUE_PARAMETER, AnnotationTarget.LOCAL_VARIABLE)
annotation class NonEmpty

@Target(AnnotationTarget.VALUE_PARAMETER, AnnotationTarget.LOCAL_VARIABLE)
annotation class NonBlank

@Target(AnnotationTarget.VALUE_PARAMETER, AnnotationTarget.LOCAL_VARIABLE)
annotation class MatchRegex(val pattern: String)

@Target(AnnotationTarget.VALUE_PARAMETER, AnnotationTarget.LOCAL_VARIABLE)
annotation class Alphabetic

@Target(AnnotationTarget.VALUE_PARAMETER, AnnotationTarget.LOCAL_VARIABLE)
annotation class Numeric

@Target(AnnotationTarget.VALUE_PARAMETER, AnnotationTarget.LOCAL_VARIABLE)
annotation class Length(val length: Int)

@Target(AnnotationTarget.VALUE_PARAMETER, AnnotationTarget.LOCAL_VARIABLE)
annotation class MinLength(val length: Int)

@Target(AnnotationTarget.VALUE_PARAMETER, AnnotationTarget.LOCAL_VARIABLE)
annotation class MaxLength(val length: Int)

@Target(AnnotationTarget.VALUE_PARAMETER, AnnotationTarget.LOCAL_VARIABLE)
annotation class Prefixed(val prefix: String)

@Target(AnnotationTarget.VALUE_PARAMETER, AnnotationTarget.LOCAL_VARIABLE)
annotation class Suffixed(val suffix: String)
