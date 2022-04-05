package com.goldenraven.padawanwallet.intro

import android.util.Log
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import com.goldenraven.padawanwallet.theme.PadawanTheme
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class WalletRecoveryScreenTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<IntroActivity>()

    @Before
    fun setUp() {

        val onBuildWalletButtonClicked : (WalletCreateType) -> Unit = { walletCreateType ->
            when (walletCreateType) {
                is WalletCreateType.FROMSCRATCH -> Log.i("WalletRecoveryScreenTest", "Successful wallet init")
                is WalletCreateType.RECOVER -> Log.i("WalletRecoveryScreenTest", "Successful wallet recovery")
            }
        }

        composeTestRule.setContent {
            PadawanTheme {
                WalletRecoveryScreen(onBuildWalletButtonClicked = onBuildWalletButtonClicked)
            }
        }
    }

    // Backtick function names are introduced in API 30
    // Would require setting minSdk = 30 in build.gradle.kts
    @Test
    fun recoveryWalletInit() {
        for (i in 1..12) {
            composeTestRule
                .onNodeWithText("Word $i")
                .assertExists()
        }

        composeTestRule.onNodeWithTag(testTag = "Intro EnterRecoveryPhrase Button")
            .assertExists()
    }

    @Test
    fun recoveryWalletCorrectPhrase() {
        val correctRecoveryPhrase: Map<Int, String> = mapOf(
            1 to "abandon", 2 to "abandon", 3 to "abandon", 4 to "abandon", 5 to "abandon", 6 to "abandon",
            7 to "abandon", 8 to "abandon", 9 to "abandon", 10 to "abandon", 11 to "abandon", 12 to "abandon"
        )
        for (i in 1..12) {
            correctRecoveryPhrase[i]?.let {
                composeTestRule
                    .onNodeWithText("Word $i")
                    .performScrollTo()
                    .performTextInput(it)
            }
        }
        composeTestRule.onNodeWithTag(testTag = "Intro EnterRecoveryPhrase Button")
            .performClick()
        // No snackbar would indicate that checkWord() succeeded
        composeTestRule.onNodeWithTag(testTag = "Intro WalletRecoveryScreen Snackbar")
            .assertDoesNotExist()
    }

    @Test
    fun recoveryWalletWrongPhrase() {
        val wrongRecoveryPhrase: Map<Int, String> = mapOf(
            1 to "abandon", 2 to "abandon", 3 to "abandon", 4 to "abandon", 5 to "abandon", 6 to "abandon",
            7 to "abandon", 8 to "testing123", 9 to "abandon", 10 to "abandon", 11 to "abandon", 12 to "abandon"
        )
        for (i in 1..12) {
            wrongRecoveryPhrase[i]?.let {
                composeTestRule
                    .onNodeWithText("Word $i")
                    .performScrollTo()
                    .performTextInput(it)
            }
        }
        composeTestRule.onNodeWithTag(testTag = "Intro EnterRecoveryPhrase Button")
            .performClick()
        // We are testing the snackbar to see if checkWord() failed
        composeTestRule.onNodeWithText(text = "Word 8 is not valid")
            .assertExists()
    }

    @Test
    fun recoveryWalletEmptyPhrase() {
        val wrongRecoveryPhrase: Map<Int, String> = mapOf(
            1 to "abandon", 2 to "abandon", 3 to "abandon", 4 to "abandon", 5 to "abandon", 6 to "",
            7 to "abandon", 8 to "abandon", 9 to "abandon", 10 to "abandon", 11 to "abandon", 12 to "abandon"
        )
        for (i in 1..12) {
            wrongRecoveryPhrase[i]?.let {
                composeTestRule
                    .onNodeWithText("Word $i")
                    .performScrollTo()
                    .performTextInput(it)
            }
        }
        composeTestRule.onNodeWithTag(testTag = "Intro EnterRecoveryPhrase Button")
            .performClick()
        // We are testing the snackbar to see if checkWord() failed
        composeTestRule.onNodeWithText(text = "Word 6 is empty")
            .assertExists()
    }
}