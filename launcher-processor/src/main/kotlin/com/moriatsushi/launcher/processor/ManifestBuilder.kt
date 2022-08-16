package com.moriatsushi.launcher.processor

import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import org.intellij.lang.annotations.Language
import java.io.File

internal class ManifestBuilder(private val environment: SymbolProcessorEnvironment) {

    @Language("xml")
    fun buildManifest(packageName: String): String {
        return """
            <?xml version="1.0" encoding="utf-8"?>
            <manifest xmlns:android="http://schemas.android.com/apk/res/android"
                    package="$packageName">

                <application>
                    <activity
                            android:name="$packageName.DefaultComposeActivity"
                            android:exported="true">
                        <intent-filter>
                            <action android:name="android.intent.action.MAIN" />

                            <category android:name="android.intent.category.LAUNCHER" />
                        </intent-filter>
                    </activity>

                    <activity
                            android:name="$packageName.ComposeActivity"
                            android:exported="false" />
                </application>
            </manifest>

        """.trimIndent()
    }

    @Language("xml")
    fun buildManifestModule(packageName: String): String {
        return """
            <?xml version="1.0" encoding="utf-8"?>
            <manifest xmlns:android="http://schemas.android.com/apk/res/android"
                    package="$packageName">

                <application>
                    <activity
                            android:name="$packageName.ComposeActivity"
                            android:exported="false" />
                </application>
            </manifest>
        """.trimIndent()
    }

    fun createManifest(entry: String) {
        val path = environment.options["manifest_path"].orEmpty()
        val file = File(path, "AndroidManifest.xml")
        if (!file.exists()) {
            file.createNewFile()
        }
        file.writeText(entry)
    }
}
