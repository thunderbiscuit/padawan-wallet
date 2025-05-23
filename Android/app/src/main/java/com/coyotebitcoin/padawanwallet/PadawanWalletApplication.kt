/*
 * Copyright 2020-2025 thunderbiscuit and contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE file.
 */

package com.coyotebitcoin.padawanwallet

import android.app.Application
import android.content.Context
import com.coyotebitcoin.padawanwallet.domain.SettingsRepository
import com.coyotebitcoin.padawanwallet.domain.tutorials.TutorialRepository
import com.coyotebitcoin.padawanwallet.domain.bitcoin.WalletRepository
import com.coyotebitcoin.padawanwallet.domain.bitcoin.Wallet

class PadawanWalletApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        val filesDirectoryPath: String = applicationContext.filesDir.toString()

        // initialize repositories and set shared preferences
        WalletRepository.setSharedPreferences(applicationContext.getSharedPreferences("wallet", Context.MODE_PRIVATE))
        TutorialRepository.setSharedPreferences(applicationContext.getSharedPreferences("tutorials", Context.MODE_PRIVATE))
        SettingsRepository.setSharedPreferences(applicationContext.getSharedPreferences("settings", Context.MODE_PRIVATE))

        // initialize Wallet object with path variable
        Wallet.setPathAndConnectDb(filesDirectoryPath)
    }
}
