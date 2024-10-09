package io.github.kitakkun.kondition.compiler.ir.transformer

import io.github.kitakkun.kondition.compiler.ir.KonditionIrContext
import io.github.kitakkun.kondition.compiler.ir.requirement.RequirementProvider
import org.jetbrains.kotlin.backend.common.lower.createIrBuilder
import org.jetbrains.kotlin.backend.common.lower.irBlockBody
import org.jetbrains.kotlin.ir.IrStatement
import org.jetbrains.kotlin.ir.builders.irCall
import org.jetbrains.kotlin.ir.declarations.IrFunction
import org.jetbrains.kotlin.ir.expressions.IrBlockBody
import org.jetbrains.kotlin.ir.expressions.IrExpressionBody
import org.jetbrains.kotlin.ir.symbols.UnsafeDuringIrConstructionAPI
import org.jetbrains.kotlin.ir.types.classOrNull
import org.jetbrains.kotlin.ir.util.classId
import org.jetbrains.kotlin.ir.util.hasAnnotation
import org.jetbrains.kotlin.ir.visitors.IrElementTransformerVoid

class ValueParameterCheckStatementsProducer(
    private val irContext: KonditionIrContext,
    private val requirements: List<RequirementProvider>,
) : IrElementTransformerVoid() {
    @OptIn(UnsafeDuringIrConstructionAPI::class)
    override fun visitFunction(declaration: IrFunction): IrStatement {
        val parameterRequirementsMap = declaration.valueParameters.associateWith { parameter ->
            requirements.filter { requirement -> parameter.hasAnnotation(requirement.annotationClassId) }
        }
        if (parameterRequirementsMap.values.none { it.isNotEmpty() }) return super.visitFunction(declaration)

        val irBuilder = irContext.irBuiltIns.createIrBuilder(declaration.symbol)
        val statementsToAdd = parameterRequirementsMap.flatMap { (valueParameter, requirements) ->
            requirements.map { requirement ->
                val annotation = valueParameter.annotations.first {
                    it.type.classOrNull?.owner?.classId == requirement.annotationClassId
                }
                val condition = with(requirement) {
                    irBuilder.produceRequiredCondition(
                        irContext = irContext,
                        parentDeclaration = declaration,
                        valueParameter = valueParameter,
                        annotation = annotation,
                    )
                }
                val message = with(requirement) {
                    irBuilder.produceErrorMessage(
                        irContext = irContext,
                        parentDeclaration = declaration,
                        valueParameter = valueParameter,
                        annotation = annotation,
                    )
                }
                irBuilder.irCall(irContext.requireFunction).apply {
                    putValueArgument(0, condition)
                    putValueArgument(1, message)
                }
            }
        }

        val body = declaration.body
        when (body) {
            is IrBlockBody -> body.statements.addAll(0, statementsToAdd)
            is IrExpressionBody -> irBuilder.irBlockBody(body.expression) {
                +body.expression
                +statementsToAdd
            }
        }
        declaration.body = body

        return super.visitFunction(declaration)
    }
}
