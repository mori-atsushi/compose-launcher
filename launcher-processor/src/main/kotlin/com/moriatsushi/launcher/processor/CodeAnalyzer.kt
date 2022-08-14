package com.moriatsushi.launcher.processor

import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSAnnotation
import com.google.devtools.ksp.symbol.KSFunctionDeclaration

internal class CodeAnalyzer {
    companion object {
        private const val DefaultArgName = "default"
    }

    fun analyze(
        symbols: Sequence<KSAnnotated>,
    ): Result {
        var default: KSFunctionDeclaration? = null
        val others = mutableListOf<KSFunctionDeclaration>()

        symbols
            .filterIsInstance<KSFunctionDeclaration>()
            .forEach {
                if (it.isDefault) {
                    if (default == null) {
                        default = it
                    } else {
                        throw IllegalArgumentException(
                            "Multiple default entries are not allowed",
                        )
                    }
                } else {
                    others.add(it)
                }
            }

        return Result(
            default = default,
            others = others,
        )
    }

    data class Result(
        val default: KSFunctionDeclaration? = null,
        val others: List<KSFunctionDeclaration> = emptyList(),
    )

    private val KSFunctionDeclaration.isDefault: Boolean
        get() {
            val targetAnnotation = annotations.find {
                it.qualifiedName == EntryAnnotationName
            }
            val defaultArg = targetAnnotation?.arguments?.find {
                it.name?.asString() == DefaultArgName
            }
            return defaultArg != null && defaultArg.value as Boolean
        }

    private val KSAnnotation.qualifiedName: String?
        get() = annotationType.resolve().declaration.qualifiedName?.asString()
}
