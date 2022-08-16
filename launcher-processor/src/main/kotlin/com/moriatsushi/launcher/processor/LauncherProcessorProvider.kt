package com.moriatsushi.launcher.processor

import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.processing.SymbolProcessorProvider

class LauncherProcessorProvider : SymbolProcessorProvider {
    override fun create(environment: SymbolProcessorEnvironment): SymbolProcessor {
        return LauncherProcessor(
            codeGenerator = environment.codeGenerator,
            codeBuilder = CodeBuilder(),
            manifestBuilder = ManifestBuilder(environment),
            codeAnalyzer = CodeAnalyzer(),
            logger = environment.logger,
        )
    }
}
