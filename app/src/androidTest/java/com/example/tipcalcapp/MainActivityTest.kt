package com.example.tipcalcapp

import androidx.compose.ui.test.click
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.performTouchInput
import org.junit.Rule
import org.junit.Test

class MainActivityTest {
    @get:Rule
    val composeTestRule = createAndroidComposeRule(MainActivity::class.java)

    @Test
    fun test_TopHeaderの表示確認() {
        composeTestRule.onNodeWithText("Total Per Person").assertExists()
        composeTestRule.onNodeWithText("$0.00").assertExists()
    }

    @Test
    fun test_BillFormの表示確認_初期表示() {
        composeTestRule.onNodeWithText("Enter Bill").assertExists()
        composeTestRule.onNodeWithText("Enter Bill").performTouchInput {
            click()
        }
    }

    @Test
    fun test_BillFormの表示確認_入力() {
        composeTestRule.onNodeWithText("Enter Bill").performTextInput("400")
        composeTestRule.onNodeWithText("Split").assertExists()
        composeTestRule.onNodeWithText("Tip").assertExists()
    }
}
