package io.github.kitakkun.kondition.compiler.backend.requirement

import io.github.kitakkun.kondition.compiler.common.KonditionConsts
import io.github.kitakkun.kondition.compiler.backend.KonditionIrContext
import io.github.kitakkun.kondition.compiler.backend.util.irZero
import org.jetbrains.kotlin.ir.builders.IrBuilderWithScope
import org.jetbrains.kotlin.ir.builders.irCall
import org.jetbrains.kotlin.ir.builders.irGet
import org.jetbrains.kotlin.ir.builders.irString
import org.jetbrains.kotlin.ir.declarations.IrFunction
import org.jetbrains.kotlin.ir.declarations.IrValueDeclaration
import org.jetbrains.kotlin.ir.expressions.IrConstructorCall
import org.jetbrains.kotlin.ir.expressions.IrExpression
import org.jetbrains.kotlin.ir.symbols.UnsafeDuringIrConstructionAPI
import org.jetbrains.kotlin.ir.types.classOrFail
import org.jetbrains.kotlin.ir.util.classId
import org.jetbrains.kotlin.name.ClassId

class NonNegativeRequirementProvider : RequirementProvider {
    override val annotationClassId: ClassId = KonditionConsts.NonNegativeClassId

    override fun IrBuilderWithScope.produceErrorMessage(
        irContext: KonditionIrContext,
        parentDeclaration: IrFunction,
        value: IrValueDeclaration,
        annotation: IrConstructorCall,
    ): IrExpression? = irString("${value.name} in ${parentDeclaration.name} must be greater than or equals to 0.")

    @OptIn(UnsafeDuringIrConstructionAPI::class)
    override fun IrBuilderWithScope.produceRequiredCondition(
        irContext: KonditionIrContext,
        parentDeclaration: IrFunction,
        value: IrValueDeclaration,
        annotation: IrConstructorCall,
    ): IrExpression = irCall(irContext.greaterThanOrEquals).apply {
        extensionReceiver = irGet(value)
        putValueArgument(0, irZero(value.type.classOrFail.owner.classId!!))
    }
}
