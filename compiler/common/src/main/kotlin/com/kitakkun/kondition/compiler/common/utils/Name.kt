package com.kitakkun.kondition.compiler.common.utils

import org.jetbrains.kotlin.name.Name

val String.toName get() = Name.identifier(this)
