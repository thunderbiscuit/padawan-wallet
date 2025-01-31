/*
 * Copyright 2020-2024 thunderbiscuit and contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE file.
 */

package com.goldenraven.padawanwallet.utils

/**
 * Calculates the net amount sent in a transaction, without fees. By default BDK gives us the total number of
 * satoshis sent in a transaction, but for display purposes it's often useful to break that amount in two: what
 * was sent to the recipient and the fees paid.
 */
fun netSendWithoutFees(txSatsOut: ULong, txSatsIn: ULong, fee: ULong): ULong {
    return txSatsOut - (txSatsIn + fee)
}

/**
 * Determines whether a transaction is a payment or a receive.
 */
fun txType(sent: ULong, received: ULong): TxType {
    return if (sent > received) TxType.PAYMENT else TxType.RECEIVE
}

enum class TxType {
    PAYMENT,
    RECEIVE,
}
