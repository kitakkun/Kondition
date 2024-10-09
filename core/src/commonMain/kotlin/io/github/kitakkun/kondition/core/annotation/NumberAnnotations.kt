@file:Suppress("UNUSED")

package io.github.kitakkun.kondition.core.annotation

@Target(AnnotationTarget.VALUE_PARAMETER)
annotation class NonZero

@Target(AnnotationTarget.VALUE_PARAMETER)
annotation class Positive

@Target(AnnotationTarget.VALUE_PARAMETER)
annotation class Negative

@Target(AnnotationTarget.VALUE_PARAMETER)
annotation class NonPositive

@Target(AnnotationTarget.VALUE_PARAMETER)
annotation class NonNegative

enum class RangeRule {
    InclusiveInclusive,
    ExclusiveExclusive,
    ExclusiveInclusive,
    InclusiveExclusive,
}

@Target(AnnotationTarget.VALUE_PARAMETER)
annotation class RangedInt(
    val start: Int,
    val end: Int,
    val rule: RangeRule = RangeRule.InclusiveInclusive,
)

@Target(AnnotationTarget.VALUE_PARAMETER)
annotation class RangedLong(
    val start: Long,
    val end: Long,
    val rule: RangeRule = RangeRule.InclusiveInclusive,
)

@Target(AnnotationTarget.VALUE_PARAMETER)
annotation class RangedShort(
    val start: Short,
    val end: Short,
    val rule: RangeRule = RangeRule.InclusiveInclusive,
)

@Target(AnnotationTarget.VALUE_PARAMETER)
annotation class RangedByte(
    val start: Byte,
    val end: Byte,
    val rule: RangeRule = RangeRule.InclusiveInclusive,
)

@Target(AnnotationTarget.VALUE_PARAMETER)
annotation class RangedDouble(
    val start: Double,
    val end: Double,
    val rule: RangeRule = RangeRule.InclusiveInclusive,
)

@Target(AnnotationTarget.VALUE_PARAMETER)
annotation class GreaterThan(val threshold: Double)

@Target(AnnotationTarget.VALUE_PARAMETER)
annotation class GreaterThanOrEquals(val threshold: Double)

@Target(AnnotationTarget.VALUE_PARAMETER)
annotation class LessThan(val threshold: Double)

@Target(AnnotationTarget.VALUE_PARAMETER)
annotation class LessThanOrEquals(val threshold: Double)
