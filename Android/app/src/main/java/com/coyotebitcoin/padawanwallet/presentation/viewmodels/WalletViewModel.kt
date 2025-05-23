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
import com.coyotebitcoin.padawanwallet.presentation.viewmodels.mvi.MessageType
import com.coyotebitcoin.padawanwallet.presentation.viewmodels.mvi.WalletAction
import com.coyotebitcoin.padawanwallet.presentation.viewmodels.mvi.WalletState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.bitcoindevkit.Amount
import org.bitcoindevkit.FeeRate
import org.bitcoindevkit.Transaction
import kotlin.system.measureTimeMillis

private const val TAG = "WalletViewModel"

class WalletViewModel(application: Application) : AndroidViewModel(application) {
    private var txList: List<TransactionDetails> by mutableStateOf(emptyList())
    private var isOnline: Boolean by mutableStateOf(false)
    private var sendAddress: String? by mutableStateOf(null)
    private var singleTxDetails: TransactionDetails? = null

    var walletState by mutableStateOf(
        WalletState(
            balance = 0u,
            transactions = txList,
            txAndFee = null,
            isOnline = isOnline,
            currentlySyncing = false
        )
    )
        private set

    init {
        isOnline = updateNetworkStatus(application)
        firstAutoSync()
        Log.i(TAG, "First sync")
        val txList: List<TransactionDetails> = Wallet.listTransactions()
        walletState = walletState.copy(transactions = txList, isOnline = isOnline)
    }


    fun onAction(action: WalletAction) {
        when (action) {
            is WalletAction.Sync -> sync()
            is WalletAction.RequestCoins -> requestCoins()
            is WalletAction.CheckNetworkStatus -> updateNetworkStatus()
            is WalletAction.QRCodeScanned -> updateSendAddress(action.address)
            is WalletAction.Broadcast -> broadcastTransaction(action.tx)
            is WalletAction.UiMessageDelivered -> uiMessageDelivered()
            is WalletAction.SeeSingleTx -> { setSingleTxDetails(action.tx) }
            is WalletAction.BuildAndSignPsbt -> { buildAndSignPsbt(action.address, action.amount, action.feeRate) }
        }
    }

    private fun sync() {
        if (isOnline) {
            walletState = walletState.copy(currentlySyncing = true)

            viewModelScope.launch(Dispatchers.IO) {
                val minDurationMillis = 3000L

                val elapsed = measureTimeMillis {
                    updateBalance()
                    syncTransactionHistory()
                }
                val remaining = minDurationMillis - elapsed
                if (remaining > 0) delay(remaining)

                walletState = walletState.copy(currentlySyncing = false)
            }
        }
    }

    // TODO: Not sure about this getApplication() call
    private fun updateNetworkStatus(context: Context = getApplication()): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)

        val isOnlineNow: Boolean = if (capabilities != null) {
            when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)     -> true
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
        walletState = walletState.copy(balance = balance)
    }

    private fun requestCoins() {
        val address = Wallet.getLastUnusedAddress().address
        val faucetUrl: String = BuildConfig.FAUCET_URL
        val faucetUsername: String = BuildConfig.FAUCET_USERNAME
        val faucetPassword: String = BuildConfig.FAUCET_PASSWORD
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
                    address.toString(),
                    faucetUrl,
                    faucetUsername,
                    faucetPassword
                )
            }

            when (response) {
                is FaucetCall.Success -> {
                    WalletRepository.faucetCallDone()
                    Log.i(TAG, "Faucet call succeeded with status: ${response.status}, description: ${response.description}")
                }
                is FaucetCall.Error -> {
                    sendMessageToUi(MessageType.Error, "Status ${response.status}, ${response.description}")
                    Log.i(TAG, "Faucet call failed with status:${response.status}, description:${response.description}")
                }
                is FaucetCall.ExceptionThrown -> {
                    Log.i(TAG, "Faucet call threw an exception: ${response.exception}")
                }
            }
            delay(2000)
            sync()
        }
    }

    private fun sendMessageToUi(type: MessageType, message: String) {
        walletState = walletState.copy(messageForUi = Pair(type, message))
    }

    private fun updateSendAddress(address: String) {
        Log.i(TAG, "Updating send address to $address")
        walletState = walletState.copy(sendAddress = address)
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
        walletState = walletState.copy(messageForUi = null)
    }

    private fun firstAutoSync() {
        viewModelScope.launch {
            delay(4000)
            sync()
        }
    }

    private fun syncTransactionHistory() {
        val txHistory = Wallet.listTransactions()
        Log.i(TAG, "Transaction history synced, number of transactions: ${txHistory.size}")
        walletState = walletState.copy(transactions = txHistory)
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
        walletState = walletState.copy(txAndFee = Pair(tx, Amount.fromSat(fee)))
    }
}
