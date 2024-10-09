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

@Target(AnnotationTarget.VALUE_PARAMETER, AnnotationTarget.LOCAL_VARIABLE)
annotation class RangedInt(
    val start: Int,
    val end: Int,
    val rule: RangeRule = RangeRule.InclusiveInclusive,
)

@Target(AnnotationTarget.VALUE_PARAMETER, AnnotationTarget.LOCAL_VARIABLE)
annotation class RangedLong(
    val start: Long,
    val end: Long,
    val rule: RangeRule = RangeRule.InclusiveInclusive,
)

@Target(AnnotationTarget.VALUE_PARAMETER, AnnotationTarget.LOCAL_VARIABLE)
annotation class RangedShort(
    val start: Short,
    val end: Short,
    val rule: RangeRule = RangeRule.InclusiveInclusive,
)

@Target(AnnotationTarget.VALUE_PARAMETER, AnnotationTarget.LOCAL_VARIABLE)
annotation class RangedByte(
    val start: Byte,
    val end: Byte,
    val rule: RangeRule = RangeRule.InclusiveInclusive,
)

@Target(AnnotationTarget.VALUE_PARAMETER, AnnotationTarget.LOCAL_VARIABLE)
annotation class RangedDouble(
    val start: Double,
    val end: Double,
    val rule: RangeRule = RangeRule.InclusiveInclusive,
)

@Target(AnnotationTarget.VALUE_PARAMETER, AnnotationTarget.LOCAL_VARIABLE)
annotation class RangedFloat(
    val start: Float,
    val end: Float,
    val rule: RangeRule = RangeRule.InclusiveInclusive,
)

@Target(AnnotationTarget.VALUE_PARAMETER, AnnotationTarget.LOCAL_VARIABLE)
annotation class GreaterThan(val threshold: Double)

@Target(AnnotationTarget.VALUE_PARAMETER, AnnotationTarget.LOCAL_VARIABLE)
annotation class GreaterThanOrEquals(val threshold: Double)

@Target(AnnotationTarget.VALUE_PARAMETER, AnnotationTarget.LOCAL_VARIABLE)
annotation class LessThan(val threshold: Double)

@Target(AnnotationTarget.VALUE_PARAMETER, AnnotationTarget.LOCAL_VARIABLE)
annotation class LessThanOrEquals(val threshold: Double)
