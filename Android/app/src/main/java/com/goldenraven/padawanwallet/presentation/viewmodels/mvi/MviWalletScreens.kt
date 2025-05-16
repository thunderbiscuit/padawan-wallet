/*
 * Copyright 2020-2025 thunderbiscuit and contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE file.
 */

package com.goldenraven.padawanwallet.presentation.viewmodels.mvi

import com.goldenraven.padawanwallet.domain.bitcoin.TransactionDetails
import org.bitcoindevkit.Amount
import org.bitcoindevkit.FeeRate
import org.bitcoindevkit.Transaction

data class WalletState(
    val balance: ULong,
    val transactions: List<TransactionDetails>,
    val txAndFee: Pair<Transaction, Amount>? = null,
    val isOnline: Boolean,
    val currentlySyncing: Boolean,
    val messageForUi: Pair<MessageType, String>? = null,
    val sendAddress: String? = null
)

enum class MessageType {
    Error,
    Success,
}

sealed interface WalletAction {
    data object Sync : WalletAction
    data object RequestCoins : WalletAction
    data object CheckNetworkStatus : WalletAction
    data class  QRCodeScanned(val address: String) : WalletAction
    data class  Broadcast(val tx: Transaction) : WalletAction
    data object UiMessageDelivered : WalletAction
    data class  SeeSingleTx(val tx: String) : WalletAction
    data class  BuildAndSignPsbt(val address: String, val amount: Amount, val feeRate: FeeRate) : WalletAction
}
