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
 * Determines whether a transaction is a payment (outbound) or a receive (inbound). We define an
 * outbound transaction as one where the amount sent is greater than the amount received, and vice
 * versa for inbound transactions.
 */
fun txType(sent: ULong, received: ULong): TxType {
    return if (sent > received) TxType.OUTBOUND else TxType.INBOUND
}

/**
 * We define an outbound transaction as one where the amount sent is greater than the amount
 * received, and vice versa for inbound transactions.
 */
enum class TxType {
    OUTBOUND,
    INBOUND,
}
