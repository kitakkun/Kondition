@file:Suppress("UNUSED")

package io.github.kitakkun.kondition.core.annotation

@Target(AnnotationTarget.VALUE_PARAMETER)
annotation class NonEmpty

@Target(AnnotationTarget.VALUE_PARAMETER)
annotation class NonBlank

@Target(AnnotationTarget.VALUE_PARAMETER)
annotation class MatchRegex(val pattern: String)

@Target(AnnotationTarget.VALUE_PARAMETER)
annotation class Alphabetic

@Target(AnnotationTarget.VALUE_PARAMETER)
annotation class Numeric

@Target(AnnotationTarget.VALUE_PARAMETER)
annotation class Length(val length: Int)

@Target(AnnotationTarget.VALUE_PARAMETER)
annotation class MinLength(val length: Int)

@Target(AnnotationTarget.VALUE_PARAMETER)
annotation class MaxLength(val length: Int)
