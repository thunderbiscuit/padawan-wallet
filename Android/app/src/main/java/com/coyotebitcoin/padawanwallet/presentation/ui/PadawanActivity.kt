/*
 * Copyright 2020-2025 thunderbiscuit and contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE file.
 */

package com.coyotebitcoin.padawanwallet.presentation.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.coyotebitcoin.padawanwallet.domain.bitcoin.Wallet
import com.coyotebitcoin.padawanwallet.domain.bitcoin.WalletRepository
import com.coyotebitcoin.padawanwallet.domain.settings.SettingsRepository
import com.coyotebitcoin.padawanwallet.presentation.navigation.NavigationRoot
import com.coyotebitcoin.padawanwallet.presentation.theme.PadawanTheme

private const val TAG = "PadawanActivity"

class PadawanActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Ask shared preferences if a wallet already exists. If so, load it and launch into wallet screens, otherwise go
        // to onboarding screens.
        val onboardingDone = WalletRepository.doesWalletExist()
        if (onboardingDone) Wallet.loadWallet()

        setContent {
            PadawanTheme(SettingsRepository.getTheme()) {
                NavigationRoot(
                    onboardingDone = onboardingDone
                )
            }
        }
    }
}
