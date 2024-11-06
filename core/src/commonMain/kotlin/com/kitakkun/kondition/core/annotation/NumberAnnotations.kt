@file:Suppress("UNUSED")

package com.kitakkun.kondition.core.annotation

@NumberOnly
@Target(AnnotationTarget.VALUE_PARAMETER, AnnotationTarget.LOCAL_VARIABLE)
annotation class NonZero

@NumberOnly
@Target(AnnotationTarget.VALUE_PARAMETER, AnnotationTarget.LOCAL_VARIABLE)
annotation class Positive

@NumberOnly
@Target(AnnotationTarget.VALUE_PARAMETER, AnnotationTarget.LOCAL_VARIABLE)
annotation class Negative

@NumberOnly
@Target(AnnotationTarget.VALUE_PARAMETER, AnnotationTarget.LOCAL_VARIABLE)
annotation class NonPositive

@NumberOnly
@Target(AnnotationTarget.VALUE_PARAMETER, AnnotationTarget.LOCAL_VARIABLE)
annotation class NonNegative

enum class RangeRule {
    InclusiveInclusive,
    ExclusiveExclusive,
    ExclusiveInclusive,
    InclusiveExclusive,
}

/**
 * the argument type is [Long], but applicable for [Int], [Short], [Byte], or other numbers.
 */
@NumberOnly
@Target(AnnotationTarget.VALUE_PARAMETER, AnnotationTarget.LOCAL_VARIABLE)
annotation class Ranged(
    val start: Long,
    val end: Long,
    val rule: RangeRule = RangeRule.InclusiveInclusive,
)

/**
 * the argument type is [Double], but applicable for [Float] or other numbers.
 */
@NumberOnly
@Target(AnnotationTarget.VALUE_PARAMETER, AnnotationTarget.LOCAL_VARIABLE)
annotation class RangedDecimal(
    val start: Double,
    val end: Double,
    val rule: RangeRule = RangeRule.InclusiveInclusive,
)

/**
 * the argument type is [Long], but applicable for [Int], [Short], [Byte], or other numbers.
 */
@NumberOnly
@Target(AnnotationTarget.VALUE_PARAMETER, AnnotationTarget.LOCAL_VARIABLE)
annotation class GreaterThan(val threshold: Long)

/**
 * the argument type is [Double], but applicable for [Float] or other numbers.
 */
@NumberOnly
@Target(AnnotationTarget.VALUE_PARAMETER, AnnotationTarget.LOCAL_VARIABLE)
annotation class GreaterThanDecimal(val threshold: Double)

@NumberOnly
@Target(AnnotationTarget.VALUE_PARAMETER, AnnotationTarget.LOCAL_VARIABLE)
annotation class GreaterThanOrEquals(val threshold: Long)

@NumberOnly
@Target(AnnotationTarget.VALUE_PARAMETER, AnnotationTarget.LOCAL_VARIABLE)
annotation class GreaterThanOrEqualsDecimal(val threshold: Double)

@NumberOnly
@Target(AnnotationTarget.VALUE_PARAMETER, AnnotationTarget.LOCAL_VARIABLE)
annotation class LessThan(val threshold: Long)

@NumberOnly
@Target(AnnotationTarget.VALUE_PARAMETER, AnnotationTarget.LOCAL_VARIABLE)
annotation class LessThanDecimal(val threshold: Double)

@NumberOnly
@Target(AnnotationTarget.VALUE_PARAMETER, AnnotationTarget.LOCAL_VARIABLE)
annotation class LessThanOrEquals(val threshold: Long)

@NumberOnly
@Target(AnnotationTarget.VALUE_PARAMETER, AnnotationTarget.LOCAL_VARIABLE)
annotation class LessThanOrEqualsDecimal(val threshold: Double)
