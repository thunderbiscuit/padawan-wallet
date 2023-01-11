/*
 * Copyright 2020-2022 thunderbiscuit and contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE file.
 */

package com.goldenraven.padawanwallet.utils

import org.bitcoindevkit.TransactionDetails

fun netSendWithoutFees(txSatsOut: SatoshisOut, txSatsIn: SatoshisIn, fees: Int): Int {
    return txSatsOut.satoshis - (txSatsIn.satoshis + fees)
}

fun isPayment(sent: SatoshisOut, received: SatoshisIn): Boolean {
    return (received.satoshis - sent.satoshis) < 0
}

fun parseTxAmounts(txDetails: TransactionDetails): TxInfo {
    if (txDetails.sent == 0uL) {
        val fee = txDetails.fee ?: 0uL
        return TxInfo(type = TxType.RECEIVE, valueIn = txDetails.received, valueOut = 0uL, fee = fee)
    } else {
        val fee = txDetails.fee ?: 0uL
        val netSend: Int = netSendWithoutFees(
            txSatsOut = SatoshisOut(txDetails.sent.toInt()),
            txSatsIn = SatoshisIn(txDetails.received.toInt()),
            fees = fee.toInt()
        )
        return TxInfo(type = TxType.PAYMENT, valueIn = 0uL, valueOut = netSend.toULong(), fee = fee)
    }
}

@JvmInline
value class SatoshisIn(val satoshis: Int)

@JvmInline
value class SatoshisOut(val satoshis: Int)

enum class TxType {
    PAYMENT,
    RECEIVE,
}

data class TxInfo(val type: TxType, val valueIn: ULong, val valueOut: ULong, val fee: ULong)
