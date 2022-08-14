package com.moriatsushi.launcher.processor

import com.google.devtools.ksp.symbol.KSFunctionDeclaration

internal data class FunctionNames(
    val qualifiedName: String,
    val simpleName: String,
    val packageName: String,
) {
    companion object {
        fun of(
            functionDeclaration: KSFunctionDeclaration,
        ): FunctionNames {
            return FunctionNames(
                qualifiedName = functionDeclaration.qualifiedName?.asString()
                    ?: error("require qualifiedName"),
                simpleName = functionDeclaration.simpleName.asString(),
                packageName = functionDeclaration.packageName.asString(),
            )
        }
    }
}
