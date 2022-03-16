/*
 * Copyright 2020-2022 thunderbiscuit and contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE file.
 */

package com.goldenraven.padawanwallet.intro

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import com.goldenraven.padawanwallet.data.Repository
import com.goldenraven.padawanwallet.data.Wallet
import com.goldenraven.padawanwallet.theme.PadawanTheme
import com.goldenraven.padawanwallet.utils.SnackbarLevel
import com.goldenraven.padawanwallet.utils.fireSnackbar
import com.goldenraven.padawanwallet.wallet.WalletActivity

private const val TAG = "IntroActivity"

class IntroActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // ask the repository if a wallet already exists
        // if so, load it and launch into wallet activity, otherwise go to intro
        if (Repository.doesWalletExist()) {
            Wallet.loadExistingWallet()
            startActivity(Intent(this, WalletActivity::class.java))
            finish()
        } else {
            // this function is used in composables that navigate to the wallet activity
            // to ensure we destroy the intro activity once the wallet starts
            val onBuildWalletButtonClicked : (WalletCreateType, String?) -> Unit = { walletCreateType, recoveryPhrase ->
                try {
                    // load up a wallet either from scratch or using a BIP39 recovery phrase
                    when (walletCreateType) {
                        // if we create a wallet from scratch we don't need a recovery phrase
                        WalletCreateType.FROMSCRATCH -> Wallet.createWallet()

                        WalletCreateType.RECOVER -> recoveryPhrase?.let { recoveryPhrase ->
                            Wallet.recoverWallet(recoveryPhrase)
                        } ?: Log.i(TAG, "Error: Attempting to recover using an empty recovery phrase")
                    }
                    startActivity(Intent(this, WalletActivity::class.java))
                    finish()
                } catch (e: Throwable) {
                    Log.i(TAG, "Could not build wallet: $e")
                    // display snackbar with error
                    fireSnackbar(
                        view = findViewById(android.R.id.content),
                        level = SnackbarLevel.ERROR,
                        message = "Error: $e"
                    )
                }
            }

            setContent {
                PadawanTheme {
                    IntroNavigation(onBuildWalletButtonClicked)
                }
            }
        }
    }
}

enum class WalletCreateType {
    RECOVER,
    FROMSCRATCH
}
