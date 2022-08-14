package com.moriatsushi.launcher.test

import androidx.activity.ComponentActivity
import androidx.test.core.app.launchActivity
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.ComponentNameMatchers.hasClassName
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.intent.matcher.IntentMatchers.hasExtra
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.hamcrest.core.AllOf.allOf
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LauncherAndroidTest {
    @Before
    fun setup() {
        Intents.init()
    }

    @After
    fun clear() {
        Intents.release()
    }

    @Test
    fun `launch DefaultComposeActivity`() {
        val scenario = launchActivity<ComponentActivity>()
        scenario.onActivity {
            val launcher = getMainLauncher(it)
            launcher.launch()
        }
        val expectedClass = "com.moriatsushi.launcher.DefaultComposeActivity"
        intended(hasComponent(hasClassName(expectedClass)))
    }

    @Test
    fun `launch ComposeActivity`() {
        val scenario = launchActivity<ComponentActivity>()
        scenario.onActivity {
            val launcher = getOther1Launcher(it)
            launcher.launch()
        }
        val expectedClass = "com.moriatsushi.launcher.ComposeActivity"
        val expectedDestination = "com.moriatsushi.launcher.test.Other1"
        intended(
            allOf(
                hasComponent(hasClassName(expectedClass)),
                hasExtra("launcher_destination", expectedDestination),
            ),
        )
    }
}
