package com.kitakkun.kondition.compiler.backend

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

    val lengthEquals by lazy { getKonditionNamedFunction("lengthEquals", "core.internal") }
    val lengthGreaterThanOrEquals by lazy { getKonditionNamedFunction("lengthGreaterThanOrEquals", "core.internal") }
    val lengthLessThanOrEquals by lazy { getKonditionNamedFunction("lengthLessThanOrEquals", "core.internal") }

    @OptIn(UnsafeDuringIrConstructionAPI::class)
    val coerceAtLeast by lazy { namedFunction("kotlin.ranges", "coerceAtLeast") { it.owner.typeParameters.isNotEmpty() } }

    @OptIn(UnsafeDuringIrConstructionAPI::class)
    val coerceAtMost by lazy { namedFunction("kotlin.ranges", "coerceAtMost") { it.owner.typeParameters.isNotEmpty() } }

    @OptIn(UnsafeDuringIrConstructionAPI::class)
    val coerceIn by lazy { namedFunction("kotlin.ranges", "coerceIn") { it.owner.typeParameters.isNotEmpty() } }

    val removePrefix by lazy { namedFunction("kotlin.text", "removePrefix") }
    val removeSuffix by lazy { namedFunction("kotlin.text", "removeSuffix") }

    @OptIn(UnsafeDuringIrConstructionAPI::class)
    val trim by lazy { namedFunction("kotlin.text", "trim") { it.owner.valueParameters.isEmpty() } }

    @OptIn(UnsafeDuringIrConstructionAPI::class)
    val trimStart by lazy { namedFunction("kotlin.text", "trimStart") { it.owner.valueParameters.isEmpty() } }

    @OptIn(UnsafeDuringIrConstructionAPI::class)
    val trimEnd by lazy { namedFunction("kotlin.text", "trimEnd") { it.owner.valueParameters.isEmpty() } }

    val take by lazy { namedFunction("kotlin.text", "take") }
    val takeLast by lazy { namedFunction("kotlin.text", "takeLast") }
    val drop by lazy { namedFunction("kotlin.text", "drop") }
    val dropLast by lazy { namedFunction("kotlin.text", "dropLast") }

    @OptIn(UnsafeDuringIrConstructionAPI::class)
    val uppercaseString by lazy {
        namedFunction("kotlin.text", "uppercase") {
            it.owner.extensionReceiverParameter?.type == irBuiltIns.stringType && it.owner.valueParameters.isEmpty()
        }
    }

    @OptIn(UnsafeDuringIrConstructionAPI::class)
    val uppercaseChar by lazy {
        namedFunction("kotlin.text", "uppercaseChar") {
            it.owner.extensionReceiverParameter?.type == irBuiltIns.charType && it.owner.valueParameters.isEmpty()
        }
    }

    @OptIn(UnsafeDuringIrConstructionAPI::class)
    val lowercaseString by lazy {
        namedFunction("kotlin.text", "lowercase") {
            it.owner.extensionReceiverParameter?.type == irBuiltIns.stringType && it.owner.valueParameters.isEmpty()
        }
    }

    @OptIn(UnsafeDuringIrConstructionAPI::class)
    val lowercaseChar by lazy {
        namedFunction("kotlin.text", "lowercaseChar") {
            it.owner.extensionReceiverParameter?.type == irBuiltIns.charType && it.owner.valueParameters.isEmpty()
        }
    }

    @OptIn(UnsafeDuringIrConstructionAPI::class)
    val startsWith by lazy {
        namedFunction("kotlin.text", "startsWith") {
            it.owner.extensionReceiverParameter?.type == irBuiltIns.stringType && it.owner.valueParameters.size == 2
        }
    }

    @OptIn(UnsafeDuringIrConstructionAPI::class)
    val endsWith by lazy {
        namedFunction("kotlin.text", "endsWith") {
            it.owner.extensionReceiverParameter?.type == irBuiltIns.stringType && it.owner.valueParameters.size == 2
        }
    }

    fun namedFunction(
        packageName: String,
        name: String,
        filter: (IrSimpleFunctionSymbol) -> Boolean = { true },
    ): IrSimpleFunctionSymbol {
        val callableId = CallableId(FqName(packageName), Name.identifier(name))
        return pluginContext.referenceFunctions(callableId).first(filter)
    }

    fun getKonditionNamedFunction(name: String, subpackage: String? = null): IrFunctionSymbol {
        val suffix = subpackage?.let { ".$subpackage" } ?: ""
        return namedFunction("com.kitakkun.kondition$suffix", name)
    }

    fun getKonditionIrClassSymbol(name: String, subpackage: String? = null): IrClassSymbol {
        val suffix = subpackage?.let { ".$subpackage" } ?: ""
        return getIrClassSymbol("com.kitakkun.kondition$suffix", name)
    }

    fun getIrClassSymbol(packageName: String, name: String): IrClassSymbol = pluginContext.referenceClass(classId(packageName, name))
        ?: error("Unable to find symbol. Package: $packageName, Name: $name")
}
