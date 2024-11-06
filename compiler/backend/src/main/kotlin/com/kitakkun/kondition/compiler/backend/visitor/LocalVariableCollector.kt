package com.kitakkun.kondition.compiler.backend.visitor

import org.jetbrains.kotlin.ir.IrElement
import org.jetbrains.kotlin.ir.declarations.IrFunction
import org.jetbrains.kotlin.ir.declarations.IrVariable
import org.jetbrains.kotlin.ir.util.isLocal
import org.jetbrains.kotlin.ir.visitors.IrElementVisitorVoid
import org.jetbrains.kotlin.ir.visitors.acceptChildrenVoid

class LocalVariableCollector {
    fun collectFunctionLocalVariables(function: IrFunction): List<IrVariable> {
        val variables = mutableListOf<IrVariable>()
        function.acceptChildrenVoid(object : IrElementVisitorVoid {
            override fun visitElement(element: IrElement) {
                element.acceptChildrenVoid(this)
            }

            override fun visitVariable(declaration: IrVariable) {
                super.visitVariable(declaration)

                if (declaration.isLocal && declaration.parent == function) {
                    variables += declaration
                }
            }
        })
        return variables
    }
}
