package com.kitakkun.kondition.compiler.k2

import com.kitakkun.kondition.compiler.common.KonditionConsts
import org.jetbrains.kotlin.fir.extensions.predicate.LookupPredicate

val KonditionPredicate = LookupPredicate.create {
    annotated(KonditionConsts.RequirementKonditionClassId.asSingleFqName()) or annotated(KonditionConsts.FittingKonditionClassId.asSingleFqName())
}

val RequirementKonditionPredicate = LookupPredicate.create {
    annotated(KonditionConsts.RequirementKonditionClassId.asSingleFqName())
}

val FittingKonditionPredicate = LookupPredicate.create {
    annotated(KonditionConsts.FittingKonditionClassId.asSingleFqName())
}
