package com.goldenraven.padawanwallet.utils

public fun netSendWithoutFees(txSatsOut: Int, txSatsIn: Int, fees: Int): Int {
    return txSatsOut - (txSatsIn + fees)
}
