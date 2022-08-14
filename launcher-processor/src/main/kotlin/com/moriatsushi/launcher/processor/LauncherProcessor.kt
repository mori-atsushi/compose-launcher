package com.moriatsushi.launcher.processor

import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.Dependencies
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.symbol.KSAnnotated
import com.moriatsushi.launcher.Entry

internal class LauncherProcessor(
    private val codeGenerator: CodeGenerator,
    private val codeBuilder: CodeBuilder,
    private val codeAnalyzer: CodeAnalyzer,
    private val logger: KSPLogger,
) : SymbolProcessor {
    companion object {
        private val EntryAnnotationName = Entry::class.qualifiedName
            ?: error("require qualifiedName of Entry")
        private const val EntryPackageName = "com.moriatsushi.launcher"
        private const val EntryFileName = "ComposeActivity"
    }

    private var isGenerated: Boolean = false

    override fun process(resolver: Resolver): List<KSAnnotated> {
        if (isGenerated) return emptyList()
        try {
            generate(resolver)
        } catch (e: Throwable) {
            logger.error(e.message ?: "unknown error")
        }
        isGenerated = true
        return emptyList()
    }

    private fun generate(resolver: Resolver) {
        val symbols = resolver.getSymbolsWithAnnotation(
            annotationName = EntryAnnotationName,
        )
        val function = codeAnalyzer.getTargetFunction(symbols) ?: return
        val functionName = function.qualifiedName?.asString() ?: return
        val file = function.containingFile ?: return
        val code = codeBuilder.build(functionName)

        codeGenerator.createNewFile(
            dependencies = Dependencies(true, file),
            packageName = EntryPackageName,
            fileName = EntryFileName,
        ).use { outputStream ->
            outputStream.write(code.toByteArray())
        }
    }
}
