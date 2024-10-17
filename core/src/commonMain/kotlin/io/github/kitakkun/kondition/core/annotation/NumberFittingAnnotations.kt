package io.github.kitakkun.kondition.core.annotation

@Target(AnnotationTarget.VALUE_PARAMETER, AnnotationTarget.LOCAL_VARIABLE)
annotation class CoerceIn(val minimumValue: Long, val maximumValue: Long)

@Target(AnnotationTarget.VALUE_PARAMETER, AnnotationTarget.LOCAL_VARIABLE)
annotation class CoerceAtLeast(val minimumValue: Long)

@Target(AnnotationTarget.VALUE_PARAMETER, AnnotationTarget.LOCAL_VARIABLE)
annotation class CoerceAtMost(val maximumValue: Long)
