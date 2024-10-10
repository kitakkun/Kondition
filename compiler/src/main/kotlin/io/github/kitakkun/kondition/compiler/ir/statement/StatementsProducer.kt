package io.github.kitakkun.kondition.compiler.ir.statement

import io.github.kitakkun.kondition.compiler.ir.KonditionIrContext
import io.github.kitakkun.kondition.compiler.ir.requirement.RequirementProvider
import org.jetbrains.kotlin.ir.IrStatement
import org.jetbrains.kotlin.ir.builders.IrBuilderWithScope
import org.jetbrains.kotlin.ir.builders.irCall
import org.jetbrains.kotlin.ir.declarations.IrFunction
import org.jetbrains.kotlin.ir.declarations.IrValueDeclaration
import org.jetbrains.kotlin.ir.declarations.IrValueParameter
import org.jetbrains.kotlin.ir.declarations.IrVariable
import org.jetbrains.kotlin.ir.symbols.UnsafeDuringIrConstructionAPI
import org.jetbrains.kotlin.ir.types.classOrNull
import org.jetbrains.kotlin.ir.util.classId

class StatementsProducer(
    private val requirementProviders: List<RequirementProvider>,
) {
    @OptIn(UnsafeDuringIrConstructionAPI::class)
    fun IrBuilderWithScope.produce(
        irContext: KonditionIrContext,
        parentDeclaration: IrFunction,
        value: IrValueDeclaration,
    ): List<IrStatement> {
        val requirementCheckFunction = when (value) {
            is IrVariable -> irContext.checkFunction
            is IrValueParameter -> irContext.requireFunction
            else -> error("Unexpected type: $value")
        }

        return requirementProviders.mapNotNull { requirement ->
            val annotation = value.annotations.firstOrNull {
                it.type.classOrNull?.owner?.classId == requirement.annotationClassId
            } ?: return@mapNotNull null
            val condition = with(requirement) {
                produceRequiredCondition(
                    irContext = irContext,
                    parentDeclaration = parentDeclaration,
                    value = value,
                    annotation = annotation,
                )
            }
            val message = with(requirement) {
                produceErrorMessage(
                    irContext = irContext,
                    parentDeclaration = parentDeclaration,
                    value = value,
                    annotation = annotation,
                )
            }
            irCall(requirementCheckFunction).apply {
                putValueArgument(0, condition)
                putValueArgument(1, message)
            }
        }
    }
}
