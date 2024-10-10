@file:Suppress("UNUSED")

package io.github.kitakkun.kondition.core.annotation

@Target(AnnotationTarget.VALUE_PARAMETER, AnnotationTarget.LOCAL_VARIABLE)
annotation class NonZero

@Target(AnnotationTarget.VALUE_PARAMETER, AnnotationTarget.LOCAL_VARIABLE)
annotation class Positive

@Target(AnnotationTarget.VALUE_PARAMETER, AnnotationTarget.LOCAL_VARIABLE)
annotation class Negative

@Target(AnnotationTarget.VALUE_PARAMETER, AnnotationTarget.LOCAL_VARIABLE)
annotation class NonPositive

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
@Target(AnnotationTarget.VALUE_PARAMETER, AnnotationTarget.LOCAL_VARIABLE)
annotation class Ranged(
    val start: Long,
    val end: Long,
    val rule: RangeRule = RangeRule.InclusiveInclusive,
)

/**
 * the argument type is [Double], but applicable for [Float] or other numbers.
 */
@Target(AnnotationTarget.VALUE_PARAMETER, AnnotationTarget.LOCAL_VARIABLE)
annotation class RangedDecimal(
    val start: Double,
    val end: Double,
    val rule: RangeRule = RangeRule.InclusiveInclusive,
)

/**
 * the argument type is [Long], but applicable for [Int], [Short], [Byte], or other numbers.
 */
@Target(AnnotationTarget.VALUE_PARAMETER, AnnotationTarget.LOCAL_VARIABLE)
annotation class GreaterThan(val threshold: Long)

/**
 * the argument type is [Double], but applicable for [Float] or other numbers.
 */
@Target(AnnotationTarget.VALUE_PARAMETER, AnnotationTarget.LOCAL_VARIABLE)
annotation class GreaterThanDecimal(val threshold: Double)

@Target(AnnotationTarget.VALUE_PARAMETER, AnnotationTarget.LOCAL_VARIABLE)
annotation class GreaterThanOrEquals(val threshold: Long)

@Target(AnnotationTarget.VALUE_PARAMETER, AnnotationTarget.LOCAL_VARIABLE)
annotation class GreaterThanOrEqualsDecimal(val threshold: Double)

@Target(AnnotationTarget.VALUE_PARAMETER, AnnotationTarget.LOCAL_VARIABLE)
annotation class LessThan(val threshold: Long)

@Target(AnnotationTarget.VALUE_PARAMETER, AnnotationTarget.LOCAL_VARIABLE)
annotation class LessThanDecimal(val threshold: Double)

@Target(AnnotationTarget.VALUE_PARAMETER, AnnotationTarget.LOCAL_VARIABLE)
annotation class LessThanOrEquals(val threshold: Double)

@Target(AnnotationTarget.VALUE_PARAMETER, AnnotationTarget.LOCAL_VARIABLE)
annotation class LessThanOrEqualsDecimal(val threshold: Long)
