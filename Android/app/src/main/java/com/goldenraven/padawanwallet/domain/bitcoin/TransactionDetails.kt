package com.goldenraven.padawanwallet.domain.bitcoin

import com.goldenraven.padawanwallet.utils.TxType
import org.rustbitcoin.bitcoin.Amount
import org.rustbitcoin.bitcoin.FeeRate

data class TransactionDetails(
    val txid: String,
    val sent: Amount,
    val received: Amount,
    val fee: Amount,
    val feeRate: FeeRate,
    val txType: TxType,
    val chainPosition: ChainPosition
)

sealed interface ChainPosition {
    data object Unconfirmed : ChainPosition
    data class Confirmed(val height: UInt, val timestamp: ULong) : ChainPosition
}
