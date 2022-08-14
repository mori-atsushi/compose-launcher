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
        assertThat(generatedFiles).hasSize(2)

        val activityFile = generatedFiles.find {
            it.name == "DefaultComposeActivity.kt"
        }
        assertThat(activityFile).isNotNull()

        val activityCode = activityFile!!.readText()
        assertThat(activityCode).contains("\npackage com.moriatsushi.launcher\n")
        assertThat(activityCode).contains("class DefaultComposeActivity")
        assertThat(activityCode).contains("testPackage.Main()")
    }

    @Test
    fun `generate DefaultLauncher`() {
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

        val launcherFile = generatedFiles.find {
            it.name == "MainLauncher.kt"
        }
        assertThat(launcherFile).isNotNull()

        val launcherCode = launcherFile!!.readText()
        assertThat(launcherCode).contains("\npackage testPackage\n")
        assertThat(launcherCode).contains("@Composable\nfun rememberMainLauncher(): MainLauncher")
        assertThat(launcherCode).contains("fun getMainLauncher(context: Context): MainLauncher")
        assertThat(launcherCode).contains("interface MainLauncher")
        assertThat(launcherCode).contains("DefaultComposeActivity::class.java")
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
        assertThat(generatedFiles).hasSize(3)

        val activityFile = generatedFiles.find {
            it.name == "ComposeActivity.kt"
        }
        assertThat(activityFile).isNotNull()

        val activityCode = activityFile!!.readText()
        assertThat(activityCode).contains("\npackage com.moriatsushi.launcher\n")
        assertThat(activityCode).contains("class ComposeActivity")
        assertThat(activityCode).contains("testPackage.Other1()")
        assertThat(activityCode).contains("testPackage.Other2()")
    }

    @Test
    fun `generate OtherLauncher`() {
        val kotlinSource = SourceFile.kotlin(
            "Test.kt",
            """
                package testPackage

                import com.moriatsushi.launcher.Entry

                @Entry(default = false)
                fun Other() {
                }
            """,
        )
        val complication = createCompilation(kotlinSource)
        val result = complication.compile()
        assertThat(result.exitCode).isEqualTo(ExitCode.OK)

        val generatedFiles = findGeneratedFiles(complication)
        assertThat(generatedFiles).hasSize(2)

        val launcherFile = generatedFiles.find {
            it.name == "OtherLauncher.kt"
        }
        assertThat(launcherFile).isNotNull()

        val launcherCode = launcherFile!!.readText()
        assertThat(launcherCode).contains("\npackage testPackage\n")
        assertThat(launcherCode).contains("@Composable\nfun rememberOtherLauncher(): OtherLauncher")
        assertThat(launcherCode).contains("fun getOtherLauncher(context: Context): OtherLauncher")
        assertThat(launcherCode).contains("interface OtherLauncher")
        assertThat(launcherCode).contains("ComposeActivity::class.java")
        assertThat(launcherCode).contains("intent.putExtra(\"launcher_destination\", \"testPackage.Other\")")
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
