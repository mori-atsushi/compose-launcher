package com.moriatsushi.launcher.test

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.ComponentNameMatchers.hasClassName
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.intent.matcher.IntentMatchers.hasExtra
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.hamcrest.core.AllOf.allOf
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LauncherComposableTest {
    @get:Rule
    val composeTestRule = createComposeRule()

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
        composeTestRule.setContent {
            val launcher = rememberMainLauncher()
            LaunchedEffect(Unit) {
                launcher.launch()
            }
        }
        val expectedClass = "com.moriatsushi.launcher.DefaultComposeActivity"
        intended(hasComponent(hasClassName(expectedClass)))
    }

    @Test
    fun `launch ComposeActivity`() {
        composeTestRule.setContent {
            val launcher = rememberOther1Launcher()
            LaunchedEffect(Unit) {
                launcher.launch()
            }
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
