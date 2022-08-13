package com.moriatsushi.launcher.processor

import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.symbol.KSAnnotated

internal class LauncherProcessor : SymbolProcessor {
    override fun process(resolver: Resolver): List<KSAnnotated> {
        // TODO: implement
        return emptyList()
    }
}
