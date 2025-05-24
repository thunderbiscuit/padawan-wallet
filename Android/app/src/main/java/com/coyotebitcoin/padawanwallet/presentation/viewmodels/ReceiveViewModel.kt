/*
 * Copyright 2020-2025 thunderbiscuit and contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE file.
 */

package com.coyotebitcoin.padawanwallet.presentation.viewmodels

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.coyotebitcoin.padawanwallet.domain.bitcoin.Wallet
import com.coyotebitcoin.padawanwallet.presentation.viewmodels.mvi.ReceiveScreenAction
import com.coyotebitcoin.padawanwallet.presentation.viewmodels.mvi.ReceiveScreenState
import com.coyotebitcoin.padawanwallet.domain.utils.QrUiState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.bitcointools.bip21.Bip21URI

private const val TAG = "ReceiveViewModel"

internal class ReceiveViewModel : ViewModel() {
    var state: ReceiveScreenState by mutableStateOf(ReceiveScreenState())
        private set

    fun onAction(action: ReceiveScreenAction) {
        when (action) {
            is ReceiveScreenAction.UpdateAddress -> updateLastUnusedAddress()
        }
    }

    private fun updateLastUnusedAddress() {
        viewModelScope.launch {
            state = state.copy(qrState = QrUiState.Loading)
            val address = Wallet.getLastUnusedAddress()
            delay(400)
            val uri = Bip21URI(address = address.address.toString()).toURI()
            Log.i(TAG, "New address URI: $uri")

            state = ReceiveScreenState(
                address = address.address.toString(),
                bip21Uri = uri,
                addressIndex = address.index,
                qrState = QrUiState.QR
            )
        }
    }
}
