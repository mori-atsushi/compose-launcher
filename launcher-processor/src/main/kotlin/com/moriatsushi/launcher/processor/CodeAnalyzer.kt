package com.moriatsushi.launcher.processor

import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSFunctionDeclaration

internal class CodeAnalyzer {
    fun getTargetFunction(
        symbols: Sequence<KSAnnotated>,
    ): KSFunctionDeclaration? {
        val iterator = symbols
            .filterIsInstance<KSFunctionDeclaration>()
            .iterator()
        if (!iterator.hasNext()) {
            return null
        }
        val target = iterator.next()
        if (iterator.hasNext()) {
            throw IllegalArgumentException(
                """
                Multiple entries is not supported
                Issue: https://github.com/Mori-Atsushi/compose-launcher/issues/1
                """.trimIndent(),
            )
        }
        return target
    }
}
