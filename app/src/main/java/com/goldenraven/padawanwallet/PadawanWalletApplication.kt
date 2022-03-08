/*
 * Copyright 2020-2022 thunderbiscuit and contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE file.
 */

package com.goldenraven.padawanwallet

import android.app.Application
import android.content.Context
import com.goldenraven.padawanwallet.data.Repository
import com.goldenraven.padawanwallet.data.Wallet

class PadawanWalletApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        // initialize Wallet object with path variable
        Wallet.setPath(applicationContext.filesDir.toString())

        // initialize Repository object and set shared preferences
        Repository.setSharedPreferences(applicationContext.getSharedPreferences("current_wallet", Context.MODE_PRIVATE))
    }
}
