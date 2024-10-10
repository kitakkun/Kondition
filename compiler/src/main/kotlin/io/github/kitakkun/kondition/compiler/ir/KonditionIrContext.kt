package io.github.kitakkun.kondition.compiler.ir

import org.jetbrains.kotlin.backend.common.extensions.IrPluginContext
import org.jetbrains.kotlin.cli.common.messages.MessageCollector
import org.jetbrains.kotlin.ir.symbols.IrClassSymbol
import org.jetbrains.kotlin.ir.symbols.IrFunctionSymbol
import org.jetbrains.kotlin.ir.symbols.IrSimpleFunctionSymbol
import org.jetbrains.kotlin.ir.symbols.UnsafeDuringIrConstructionAPI
import org.jetbrains.kotlin.ir.util.getSimpleFunction
import org.jetbrains.kotlin.javac.resolve.classId
import org.jetbrains.kotlin.name.CallableId
import org.jetbrains.kotlin.name.FqName
import org.jetbrains.kotlin.name.Name

class KonditionIrContext(
    val pluginContext: IrPluginContext,
    val messageCollector: MessageCollector,
) {
    val irBuiltIns = pluginContext.irBuiltIns
    val irFactory = pluginContext.irFactory

    val requireFunction by lazy { getKonditionNamedFunction("require", "core.internal") }
    val checkFunction by lazy { getKonditionNamedFunction("check", "core.internal") }

    // number utils
    val greaterThan by lazy { getKonditionNamedFunction("greaterThan", "core.internal") }
    val lessThan by lazy { getKonditionNamedFunction("lessThan", "core.internal") }
    val greaterThanOrEquals by lazy { getKonditionNamedFunction("greaterThanOrEquals", "core.internal") }
    val lessThanOrEquals by lazy { getKonditionNamedFunction("lessThanOrEquals", "core.internal") }

    @OptIn(UnsafeDuringIrConstructionAPI::class)
    val booleanAndFunction by lazy { getIrClassSymbol("kotlin", "Boolean").getSimpleFunction("and")!! }

    // string functions
    val isNotEmptyFunction by lazy { namedFunction("kotlin.text", "isNotEmpty") }
    val isNotBlankFunction by lazy { namedFunction("kotlin.text", "isNotBlank") }
    val matchRegexFunction by lazy { getKonditionNamedFunction("matchRegex", "core.internal") }
    val isAlphaFunction by lazy { getKonditionNamedFunction("isAlpha", "core.internal") }
    val isNumericFunction by lazy { getKonditionNamedFunction("isNumeric", "core.internal") }

    fun namedFunction(
        packageName: String,
        name: String,
        filter: (IrSimpleFunctionSymbol) -> Boolean = { true },
    ): IrSimpleFunctionSymbol {
        val callableId = CallableId(FqName(packageName), Name.identifier(name))
        return pluginContext.referenceFunctions(callableId).filter(filter).first()
    }

    fun getKonditionNamedFunction(name: String, subpackage: String? = null): IrFunctionSymbol {
        val suffix = subpackage?.let { ".$subpackage" } ?: ""
        return namedFunction("io.github.kitakkun.kondition$suffix", name)
    }

    fun getKonditionIrClassSymbol(name: String, subpackage: String? = null): IrClassSymbol {
        val suffix = subpackage?.let { ".$subpackage" } ?: ""
        return getIrClassSymbol("io.github.kitakkun.kondition$suffix", name)
    }

    fun getIrClassSymbol(packageName: String, name: String): IrClassSymbol = pluginContext.referenceClass(classId(packageName, name))
        ?: error("Unable to find symbol. Package: $packageName, Name: $name")
}
