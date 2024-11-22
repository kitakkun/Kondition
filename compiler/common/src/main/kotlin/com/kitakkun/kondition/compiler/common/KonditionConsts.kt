package com.kitakkun.kondition.compiler.common

import org.jetbrains.kotlin.javac.resolve.classId
import org.jetbrains.kotlin.name.ClassId

object KonditionConsts {
    val AddPrefixClassId by lazy { konditionAnnotationClassId("AddPrefix") }
    val AddSuffixClassId by lazy { konditionAnnotationClassId("AddSuffix") }
    val RemovePrefixClassId by lazy { konditionAnnotationClassId("RemovePrefix") }
    val RemoveSuffixClassId by lazy { konditionAnnotationClassId("RemoveSuffix") }
    val TrimClassId by lazy { konditionAnnotationClassId("Trim") }
    val TrimStartClassId by lazy { konditionAnnotationClassId("TrimStart") }
    val TrimEndClassId by lazy { konditionAnnotationClassId("TrimEnd") }
    val TakeClassId by lazy { konditionAnnotationClassId("Take") }
    val TakeLastClassId by lazy { konditionAnnotationClassId("TakeLast") }
    val DropClassId by lazy { konditionAnnotationClassId("Drop") }
    val DropLastClassId by lazy { konditionAnnotationClassId("DropLast") }
    val ToUpperCaseClassId by lazy { konditionAnnotationClassId("ToUpperCase") }
    val ToLowerCaseClassId by lazy { konditionAnnotationClassId("ToLowerCase") }

    val RangedClassId by lazy { konditionAnnotationClassId("Ranged") }
    val RangedDecimalClassId by lazy { konditionAnnotationClassId("RangedDecimal") }
    val GreaterThanClassId by lazy { konditionAnnotationClassId("GreaterThan") }
    val GreaterThanDecimalClassId by lazy { konditionAnnotationClassId("GreaterThanDecimal") }
    val GreaterThanOrEqualsClassId by lazy { konditionAnnotationClassId("GreaterThanOrEquals") }
    val GreaterThanOrEqualsDecimalClassId by lazy { konditionAnnotationClassId("GreaterThanOrEqualsDecimal") }
    val LessThanClassId by lazy { konditionAnnotationClassId("LessThan") }
    val LessThanDecimalClassId by lazy { konditionAnnotationClassId("LessThanDecimal") }
    val LessThanOrEqualsClassId by lazy { konditionAnnotationClassId("LessThanOrEquals") }
    val LessThanOrEqualsDecimalClassId by lazy { konditionAnnotationClassId("LessThanOrEqualsDecimal") }
    val NegativeClassId by lazy { konditionAnnotationClassId("Negative") }
    val NonNegativeClassId by lazy { konditionAnnotationClassId("NonNegative") }
    val PositiveClassId by lazy { konditionAnnotationClassId("Positive") }
    val NonPositiveClassId by lazy { konditionAnnotationClassId("NonPositive") }
    val NonZeroClassId by lazy { konditionAnnotationClassId("NonZero") }

    val CoerceAtLeastClassId by lazy { konditionAnnotationClassId("CoerceAtLeast") }
    val CoerceAtMostClassId by lazy { konditionAnnotationClassId("CoerceAtMost") }
    val CoerceInClassId by lazy { konditionAnnotationClassId("CoerceIn") }
    val CoerceAtLeastDecimalClassId by lazy { konditionAnnotationClassId("CoerceAtLeastDecimal") }
    val CoerceAtMostDecimalClassId by lazy { konditionAnnotationClassId("CoerceAtMostDecimal") }
    val CoerceInDecimalClassId by lazy { konditionAnnotationClassId("CoerceInDecimal") }

    val NonEmptyClassId by lazy { konditionAnnotationClassId("NonEmpty") }
    val NonBlankClassId by lazy { konditionAnnotationClassId("NonBlank") }
    val LengthClassId by lazy { konditionAnnotationClassId("Length") }
    val MaxLengthClassId by lazy { konditionAnnotationClassId("MaxLength") }
    val MinLengthClassId by lazy { konditionAnnotationClassId("MinLength") }
    val AlphabeticClassId by lazy { konditionAnnotationClassId("Alphabetic") }
    val NumericClassId by lazy { konditionAnnotationClassId("Numeric") }
    val MatchRegexClassId by lazy { konditionAnnotationClassId("MatchRegex") }
    val PrefixedClassId by lazy { konditionAnnotationClassId("Prefixed") }
    val SuffixedClassId by lazy { konditionAnnotationClassId("Suffixed") }
    val UpperCasedClassId by lazy { konditionAnnotationClassId("UpperCased") }
    val LowerCasedClassId by lazy { konditionAnnotationClassId("LowerCased") }

    val AbortWithClassId by lazy { konditionAnnotationClassId("AbortWith") }

    val TypeSpecificClassId by lazy { konditionAnnotationClassId("TypeSpecific") }
    val RequirementKonditionClassId by lazy { konditionAnnotationClassId("RequirementKondition") }
    val FittingKonditionClassId by lazy { konditionAnnotationClassId("FittingKondition") }
    val KonditionClassId by lazy { konditionAnnotationClassId("Kondition") }
    val ValidatorClassId by lazy { konditionAnnotationClassId("Validator") }
    val FitterClassId by lazy { konditionAnnotationClassId("Fitter") }
    val ErrorMessageProviderClassId by lazy { konditionAnnotationClassId("ErrorMessageProvider") }

    fun konditionAnnotationClassId(name: String): ClassId = classId(
        packageName = "com.kitakkun.kondition.core.annotation",
        className = name,
    )
}
