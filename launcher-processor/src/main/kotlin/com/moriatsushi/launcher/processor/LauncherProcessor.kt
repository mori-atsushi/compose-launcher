package com.moriatsushi.launcher.processor

import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.Dependencies
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSFunctionDeclaration
import org.intellij.lang.annotations.Language

internal class LauncherProcessor(
    private val codeGenerator: CodeGenerator,
    private val logger: KSPLogger,
) : SymbolProcessor {
    private var isGenerated: Boolean = false

    override fun process(resolver: Resolver): List<KSAnnotated> {
        if (isGenerated) return emptyList()
        generate(resolver)
        isGenerated = true
        return emptyList()
    }

    private fun generate(resolver: Resolver) {
        val symbol = getTargetSymbol(resolver) ?: return
        val functionName = symbol.qualifiedName?.asString() ?: return
        val file = symbol.containingFile ?: return
        val code = createCode(functionName)

        codeGenerator.createNewFile(
            dependencies = Dependencies(true, file),
            packageName = "com.moriatsushi.launcher",
            fileName = "ComposeActivity",
        ).use { outputStream ->
            outputStream.write(code.toByteArray())
        }
    }

    private fun getTargetSymbol(resolver: Resolver): KSFunctionDeclaration? {
        val iterator = resolver
            .getSymbolsWithAnnotation(
                annotationName = "com.moriatsushi.launcher.Entry",
            )
            .filterIsInstance<KSFunctionDeclaration>()
            .iterator()
        if (!iterator.hasNext()) {
            return null
        }
        val target = iterator.next()
        if (iterator.hasNext()) {
            logger.error(
                """
                Multiple entries is not supported
                Issue: https://github.com/Mori-Atsushi/compose-launcher/issues/1
                """.trimIndent(),
            )
        }
        return target
    }

    @Language("kotlin")
    private fun createCode(functionName: String): String {
        return """
            // generated!

            package com.moriatsushi.launcher

            import android.os.Bundle
            import androidx.activity.ComponentActivity
            import androidx.activity.compose.setContent

            class ComposeActivity : ComponentActivity() {
                override fun onCreate(savedInstanceState: Bundle?) {
                    super.onCreate(savedInstanceState)

                    setContent {
                        $functionName()
                    }
                }
            }
        """.trimIndent()
    }
}
