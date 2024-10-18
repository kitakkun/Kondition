package io.github.kitakkun.kondition.core.annotation

@Target(AnnotationTarget.VALUE_PARAMETER, AnnotationTarget.LOCAL_VARIABLE)
annotation class AddPrefix(val prefix: String)

@Target(AnnotationTarget.VALUE_PARAMETER, AnnotationTarget.LOCAL_VARIABLE)
annotation class AddSuffix(val suffix: String)

@Target(AnnotationTarget.VALUE_PARAMETER, AnnotationTarget.LOCAL_VARIABLE)
annotation class RemovePrefix(val prefix: String)

@Target(AnnotationTarget.VALUE_PARAMETER, AnnotationTarget.LOCAL_VARIABLE)
annotation class RemoveSuffix(val suffix: String)

@Target(AnnotationTarget.VALUE_PARAMETER, AnnotationTarget.LOCAL_VARIABLE)
annotation class Trim

@Target(AnnotationTarget.VALUE_PARAMETER, AnnotationTarget.LOCAL_VARIABLE)
annotation class TrimStart

@Target(AnnotationTarget.VALUE_PARAMETER, AnnotationTarget.LOCAL_VARIABLE)
annotation class TrimEnd

@Target(AnnotationTarget.VALUE_PARAMETER, AnnotationTarget.LOCAL_VARIABLE)
annotation class Take(val length: Int)

@Target(AnnotationTarget.VALUE_PARAMETER, AnnotationTarget.LOCAL_VARIABLE)
annotation class TakeLast(val length: Int)

@Target(AnnotationTarget.VALUE_PARAMETER, AnnotationTarget.LOCAL_VARIABLE)
annotation class Drop(val length: Int)

@Target(AnnotationTarget.VALUE_PARAMETER, AnnotationTarget.LOCAL_VARIABLE)
annotation class DropLast(val length: Int)
