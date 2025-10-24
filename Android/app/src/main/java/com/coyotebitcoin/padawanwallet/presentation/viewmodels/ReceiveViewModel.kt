/*
 * Copyright 2020-2025 thunderbiscuit and contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE file.
 */

package com.coyotebitcoin.padawanwallet.presentation.viewmodels

import android.util.Log
import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.coyotebitcoin.padawanwallet.domain.bitcoin.Wallet
import com.coyotebitcoin.padawanwallet.domain.utils.QrUiState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.kotlinbitcointools.bip21.Bip21URI

private const val TAG = "ReceiveViewModel"

@Stable
data class ReceiveScreenState(
    val address: String? = null,
    val bip21Uri: String? = null,
    val addressIndex: UInt? = null,
    val qrState: QrUiState = QrUiState.NoQR
)

sealed interface ReceiveScreenAction {
    data object UpdateAddress : ReceiveScreenAction
}

internal class ReceiveViewModel : ViewModel() {
    private val _state: MutableStateFlow<ReceiveScreenState> = MutableStateFlow(ReceiveScreenState())
    val state: StateFlow<ReceiveScreenState> = _state.asStateFlow()

    fun onAction(action: ReceiveScreenAction) {
        when (action) {
            is ReceiveScreenAction.UpdateAddress -> updateLastUnusedAddress()
        }
    }

    private fun updateLastUnusedAddress() {
        viewModelScope.launch {
            _state.update { it.copy(qrState = QrUiState.Loading) }

            val address = Wallet.getLastUnusedAddress()
            delay(400)
            val uri = Bip21URI(address = address.address.toString()).toURI()
            Log.i(TAG, "New address URI: $uri")

            _state.update {
                it.copy(
                    address = address.address.toString(),
                    bip21Uri = uri,
                    addressIndex = address.index,
                    qrState = QrUiState.QR
                )
            }
        }
    }
}
