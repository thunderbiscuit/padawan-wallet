package com.goldenraven.padawanwallet

import timber.log.Timber

object Wallet {

    public fun helloFrom(location: String): Unit {
        Timber.i("[PADAWANLOGS] New wallet is alive and well, reporting from $location")
    }
}
