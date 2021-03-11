/*
 * Copyright 2020 thunderbiscuit and contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE file.
 */

package com.goldenraven.padawanwallet

import android.app.Application
import timber.log.Timber

class PadawanWalletApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        // logs
        Timber.plant(Timber.DebugTree())

        // initialize Wallet object with path variable
        Wallet.setPath(applicationContext.filesDir.toString())

        // initialize Repository object with applicationContext
        // Repository.setApplicationContext(applicationContext)
    }
}
