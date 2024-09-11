package com.goldenraven.padawanwallet.intro

import android.util.Log
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import com.goldenraven.padawanwallet.presentation.theme.PadawanTheme
import com.goldenraven.padawanwallet.presentation.ui.screens.intro.WalletRecoveryScreen
import org.junit.Before
import org.junit.Rule
import org.junit.Test

private const val TAG = "WalletRecoveryScreenTest"

class WalletRecoveryScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Before
    fun setUp() {

        val onBuildWalletButtonClicked : (com.goldenraven.padawanwallet.presentation.ui.WalletCreateType) -> Unit = { walletCreateType ->
            when (walletCreateType) {
                is com.goldenraven.padawanwallet.presentation.ui.WalletCreateType.FROMSCRATCH -> Log.i(TAG, "Successful wallet init")
                is com.goldenraven.padawanwallet.presentation.ui.WalletCreateType.RECOVER -> Log.i(TAG, "Successful wallet recovery")
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

        composeTestRule
            .onNodeWithTag(testTag = "Intro EnterRecoveryPhrase Button")
            .assertExists()
    }

    // @Test
    // fun recoveryWalletCorrectPhrase() {
    //     val correctRecoveryPhrase: Map<Int, String> = mapOf(
    //         1 to "abandon", 2 to "abandon", 3 to "abandon", 4 to "abandon", 5 to "abandon", 6 to "abandon",
    //         7 to "abandon", 8 to "abandon", 9 to "abandon", 10 to "abandon", 11 to "abandon", 12 to "abandon"
    //     )
    //     for (i in 1..12) {
    //         correctRecoveryPhrase[i]?.let {
    //             composeTestRule
    //                 .onNodeWithText("Word $i")
    //                 .performScrollTo()
    //                 .performTextInput(it)
    //         }
    //     }
    //     composeTestRule
    //         .onNodeWithTag(testTag = "Intro EnterRecoveryPhrase Button")
    //         .performClick()
    //     // No snackbar would indicate that checkWord() succeeded
    //     composeTestRule
    //         .onNodeWithTag(testTag = "Intro WalletRecoveryScreen Snackbar")
    //         .assertDoesNotExist()
    // }

    // @Test
    // fun recoveryWalletWrongPhrase() {
    //     val wrongRecoveryPhrase: Map<Int, String> = mapOf(
    //         1 to "abandon", 2 to "abandon", 3 to "abandon", 4 to "abandon", 5 to "abandon", 6 to "abandon",
    //         7 to "abandon", 8 to "testing123", 9 to "abandon", 10 to "abandon", 11 to "abandon", 12 to "abandon"
    //     )
    //     for (i in 1..12) {
    //         wrongRecoveryPhrase[i]?.let {
    //             composeTestRule
    //                 .onNodeWithText("Word $i")
    //                 .performScrollTo()
    //                 .performTextInput(it)
    //         }
    //     }
    //     composeTestRule
    //         .onNodeWithTag(testTag = "Intro EnterRecoveryPhrase Button")
    //         .performClick()
    //     // We are testing the snackbar to see if checkWord() failed
    //     composeTestRule
    //         .onNodeWithText(text = "Word 8 is not valid")
    //         .assertExists()
    // }

    // @Test
    // fun recoveryWalletEmptyPhrase() {
    //     val wrongRecoveryPhrase: Map<Int, String> = mapOf(
    //         1 to "abandon", 2 to "abandon", 3 to "abandon", 4 to "abandon", 5 to "abandon", 6 to "",
    //         7 to "abandon", 8 to "abandon", 9 to "abandon", 10 to "abandon", 11 to "abandon", 12 to "abandon"
    //     )
    //     for (i in 1..12) {
    //         wrongRecoveryPhrase[i]?.let {
    //             composeTestRule
    //                 .onNodeWithText("Word $i")
    //                 .performScrollTo()
    //                 .performTextInput(it)
    //         }
    //     }
    //     composeTestRule
    //         .onNodeWithTag(testTag = "Intro EnterRecoveryPhrase Button")
    //         .performClick()
    //     // We are testing the snackbar to see if checkWord() failed
    //     composeTestRule
    //         .onNodeWithText(text = "Word 6 is empty")
    //         .assertExists()
    // }
}
