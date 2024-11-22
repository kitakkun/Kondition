package com.kitakkun.kondition.compiler.k2.extensions

import com.kitakkun.kondition.compiler.common.KonditionConsts
import com.kitakkun.kondition.compiler.k2.FittingKonditionPredicate
import com.kitakkun.kondition.compiler.k2.KonditionDeclarationKey
import com.kitakkun.kondition.compiler.k2.KonditionPredicate
import com.kitakkun.kondition.compiler.k2.RequirementKonditionPredicate
import org.jetbrains.kotlin.descriptors.ClassKind
import org.jetbrains.kotlin.descriptors.Modality
import org.jetbrains.kotlin.fir.FirSession
import org.jetbrains.kotlin.fir.declarations.*
import org.jetbrains.kotlin.fir.expressions.FirAnnotation
import org.jetbrains.kotlin.fir.expressions.FirArrayLiteral
import org.jetbrains.kotlin.fir.expressions.FirGetClassCall
import org.jetbrains.kotlin.fir.expressions.arguments
import org.jetbrains.kotlin.fir.extensions.FirDeclarationGenerationExtension
import org.jetbrains.kotlin.fir.extensions.FirDeclarationPredicateRegistrar
import org.jetbrains.kotlin.fir.extensions.MemberGenerationContext
import org.jetbrains.kotlin.fir.extensions.predicateBasedProvider
import org.jetbrains.kotlin.fir.packageFqName
import org.jetbrains.kotlin.fir.plugin.createMemberFunction
import org.jetbrains.kotlin.fir.plugin.createTopLevelClass
import org.jetbrains.kotlin.fir.resolve.providers.firProvider
import org.jetbrains.kotlin.fir.symbols.impl.FirClassLikeSymbol
import org.jetbrains.kotlin.fir.symbols.impl.FirClassSymbol
import org.jetbrains.kotlin.fir.symbols.impl.FirConstructorSymbol
import org.jetbrains.kotlin.fir.symbols.impl.FirNamedFunctionSymbol
import org.jetbrains.kotlin.fir.types.ConeKotlinType
import org.jetbrains.kotlin.fir.types.coneType
import org.jetbrains.kotlin.fir.types.typeContext
import org.jetbrains.kotlin.javac.resolve.classId
import org.jetbrains.kotlin.name.CallableId
import org.jetbrains.kotlin.name.ClassId
import org.jetbrains.kotlin.name.Name

class KonditionInterfaceGenerationExtension(session: FirSession) : FirDeclarationGenerationExtension(session) {
    companion object {
        val ValidateFunctionName = Name.identifier("validate")
        val ProduceErrorMessageFunctionName = Name.identifier("produceErrorMessage")
        val FitFunctionName = Name.identifier("fit")
    }

    enum class KonditionType {
        Requirement,
        Fitting,
    }

    override fun getTopLevelClassIds(): Set<ClassId> = session.predicateBasedProvider
        .getSymbolsByPredicate(KonditionPredicate)
        .filterIsInstance<FirClassSymbol<*>>()
        .map { classId(it.packageFqName().asString(), it.name.asString() + "Kondition") }
        .toSet()

    override fun getCallableNamesForClass(classSymbol: FirClassSymbol<*>, context: MemberGenerationContext): Set<Name> {
        val origin = classSymbol.origin as? FirDeclarationOrigin.Plugin ?: return emptySet()
        if (origin.key != KonditionDeclarationKey) return emptySet()

        val annotationClassId =
            classId(classSymbol.classId.packageFqName.asString(), classSymbol.name.asString().removeSuffix("Kondition"))
        val annotationClass = session.firProvider.getFirClassifierByFqName(annotationClassId) as? FirRegularClass ?: return emptySet()

        val type = when {
            session.predicateBasedProvider.matches(RequirementKonditionPredicate, annotationClass) -> KonditionType.Requirement
            session.predicateBasedProvider.matches(FittingKonditionPredicate, annotationClass) -> KonditionType.Fitting
            else -> return emptySet()
        }

        return when (type) {
            KonditionType.Requirement -> setOf(ValidateFunctionName, ProduceErrorMessageFunctionName)
            KonditionType.Fitting -> setOf(FitFunctionName)
        }
    }

    override fun generateTopLevelClassLikeDeclaration(classId: ClassId): FirClassLikeSymbol<*> {
        return createTopLevelClass(
            classId = classId,
            key = KonditionDeclarationKey,
            classKind = ClassKind.INTERFACE,
            config = { modality = Modality.ABSTRACT }
        ).symbol
    }

    override fun generateFunctions(callableId: CallableId, context: MemberGenerationContext?): List<FirNamedFunctionSymbol> {
        val ownerClassSymbol = context?.owner ?: return emptyList()
        val annotationClassId = classId(
            ownerClassSymbol.classId.packageFqName.asString(),
            ownerClassSymbol.name.asString().removeSuffix("Kondition")
        )
        val annotationClass = session.firProvider.getFirClassifierByFqName(annotationClassId) as? FirRegularClass ?: return emptyList()
        val annotationConstructor = annotationClass.primaryConstructorIfAny(session) ?: return emptyList()

        val type = when {
            session.predicateBasedProvider.matches(RequirementKonditionPredicate, annotationClass) -> KonditionType.Requirement
            session.predicateBasedProvider.matches(FittingKonditionPredicate, annotationClass) -> KonditionType.Fitting
            else -> return emptyList()
        }

        val function = when (callableId.callableName) {
            FitFunctionName -> createFitFunction(ownerClassSymbol)
            ValidateFunctionName -> createValidateFunction(ownerClassSymbol, annotationClass, annotationConstructor)
            ProduceErrorMessageFunctionName -> createProduceErrorMessageFunction(ownerClassSymbol, annotationClass, annotationConstructor)
            else -> null
        }

        return listOfNotNull(function?.symbol)

        return listOf(
            createMemberFunction(
                owner = ownerClassSymbol,
                key = KonditionDeclarationKey,
                name = callableId.callableName,
                returnType = session.builtinTypes.unitType.coneType,
                config = {
                    modality = Modality.ABSTRACT
                    annotationConstructor.valueParameterSymbols.forEach { valueParameterSymbol ->
                        valueParameter(valueParameterSymbol.name, valueParameterSymbol.resolvedReturnType)
                    }
                }
            ).symbol
        )
    }

    private fun createFitFunction(
        ownerClassSymbol: FirClassSymbol<*>,
    ): FirSimpleFunction {
        return createMemberFunction(
            owner = ownerClassSymbol,
            key = KonditionDeclarationKey,
            name = FitFunctionName,
            returnType = session.builtinTypes.unitType.coneType,
            config = {
                modality = Modality.ABSTRACT
            }
        )
    }

    private fun createValidateFunction(
        ownerClassSymbol: FirClassSymbol<*>,
        annotationClass: FirRegularClass,
        annotationConstructor: FirConstructorSymbol,
    ): FirSimpleFunction {
        val targetClasses = annotationClass.getAnnotationByClassId(KonditionConsts.TypeSpecificClassId, session)?.getKClassArrayArgument(
            Name.identifier("classes")
        )
        val type = targetClasses?.let { session.typeContext.intersectTypes(it) } ?: session.builtinTypes.anyType.coneType
        return createMemberFunction(
            owner = ownerClassSymbol,
            key = KonditionDeclarationKey,
            name = ValidateFunctionName,
            returnType = session.builtinTypes.booleanType.coneType,
            config = {
                modality = Modality.ABSTRACT
                valueParameter(Name.identifier("value"), type)
                annotationConstructor.valueParameterSymbols.forEach { valueParameterSymbol ->
                    valueParameter(valueParameterSymbol.name, valueParameterSymbol.resolvedReturnType)
                }
            }
        )
    }

    private fun FirAnnotation.getKClassArrayArgument(name: Name): List<ConeKotlinType> {
        val argument = findArgumentByName(name) ?: return emptyList()
        val arrayLiteral = argument.evaluateAs<FirArrayLiteral>(session) ?: return emptyList()
        return arrayLiteral.arguments.mapNotNull { (it as? FirGetClassCall)?.getTargetType() }
    }

    private fun createProduceErrorMessageFunction(
        ownerClassSymbol: FirClassSymbol<*>,
        annotationClass: FirRegularClass,
        annotationConstructor: FirConstructorSymbol,
    ): FirSimpleFunction {
        val targetClasses = annotationClass.getAnnotationByClassId(KonditionConsts.TypeSpecificClassId, session)?.getKClassArrayArgument(
            Name.identifier("classes")
        )
        val type = targetClasses?.let { session.typeContext.intersectTypes(it) } ?: session.builtinTypes.anyType.coneType
        return createMemberFunction(
            owner = ownerClassSymbol,
            key = KonditionDeclarationKey,
            name = ProduceErrorMessageFunctionName,
            returnType = session.builtinTypes.stringType.coneType,
            config = {
                modality = Modality.ABSTRACT
                valueParameter(Name.identifier("value"), type)
                annotationConstructor.valueParameterSymbols.forEach { valueParameterSymbol ->
                    valueParameter(valueParameterSymbol.name, valueParameterSymbol.resolvedReturnType)
                }
            }
        )
    }

    override fun FirDeclarationPredicateRegistrar.registerPredicates() {
        register(KonditionPredicate, RequirementKonditionPredicate, FittingKonditionPredicate)
    }
}
