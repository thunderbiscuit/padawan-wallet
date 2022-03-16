/*
 * Copyright 2020-2022 thunderbiscuit and contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE file.
 */

package com.goldenraven.padawanwallet.intro

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import com.goldenraven.padawanwallet.data.Repository
import com.goldenraven.padawanwallet.data.Wallet
import com.goldenraven.padawanwallet.theme.PadawanTheme
import com.goldenraven.padawanwallet.wallet.WalletActivity

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
            val activityShutDown: () -> Unit = {
                this.finish()
            }

            setContent {
                PadawanTheme {
                    IntroNavigation(activityShutDown)
                }
            }
        }
    }
}
