/*
 * Copyright 2020-2025 thunderbiscuit and contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE file.
 */

package com.coyotebitcoin.padawanwallet.presentation.ui

import android.R
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.coyotebitcoin.padawanwallet.domain.settings.SettingsRepository
import com.coyotebitcoin.padawanwallet.domain.bitcoin.WalletRepository
import com.coyotebitcoin.padawanwallet.domain.bitcoin.Wallet
import com.coyotebitcoin.padawanwallet.presentation.navigation.IntroNavigation
import com.coyotebitcoin.padawanwallet.presentation.theme.PadawanTheme
import com.coyotebitcoin.padawanwallet.presentation.navigation.HomeNavigation
import com.coyotebitcoin.padawanwallet.presentation.utils.SnackbarLevel
import com.coyotebitcoin.padawanwallet.domain.utils.WalletCreateType
import com.coyotebitcoin.padawanwallet.presentation.utils.fireSnackbar
import com.coyotebitcoin.padawanwallet.domain.utils.setLanguage

private const val TAG = "PadawanActivity"

class PadawanActivity : AppCompatActivity() {
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
                    fireSnackbar(
                        view = findViewById(R.id.content),
                        level = SnackbarLevel.ERROR,
                        message = "Error: $e"
                    )
                }
            }

            setContent {
                PadawanTheme(SettingsRepository.getTheme()) {
                    IntroNavigation(onBuildWalletButtonClicked)
                }
            }
        }
    }

    // TODO: This feels hacky but the AppCompatDelegate.getApplicationLocales() API returns an
    //       an empty list if it hasn't been set manually. We cannot set it directly inside
    //       the onCreate() method, and so we run it here.
    // TODO: This can be cleaned up. The behaviour between API 33+ and > 33 are different, and not
    //       handled optimally by this code.
    // Languages: We check if the language has been manually set in the app
    // and if it has not we use the system default language.
    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(newBase)
        setLanguage()
    }
}
