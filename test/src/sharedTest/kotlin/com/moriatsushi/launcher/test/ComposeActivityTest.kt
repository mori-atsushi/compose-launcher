package com.moriatsushi.launcher.test

import android.content.Intent
import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createEmptyComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.test.core.app.launchActivity
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.moriatsushi.launcher.ComposeActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ComposeActivityTest {
    @get:Rule
    val composeTestRule = createEmptyComposeRule()

    private val context = InstrumentationRegistry.getInstrumentation().targetContext

    @Test
    fun `Entry of Other1 is displayed`() {
        val intent = Intent(context, ComposeActivity::class.java)
        intent.putExtra("launcher_destination", "com.moriatsushi.launcher.test.Other1")
        launchActivity<ComponentActivity>(intent)
        composeTestRule.onNodeWithText("Other1").assertIsDisplayed()
    }

    @Test
    fun `Entry of Other2 is displayed`() {
        val intent = Intent(context, ComposeActivity::class.java)
        intent.putExtra("launcher_destination", "com.moriatsushi.launcher.test.Other2")
        launchActivity<ComponentActivity>(intent)
        composeTestRule.onNodeWithText("Other2").assertIsDisplayed()
    }
}
