/*
 * Copyright 2020-2022 thunderbiscuit and contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE file.
 */

package com.goldenraven.padawanwallet.utils

fun netSendWithoutFees(txSatsOut: SatoshisOut, txSatsIn: SatoshisIn, fees: Int): Int {
    return txSatsOut.satoshis - (txSatsIn.satoshis + fees)
}

fun isPayment(sent: SatoshisOut, received: SatoshisIn): Boolean {
    return (received.satoshis - sent.satoshis) < 0
}

@JvmInline
value class SatoshisIn(val satoshis: Int)

@JvmInline
value class SatoshisOut(val satoshis: Int)
