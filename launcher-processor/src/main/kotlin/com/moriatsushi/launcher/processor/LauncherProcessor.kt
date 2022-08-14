package com.moriatsushi.launcher.processor

import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.Dependencies
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSFunctionDeclaration

internal class LauncherProcessor(
    private val codeGenerator: CodeGenerator,
    private val codeBuilder: CodeBuilder,
    private val codeAnalyzer: CodeAnalyzer,
    private val logger: KSPLogger,
) : SymbolProcessor {
    companion object {
        private const val EntryPackageName = "com.moriatsushi.launcher"
        private const val DefaultEntryFileName = "DefaultComposeActivity"
        private const val OtherEntryFileName = "ComposeActivity"
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
        val result = codeAnalyzer.analyze(symbols)

        if (result.default != null) {
            generateDefaultEntry(result.default)
        }

        if (result.others.isNotEmpty()) {
            generateOtherEntries(result.others)
        }
    }

    private fun generateDefaultEntry(
        function: KSFunctionDeclaration,
    ) {
        val functionNames = FunctionNames.of(function)
        val file = function.containingFile ?: return
        val activityCode = codeBuilder.buildDefaultActivity(functionNames)

        codeGenerator.createNewFile(
            dependencies = Dependencies(true, file),
            packageName = EntryPackageName,
            fileName = DefaultEntryFileName,
        ).use { outputStream ->
            outputStream.write(activityCode.toByteArray())
        }

        val launcherCode = codeBuilder.buildLauncher(
            function = functionNames,
            isDefault = true,
        )

        codeGenerator.createNewFile(
            dependencies = Dependencies(false, file),
            packageName = function.packageName.asString(),
            fileName = "${function.simpleName.asString()}Launcher",
        ).use { outputStream ->
            outputStream.write(launcherCode.toByteArray())
        }
    }

    private fun generateOtherEntries(
        functions: List<KSFunctionDeclaration>,
    ) {
        val files = functions.mapNotNull {
            it.containingFile
        }.toTypedArray()
        val activityCode = codeBuilder.buildOtherActivity(
            functions.map { FunctionNames.of(it) },
        )

        codeGenerator.createNewFile(
            dependencies = Dependencies(true, *files),
            packageName = EntryPackageName,
            fileName = OtherEntryFileName,
        ).use { outputStream ->
            outputStream.write(activityCode.toByteArray())
        }

        functions.forEach {
            val functionNames = FunctionNames.of(it)
            val launcherCode = codeBuilder.buildLauncher(
                function = functionNames,
                isDefault = false,
            )
            val file = it.containingFile
                ?: error("require containingFile")

            codeGenerator.createNewFile(
                dependencies = Dependencies(false, file),
                packageName = it.packageName.asString(),
                fileName = "${it.simpleName.asString()}Launcher",
            ).use { outputStream ->
                outputStream.write(launcherCode.toByteArray())
            }
        }
    }
}
