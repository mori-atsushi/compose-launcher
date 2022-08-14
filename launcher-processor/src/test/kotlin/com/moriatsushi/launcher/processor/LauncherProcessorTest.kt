package com.moriatsushi.launcher.processor

import com.google.common.truth.Truth.assertThat
import com.tschuchort.compiletesting.KotlinCompilation
import com.tschuchort.compiletesting.KotlinCompilation.ExitCode
import com.tschuchort.compiletesting.SourceFile
import com.tschuchort.compiletesting.kspIncremental
import com.tschuchort.compiletesting.kspSourcesDir
import com.tschuchort.compiletesting.symbolProcessorProviders
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import java.io.File

class LauncherProcessorTest {
    @Rule
    @JvmField
    val temporaryFolder = TemporaryFolder()

    @Test
    fun `generate DefaultComposeActivity`() {
        val kotlinSource = SourceFile.kotlin(
            "Test.kt",
            """
                package testPackage

                import com.moriatsushi.launcher.Entry

                @Entry(default = true)
                fun Main() {
                }
            """,
        )
        val complication = createCompilation(kotlinSource)
        val result = complication.compile()
        assertThat(result.exitCode).isEqualTo(ExitCode.OK)

        val generatedFiles = findGeneratedFiles(complication)
        assertThat(generatedFiles).hasSize(1)

        val generatedFile = generatedFiles.first()
        assertThat(generatedFile.name).isEqualTo("DefaultComposeActivity.kt")

        val generatedString = generatedFile.readText()
        assertThat(generatedString).contains("DefaultComposeActivity")
        assertThat(generatedString).contains("testPackage.Main()")
    }

    @Test
    fun `generate ComposeActivity`() {
        val kotlinSource = SourceFile.kotlin(
            "Test.kt",
            """
                package testPackage

                import com.moriatsushi.launcher.Entry

                @Entry(default = false)
                fun Other1() {
                }

                @Entry(default = false)
                fun Other2() {
                }
            """,
        )
        val complication = createCompilation(kotlinSource)
        val result = complication.compile()
        assertThat(result.exitCode).isEqualTo(ExitCode.OK)

        val generatedFiles = findGeneratedFiles(complication)
        assertThat(generatedFiles).hasSize(1)

        val generatedFile = generatedFiles.first()
        assertThat(generatedFile.name).isEqualTo("ComposeActivity.kt")

        val generatedString = generatedFile.readText()
        assertThat(generatedString).contains("ComposeActivity")
        assertThat(generatedString).contains("testPackage.Other1()")
        assertThat(generatedString).contains("testPackage.Other2()")
    }

    @Test
    fun `do not generate if there is no target`() {
        val kotlinSource = SourceFile.kotlin(
            "Test.kt",
            """
                package testPackage

                fun Main() {
                }
            """,
        )
        val complication = createCompilation(kotlinSource)
        val result = complication.compile()
        assertThat(result.exitCode).isEqualTo(ExitCode.OK)

        val generatedFiles = findGeneratedFiles(complication)
        assertThat(generatedFiles).isEmpty()
    }

    @Test
    fun `multiple default entries are not allowed`() {
        val kotlinSource = SourceFile.kotlin(
            "Test.kt",
            """
                package testPackage

                import com.moriatsushi.launcher.Entry

                @Entry(default = true)
                fun Main1() {
                }

                @Entry(default = true)
                fun Main2() {
                }
            """,
        )
        val complication = createCompilation(kotlinSource)
        val result = complication.compile()
        assertThat(result.exitCode).isEqualTo(ExitCode.COMPILATION_ERROR)
        assertThat(result.messages).contains("Multiple default entries are not allowed")
    }

    private fun createCompilation(vararg sourceFiles: SourceFile): KotlinCompilation {
        return KotlinCompilation().apply {
            workingDir = temporaryFolder.root
            sources = sourceFiles.asList()
            inheritClassPath = true
            symbolProcessorProviders = listOf(LauncherProcessorProvider())
            kspIncremental = true
            inheritClassPath = true
        }
    }

    private fun findGeneratedFiles(compilation: KotlinCompilation): List<File> {
        return compilation.kspSourcesDir
            .walkTopDown()
            .filter { it.isFile }
            .toList()
    }
}
