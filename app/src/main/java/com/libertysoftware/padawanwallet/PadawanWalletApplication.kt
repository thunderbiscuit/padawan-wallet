package com.libertysoftware.padawanwallet

import android.app.Application
import timber.log.Timber

class PadawanWalletApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
    }
}
