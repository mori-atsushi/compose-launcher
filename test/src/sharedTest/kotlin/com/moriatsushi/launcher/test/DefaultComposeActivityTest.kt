package com.moriatsushi.launcher.test

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.moriatsushi.launcher.DefaultComposeActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class DefaultComposeActivityTest {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<DefaultComposeActivity>()

    @Test
    fun `Default Entry is displayed`() {
        composeTestRule.onNodeWithText("Main").assertIsDisplayed()
    }
}
