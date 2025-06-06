/*
 * Copyright 2020-2025 thunderbiscuit and contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE file.
 */

package com.coyotebitcoin.padawanwallet.presentation.ui

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.coyotebitcoin.padawanwallet.domain.bitcoin.Wallet
import com.coyotebitcoin.padawanwallet.domain.bitcoin.WalletRepository
import com.coyotebitcoin.padawanwallet.domain.settings.SettingsRepository
import com.coyotebitcoin.padawanwallet.domain.utils.WalletCreateType
import com.coyotebitcoin.padawanwallet.presentation.navigation.HomeNavigation
import com.coyotebitcoin.padawanwallet.presentation.navigation.IntroNavigation
import com.coyotebitcoin.padawanwallet.presentation.theme.PadawanTheme

private const val TAG = "PadawanActivity"

class PadawanActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // ask the repository if a wallet already exists
        // if so, load it and launch into wallet activity, otherwise go to intro
        if (WalletRepository.doesWalletExist()) {
            Wallet.loadWallet()
            Log.i(TAG, "Wallet already exists!")

            setContent {
                PadawanTheme(SettingsRepository.getTheme()) {
                    HomeNavigation()
                }
            }
        } else {
            // this function is used in composables that navigate to the wallet activity
            // to ensure we destroy the intro activity once the wallet starts
            val onBuildWalletButtonClicked : (WalletCreateType) -> Unit = { walletCreateType ->
                try {
                    // load up a wallet either from scratch or using a BIP39 recovery phrase
                    when (walletCreateType) {
                        // if we create a wallet from scratch we don't need a recovery phrase
                        is WalletCreateType.FROMSCRATCH -> Wallet.createWallet()
                        is WalletCreateType.RECOVER -> Wallet.recoverWallet(walletCreateType.recoveryPhrase)
                    }

                    setContent {
                        PadawanTheme(SettingsRepository.getTheme()) {
                            HomeNavigation()
                        }
                    }
                } catch (e: Throwable) {
                    Log.i(TAG, "Could not build wallet: $e")
                    Toast.makeText(applicationContext, "Could not build wallet: $e", Toast.LENGTH_LONG).show()
                }
            }

            setContent {
                PadawanTheme(SettingsRepository.getTheme()) {
                    IntroNavigation(onBuildWalletButtonClicked)
                }
            }
        }
    }
}
