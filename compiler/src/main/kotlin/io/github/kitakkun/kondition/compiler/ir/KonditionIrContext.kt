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

    // int annotations
    val rangedIntAnnotation by lazy { getKonditionIrClassSymbol("RangedInt", "core.annotation") }

    val greaterThan by lazy { getKonditionNamedFunction("greaterThan", "core.internal") }
    val lessThan by lazy { getKonditionNamedFunction("lessThan", "core.internal") }
    val greaterThanOrEquals by lazy { getKonditionNamedFunction("greaterThanOrEquals", "core.internal") }
    val lessThanOrEquals by lazy { getKonditionNamedFunction("lessThanOrEquals", "core.internal") }

    @OptIn(UnsafeDuringIrConstructionAPI::class)
    val intCompareToFunction by lazy { getIrClassSymbol("kotlin", "Int").getSimpleFunction("compareTo")!! }

    @OptIn(UnsafeDuringIrConstructionAPI::class)
    val booleanAndFunction by lazy { getIrClassSymbol("kotlin", "Boolean").getSimpleFunction("and")!! }

    // string annotations
    val nonBlankAnnotation by lazy { getKonditionIrClassSymbol("NonBlank", "core.annotation") }
    val isNotEmptyFunction by lazy { namedFunction("kotlin.text", "isNotEmpty") }
    val nonEmptyAnnotation by lazy { getKonditionIrClassSymbol("NonEmpty", "core.annotation") }
    val isNotBlankFunction by lazy { namedFunction("kotlin.text", "isNotBlank") }
    val matchRegexAnnotation by lazy { getKonditionIrClassSymbol("MatchRegex", "core.annotation") }
    val matchRegexFunction by lazy { getKonditionNamedFunction("matchRegex", "core.internal") }

    fun namedFunction(packageName: String, name: String, filter: (IrSimpleFunctionSymbol) -> Boolean = { true }): IrSimpleFunctionSymbol = pluginContext.referenceFunctions(CallableId(FqName(packageName), Name.identifier(name))).filter(filter).first()

    fun getKonditionNamedFunction(name: String, subpackage: String? = null): IrFunctionSymbol {
        val suffix = subpackage?.let { ".$subpackage" } ?: ""
        return namedFunction("io.github.kitakkun.kondition$suffix", name)
    }

    fun getKonditionIrClassSymbol(name: String, subpackage: String? = null): IrClassSymbol {
        val suffix = subpackage?.let { ".$subpackage" } ?: ""
        return getIrClassSymbol("io.github.kitakkun.kondition$suffix", name)
    }

    fun getIrClassSymbol(packageName: String, name: String): IrClassSymbol = pluginContext.referenceClass(classId(packageName, name)) ?: error("Unable to find symbol. Package: $packageName, Name: $name")
}
