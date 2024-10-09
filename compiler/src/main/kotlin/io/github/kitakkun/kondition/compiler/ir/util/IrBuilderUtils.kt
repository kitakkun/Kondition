package io.github.kitakkun.kondition.compiler.ir.util

import org.jetbrains.kotlin.ir.builders.IrBuilderWithScope
import org.jetbrains.kotlin.ir.expressions.impl.IrConstImpl

fun IrBuilderWithScope.irFloat(value: Float) = IrConstImpl.float(startOffset, endOffset, context.irBuiltIns.floatType, value)
fun IrBuilderWithScope.irDouble(value: Double) = IrConstImpl.double(startOffset, endOffset, context.irBuiltIns.doubleType, value)
