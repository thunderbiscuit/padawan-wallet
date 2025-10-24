/*
 * Copyright 2020-2025 thunderbiscuit and contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE file.
 */

package com.coyotebitcoin.padawanwallet.presentation.viewmodels

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.coyotebitcoin.padawanwallet.BuildConfig
import com.coyotebitcoin.padawanwallet.domain.bitcoin.FaucetCall
import com.coyotebitcoin.padawanwallet.domain.bitcoin.FaucetService
import com.coyotebitcoin.padawanwallet.domain.bitcoin.TransactionDetails
import com.coyotebitcoin.padawanwallet.domain.bitcoin.Wallet
import com.coyotebitcoin.padawanwallet.domain.bitcoin.WalletRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.bitcoindevkit.Amount
import org.bitcoindevkit.FeeRate
import org.bitcoindevkit.Transaction
import kotlin.system.measureTimeMillis

private const val TAG = "WalletViewModel"

data class WalletState(
    val balance: ULong,
    val transactions: List<TransactionDetails>,
    val txAndFee: Pair<Transaction, Amount>? = null,
    val isOnline: Boolean,
    val currentlySyncing: Boolean,
    val messageForUi: Pair<MessageType, String>? = null,
    val sendAddress: String? = null,
    val userCanRequestFaucetCoins: Boolean = false,
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

class WalletViewModel(application: Application) : AndroidViewModel(application) {
    private var txList: List<TransactionDetails> by mutableStateOf(emptyList())
    private var isOnline: Boolean by mutableStateOf(false)
    private var sendAddress: String? by mutableStateOf(null)
    private var singleTxDetails: TransactionDetails? = null
    private var userCanRequestFaucetCoins: Boolean by mutableStateOf(false)

    private val _walletState: MutableStateFlow<WalletState> = MutableStateFlow(
        WalletState(
            balance = 0u,
            transactions = txList,
            txAndFee = null,
            isOnline = isOnline,
            currentlySyncing = false
        )
    )
    val walletState: StateFlow<WalletState> = _walletState.asStateFlow()

    init {
        isOnline = updateNetworkStatus(application)
        userCanRequestFaucetCoins = !WalletRepository.hasUserClaimedFaucetCoins()
        _walletState.update { it.copy(isOnline = isOnline, userCanRequestFaucetCoins = userCanRequestFaucetCoins) }
        // firstAutoSync()
    }

    fun onAction(action: WalletAction) {
        when (action) {
            is WalletAction.Sync -> sync()
            is WalletAction.RequestCoins -> requestCoins()
            is WalletAction.CheckNetworkStatus -> updateNetworkStatus()
            is WalletAction.QRCodeScanned -> updateSendAddress(action.address)
            is WalletAction.Broadcast -> broadcastTransaction(action.tx)
            is WalletAction.UiMessageDelivered -> uiMessageDelivered()
            is WalletAction.SeeSingleTx -> setSingleTxDetails(action.tx)
            is WalletAction.BuildAndSignPsbt -> buildAndSignPsbt(action.address, action.amount, action.feeRate)
        }
    }

    private fun sync() {
        if (isOnline) {
            _walletState.update { it.copy(currentlySyncing = true) }

            viewModelScope.launch(Dispatchers.IO) {
                val minDurationMillis = 3000L

                val elapsed = measureTimeMillis {
                    updateBalance()
                    syncTransactionHistory()
                }
                val remaining = minDurationMillis - elapsed
                if (remaining > 0) delay(remaining)

                _walletState.update { it.copy(currentlySyncing = false) }
            }
        }
    }

    // TODO: Not sure about this getApplication() call
    private fun updateNetworkStatus(context: Context = getApplication()): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)

        val isOnlineNow: Boolean = if (capabilities != null) {
            when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                else -> false
            }
        } else {
            false
        }
        Log.i(TAG, "Updating online status to $isOnlineNow")
        return isOnlineNow
    }

    private fun updateBalance(firstSync: Boolean = false) {
        Log.i(TAG, "Updating balance...")
        Wallet.sync()
        val balance = Wallet.getBalance()
        Log.i(TAG, "Balance updated to: $balance")
        _walletState.update { it.copy(balance = balance) }
    }

    private fun requestCoins() {
        val address = Wallet.getLastUnusedAddress().address
        val faucetUrl: String = BuildConfig.FAUCET_URL
        val faucetToken: String = BuildConfig.FAUCET_TOKEN
        Log.i(TAG, "############################################")
        // Log.i(TAG, "Calling server with url: $faucetUrl")
        // Log.i(TAG, "Calling server with username: $faucetUsername")
        // Log.i(TAG, "Calling server with password: $faucetPassword")
        Log.i(TAG, "############################################")
        // val faucetPassword: String = "password" // Use for testing failed requests

        val faucetService = FaucetService()
        viewModelScope.launch {
            val response = withContext(Dispatchers.IO) {
                faucetService.callTatooineFaucet(
                    address = address.toString(),
                    faucetUrl = faucetUrl,
                    faucetToken = faucetToken,
                )
            }

            when (response) {
                is FaucetCall.Success -> {
                    WalletRepository.userHasClaimedFaucetCoins()
                    Log.i(
                        TAG,
                        "Faucet call succeeded with status: ${response.status}, description: ${response.description}"
                    )
                }

                is FaucetCall.Error -> {
                    sendMessageToUi(MessageType.Error, "Status ${response.status}, ${response.description}")
                    Log.i(TAG, "Faucet call failed with status:${response.status}, description:${response.description}")
                }

                is FaucetCall.ExceptionThrown -> {
                    Log.i(TAG, "Faucet call threw an exception: ${response.exception}")
                }
            }
            delay(1000)
            _walletState.update { it.copy(userCanRequestFaucetCoins = false) }
            sync()
        }
    }

    private fun sendMessageToUi(type: MessageType, message: String) {
        _walletState.update { it.copy(messageForUi = Pair(type, message)) }
    }

    private fun updateSendAddress(address: String) {
        Log.i(TAG, "Updating send address to $address")
        _walletState.update { it.copy(sendAddress = address) }
    }

    private fun broadcastTransaction(tx: Transaction) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                try {
                    Wallet.broadcast(tx)
                } catch (e: Throwable) {
                    Log.i(TAG, "Broadcast error: ${e.message}")
                    "Error: ${e.message}"
                }
            }
            sync()
        }
    }

    private fun uiMessageDelivered() {
        _walletState.update { it.copy(messageForUi = null) }
    }

    private fun firstAutoSync() {
        viewModelScope.launch {
            delay(1000)
            sync()
        }
    }

    private fun syncTransactionHistory() {
        val txHistory = Wallet.listTransactions()
        Log.i(TAG, "Transaction history synced, number of transactions: ${txHistory.size}")
        _walletState.update { it.copy(transactions = txHistory) }
    }

    private fun setSingleTxDetails(tx: String) {
        Wallet.getTransaction(tx)?.let {
            singleTxDetails = it
        }
    }

    fun getSingleTxDetails(): TransactionDetails? {
        return singleTxDetails
    }

    private fun buildAndSignPsbt(address: String, amount: Amount, feeRate: FeeRate) {
        val psbt = Wallet.createPsbt(address, amount, feeRate)
        Wallet.sign(psbt)
        val fee = psbt.fee()
        val tx: Transaction = psbt.extractTx()
        _walletState.update { it.copy(txAndFee = Pair(tx, Amount.fromSat(fee))) }
    }
}
