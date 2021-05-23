package com.goldenraven.padawanwallet.utils

import timber.log.Timber

public fun isSend(sent: Int, received: Int): Boolean {
    Timber.i("[PADAWANLOGS] isSend value is ${received - sent}")
    return received - sent < 0
}