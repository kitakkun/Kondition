package com.kitakkun.kondition.core.annotation

import kotlin.reflect.KClass

/**
 * ```kotlin
 * @Kondition(implementationClass = MyKonditionImpl::class)
 * annotation class MyKondition(val arg1: Int, val arg2: Int)
 *
 * interface MyKonditionImpl {
 *     fun validate(value: Int, arg1: Int, val arg2: Int): Boolean {
 *         return value in arg1..arg2
 *     }
 *
 *     fun produceErrorMessage(value: Int): String {
 *         return "error"
 *     }
 * }
 * ```
 */
@Target(AnnotationTarget.ANNOTATION_CLASS)
annotation class RequirementKondition(val implementationClass: KClass<*>)

@Target(AnnotationTarget.ANNOTATION_CLASS)
annotation class FittingKondition(val implementationClass: KClass<*>)
