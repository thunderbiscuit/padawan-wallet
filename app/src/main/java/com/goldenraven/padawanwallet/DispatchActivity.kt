/*
 * Copyright 2020 thunderbiscuit and contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE file.
 */

package com.goldenraven.padawanwallet

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.goldenraven.padawanwallet.data.Repository
import com.goldenraven.padawanwallet.data.Wallet
import com.goldenraven.padawanwallet.intro.IntroActivity
import com.goldenraven.padawanwallet.wallet.WalletActivity

class DispatchActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // ask Repository if a wallet already exists
        // if so, load it and launch into wallet activity, otherwise go to intro
        if (Repository.doesWalletExist()) {
            Wallet.loadExistingWallet()
            startActivity(Intent(this, WalletActivity::class.java))
            finish()
        } else {
            startActivity(Intent(this, IntroActivity::class.java))
            finish()
        }
    }
}
