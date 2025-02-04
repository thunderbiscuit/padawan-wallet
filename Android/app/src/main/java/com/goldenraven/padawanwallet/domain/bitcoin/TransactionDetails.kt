/*
 * Copyright 2020-2025 thunderbiscuit and contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE file.
 */

package com.goldenraven.padawanwallet.domain.bitcoin

import org.rustbitcoin.bitcoin.Amount
import org.rustbitcoin.bitcoin.FeeRate

data class TransactionDetails(
    val txid: String,
    val sent: Amount,
    val received: Amount,
    val paymentAmount: ULong,
    val fee: Amount,
    val feeRate: FeeRate,
    val txType: TxType,
    val chainPosition: ChainPosition
)

sealed interface ChainPosition {
    data object Unconfirmed : ChainPosition
    data class Confirmed(val height: UInt, val timestamp: ULong) : ChainPosition
}
