/*
 * Copyright 2020-2024 thunderbiscuit and contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE file.
 */

package com.goldenraven.padawanwallet.presentation.viewmodels.mvi

import com.goldenraven.padawanwallet.domain.tx.Tx
import org.bitcoindevkit.PartiallySignedTransaction

data class WalletState(
    val balance: ULong,
    val transactions: List<Tx>,
    val isOnline: Boolean,
    val currentlySyncing: Boolean,
    val messageForUi: Pair<MessageType, String>? = null,
)

enum class MessageType {
    Error,
    Success,
}

sealed interface WalletAction {
    data object Sync : WalletAction
    data object RequestCoins : WalletAction
    data object CheckNetworkStatus : WalletAction
    data class QRCodeScanned(val address: String) : WalletAction
    data class Broadcast(val tx: PartiallySignedTransaction) : WalletAction
    data object UiMessageDelivered : WalletAction
}
