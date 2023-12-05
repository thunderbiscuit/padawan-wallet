/*
 * Copyright 2020-2023 thunderbiscuit and contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE file.
 */

package com.goldenraven.padawanwallet

import android.app.Application
import android.content.Context
import com.goldenraven.padawanwallet.domain.tutorials.TutorialRepository
import com.goldenraven.padawanwallet.domain.wallet.WalletRepository
import com.goldenraven.padawanwallet.domain.wallet.Wallet

class PadawanWalletApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        // initialize Wallet object with path variable
        Wallet.setPath(applicationContext.filesDir.toString())

        // initialize repositories and set shared preferences
        WalletRepository.setSharedPreferences(applicationContext.getSharedPreferences("wallet", Context.MODE_PRIVATE))
        TutorialRepository.setSharedPreferences(applicationContext.getSharedPreferences("tutorials", Context.MODE_PRIVATE))
    }
}
