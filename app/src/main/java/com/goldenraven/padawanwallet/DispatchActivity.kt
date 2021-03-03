/*
 * Copyright 2020 thunderbiscuit and contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE file.
 */

package com.goldenraven.padawanwallet

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.goldenraven.padawanwallet.home.HomeActivity
import com.goldenraven.padawanwallet.intro.IntroActivity
import timber.log.Timber

class DispatchActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // initiate Wallet object
        // val wallet: Wallet = Wallet
        Wallet.helloFrom("DispatchActivity")

        // load wallet if it exists
        val currentWallet: SharedPreferences = getSharedPreferences("current_wallet", Context.MODE_PRIVATE)

        val currentWalletExists: Boolean = currentWallet.getBoolean("initialized", false)
        Timber.i("[PADAWANLOGS] Value of currentWalletExists at launch: $currentWalletExists")

        if (currentWalletExists) {
           val name: String = currentWallet.getString("name", null)!!
           val path: String = currentWallet.getString("path", null)!!
           val descriptor: String = currentWallet.getString("descriptor", null)!!
           val changeDescriptor: String = currentWallet.getString("changeDescriptor", null)!!
           val electrumURL: String = currentWallet.getString("electrumURL", null)!!

           val app = application as PadawanWalletApplication
           app.initialize(
               name = name,
               path = path,
               descriptor = descriptor,
               changeDescriptor = changeDescriptor,
               electrumURL = electrumURL,
               electrumProxy = null,
           )
        }

        // launch into wallet activity if user already has a padawan wallet on device
        if (currentWalletExists) {
            startActivity(Intent(this, HomeActivity::class.java))
        } else {
            startActivity(Intent(this, IntroActivity::class.java))
        }
    }
}
