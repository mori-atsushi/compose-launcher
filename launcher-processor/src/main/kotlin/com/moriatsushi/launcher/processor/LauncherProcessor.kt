package com.moriatsushi.launcher.processor

import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.Dependencies
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.symbol.KSAnnotated

internal class LauncherProcessor(
    private val codeGenerator: CodeGenerator,
    private val logger: KSPLogger,
) : SymbolProcessor {
    private var isGenerated: Boolean = false

    override fun process(resolver: Resolver): List<KSAnnotated> {
        if (isGenerated) return emptyList()

        val code = """
            package com.moriatsushi.launcher

            import androidx.activity.ComponentActivity

            class ComposeActivity : ComponentActivity()
        """.trimIndent()

        codeGenerator.createNewFile(
            dependencies = Dependencies(false),
            packageName = "com.moriatsushi.launcher",
            fileName = "ComposeActivity",
        ).use { outputStream ->
            outputStream.write(code.toByteArray())
        }

        isGenerated = true
        return emptyList()
    }
}
