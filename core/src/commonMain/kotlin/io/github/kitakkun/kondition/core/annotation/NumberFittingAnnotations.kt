package io.github.kitakkun.kondition.core.annotation

@NumberOnly
@Target(AnnotationTarget.VALUE_PARAMETER, AnnotationTarget.LOCAL_VARIABLE)
annotation class CoerceIn(val minimumValue: Long, val maximumValue: Long)

@NumberOnly
@Target(AnnotationTarget.VALUE_PARAMETER, AnnotationTarget.LOCAL_VARIABLE)
annotation class CoerceAtLeast(val minimumValue: Long)

@NumberOnly
@Target(AnnotationTarget.VALUE_PARAMETER, AnnotationTarget.LOCAL_VARIABLE)
annotation class CoerceAtMost(val maximumValue: Long)

@NumberOnly
@Target(AnnotationTarget.VALUE_PARAMETER, AnnotationTarget.LOCAL_VARIABLE)
annotation class CoerceInDecimal(val minimumValue: Double, val maximumValue: Double)

@NumberOnly
@Target(AnnotationTarget.VALUE_PARAMETER, AnnotationTarget.LOCAL_VARIABLE)
annotation class CoerceAtLeastDecimal(val minimumValue: Double)

@NumberOnly
@Target(AnnotationTarget.VALUE_PARAMETER, AnnotationTarget.LOCAL_VARIABLE)
annotation class CoerceAtMostDecimal(val maximumValue: Double)
