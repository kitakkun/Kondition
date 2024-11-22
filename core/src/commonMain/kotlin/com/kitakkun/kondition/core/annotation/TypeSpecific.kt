package com.kitakkun.kondition.core.annotation

import kotlin.reflect.KClass

/**
 * Restrict annotation usages by classes
 */
@Retention(AnnotationRetention.BINARY)
@Target(AnnotationTarget.ANNOTATION_CLASS)
annotation class TypeSpecific(val classes: Array<KClass<*>>)

@Retention(AnnotationRetention.BINARY)
@Target(AnnotationTarget.ANNOTATION_CLASS)
@TypeSpecific(
    classes = [
        Number::class,
        Int::class,
        Long::class,
        Byte::class,
        Short::class,
        Float::class,
        Double::class,
    ],
)
internal annotation class NumberOnly

@Retention(AnnotationRetention.BINARY)
@Target(AnnotationTarget.ANNOTATION_CLASS)
@TypeSpecific([Char::class, String::class])
internal annotation class StringOrCharOnly

@Retention(AnnotationRetention.BINARY)
@Target(AnnotationTarget.ANNOTATION_CLASS)
@TypeSpecific([String::class])
internal annotation class StringOnly
