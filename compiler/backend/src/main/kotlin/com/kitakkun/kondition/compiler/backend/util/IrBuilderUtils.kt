package com.kitakkun.kondition.compiler.backend.util

import org.jetbrains.kotlin.ir.builders.IrBuilderWithScope
import org.jetbrains.kotlin.ir.builders.irByte
import org.jetbrains.kotlin.ir.builders.irInt
import org.jetbrains.kotlin.ir.builders.irLong
import org.jetbrains.kotlin.ir.builders.irShort
import org.jetbrains.kotlin.ir.expressions.IrExpression
import org.jetbrains.kotlin.ir.expressions.impl.IrConstImpl
import org.jetbrains.kotlin.javac.resolve.classId
import org.jetbrains.kotlin.name.ClassId

fun IrBuilderWithScope.irFloat(value: Float) = IrConstImpl.float(startOffset, endOffset, context.irBuiltIns.floatType, value)
fun IrBuilderWithScope.irDouble(value: Double) = IrConstImpl.double(startOffset, endOffset, context.irBuiltIns.doubleType, value)

fun IrBuilderWithScope.irZero(numberClassId: ClassId): IrExpression = when (numberClassId) {
    classId("kotlin", "Int") -> irInt(0)
    classId("kotlin", "Long") -> irLong(0)
    classId("kotlin", "Double") -> irDouble(0.0)
    classId("kotlin", "Short") -> irShort(0)
    classId("kotlin", "Byte") -> irByte(0)
    classId("kotlin", "Float") -> irFloat(0.0f)
    else -> error("Unexpected type")
}
