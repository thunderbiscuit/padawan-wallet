/*
 * Copyright 2020-2025 thunderbiscuit and contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE file.
 */

package com.coyotebitcoin.padawanwallet.intro

import android.util.Log
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import com.coyotebitcoin.padawanwallet.domain.utils.WalletCreateType
import com.coyotebitcoin.padawanwallet.presentation.theme.PadawanTheme
import com.coyotebitcoin.padawanwallet.presentation.ui.screens.intro.WalletRecoveryScreen
import org.junit.Before
import org.junit.Rule
import org.junit.Test

private const val TAG = "WalletRecoveryScreenTest"

class WalletRecoveryScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Before
    fun setUp() {

        val onBuildWalletButtonClicked : (WalletCreateType) -> Unit = { walletCreateType ->
            when (walletCreateType) {
                is WalletCreateType.FROMSCRATCH -> Log.i(TAG, "Successful wallet initialization")
                is WalletCreateType.RECOVER -> Log.i(TAG, "Successful wallet recovery")
            }
        }

        composeTestRule.setContent {
            PadawanTheme {
                WalletRecoveryScreen(
                    onBuildWalletButtonClicked = onBuildWalletButtonClicked
                )
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
}
