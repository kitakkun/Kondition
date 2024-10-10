package io.github.kitakkun.kondition.compiler.ir.requirement

import io.github.kitakkun.kondition.compiler.ir.KonditionIrContext
import io.github.kitakkun.kondition.compiler.ir.util.irZero
import io.github.kitakkun.kondition.core.annotation.NonZero
import org.jetbrains.kotlin.descriptors.runtime.structure.classId
import org.jetbrains.kotlin.ir.builders.IrBuilderWithScope
import org.jetbrains.kotlin.ir.builders.irGet
import org.jetbrains.kotlin.ir.builders.irNotEquals
import org.jetbrains.kotlin.ir.builders.irString
import org.jetbrains.kotlin.ir.declarations.IrFunction
import org.jetbrains.kotlin.ir.declarations.IrValueDeclaration
import org.jetbrains.kotlin.ir.expressions.IrConstructorCall
import org.jetbrains.kotlin.ir.expressions.IrExpression
import org.jetbrains.kotlin.ir.symbols.UnsafeDuringIrConstructionAPI
import org.jetbrains.kotlin.ir.types.classOrFail
import org.jetbrains.kotlin.ir.util.classId
import org.jetbrains.kotlin.name.ClassId

class NonZeroRequirementProvider : RequirementProvider {
    override val annotationClassId: ClassId = NonZero::class.java.classId

    override fun IrBuilderWithScope.produceErrorMessage(irContext: KonditionIrContext, parentDeclaration: IrFunction, value: IrValueDeclaration, annotation: IrConstructorCall): IrExpression? = irString("${value.name} in ${parentDeclaration.name} can't be zero.")

    @OptIn(UnsafeDuringIrConstructionAPI::class)
    override fun IrBuilderWithScope.produceRequiredCondition(irContext: KonditionIrContext, parentDeclaration: IrFunction, value: IrValueDeclaration, annotation: IrConstructorCall): IrExpression = irNotEquals(
        irGet(value),
        irZero(value.type.classOrFail.owner.classId!!),
    )
}
