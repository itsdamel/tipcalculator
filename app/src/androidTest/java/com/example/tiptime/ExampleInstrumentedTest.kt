package com.example.tiptime

import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performTextInput
import com.example.tiptime.ui.theme.TipTimeTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import java.text.NumberFormat
import java.util.Locale

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */

class TipUITests {
    @get:Rule
    val composeTestRule = createComposeRule()
    /*Setting layout to test, similar to onCreate()*/
    @Test
    fun calculate_20_percent_tip() {
        composeTestRule.setContent {
            TipTimeTheme {
                Layout()
            }
        }
        composeTestRule.onNodeWithText("€")
            .performTextInput("10")

        composeTestRule.onNodeWithText("%")
            .performTextInput("20")
        val expectedTip = NumberFormat.getNumberInstance(Locale.FRANCE).format(2)
        composeTestRule.onNodeWithText("$expectedTip€").assertExists(
            "No node with this text was found."
        )
    }
}



