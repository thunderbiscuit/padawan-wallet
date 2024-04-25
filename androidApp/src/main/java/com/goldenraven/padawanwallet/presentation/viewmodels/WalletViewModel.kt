/*
 * Copyright 2020-2023 thunderbiscuit and contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE file.
 */

package com.goldenraven.padawanwallet.presentation.viewmodels

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import android.widget.Toast
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.goldenraven.padawanwallet.BuildConfig
import com.goldenraven.padawanwallet.R
import com.goldenraven.padawanwallet.domain.wallet.Wallet
import com.goldenraven.padawanwallet.domain.wallet.WalletRepository
import com.goldenraven.padawanwallet.domain.tx.Tx
import com.goldenraven.padawanwallet.domain.tx.TxDao
import com.goldenraven.padawanwallet.domain.tx.TxDatabase
import com.goldenraven.padawanwallet.domain.tx.TxRepository
import com.goldenraven.padawanwallet.padawankmp.FaucetCall
import com.goldenraven.padawanwallet.padawankmp.FaucetService
import com.goldenraven.padawanwallet.utils.SatoshisIn
import com.goldenraven.padawanwallet.utils.SatoshisOut
import com.goldenraven.padawanwallet.utils.isPayment
import com.goldenraven.padawanwallet.utils.netSendWithoutFees
import com.goldenraven.padawanwallet.utils.timestampToString
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.bitcoindevkit.AddressInfo
import org.bitcoindevkit.PartiallySignedTransaction

private const val TAG = "WalletViewModel"

class WalletViewModel(
    application: Application,
) : AndroidViewModel(application) {
    val readAllData: MutableStateFlow<List<Tx>> = MutableStateFlow<List<Tx>>(emptyList<Tx>())
    private val repository: TxRepository
    var openFaucetDialog: MutableState<Boolean> = mutableStateOf(false)

    private var _balance: MutableStateFlow<ULong> = MutableStateFlow(0u)
    val balance: StateFlow<ULong>
        get() = _balance

    private var _address: MutableStateFlow<String> = MutableStateFlow("1234")
    val address: StateFlow<String>
        get() = _address

    var QRState: MutableStateFlow<QRUIState> = MutableStateFlow(QRUIState.NoQR)

    private val _isRefreshing: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean>
        get() = _isRefreshing.asStateFlow()

    var isOnline: MutableStateFlow<Boolean> = MutableStateFlow(false)

    init {
        Log.i(TAG, "The WalletScreen viewmodel is being initialized...")

        val txDao: TxDao = TxDatabase.getDatabase(application).txDao()
        repository = TxRepository(txDao)
        viewModelScope.launch {
            repository.readAllData
                .collect { result ->
                    readAllData.value = result
                }
        }

        updateConnectivityStatus(application)

        // app will sync on initialization of the viewmodel after an 4 seconds delay
        viewModelScope.launch {
            delay(4000)
            refresh(application)
        }
    }

    // Faucet Code
    fun onPositiveDialogClick() {
        requestTestnetCoins(getLastUnusedAddress())
        faucetCallDone()
        openFaucetDialog.value = false
    }

    fun onNegativeDialogClick() {
        openFaucetDialog.value = false
    }

    private fun faucetCallDone() {
        WalletRepository.faucetCallDone()
    }

    private fun getLastUnusedAddress(): AddressInfo {
        val address = Wallet.getLastUnusedAddress()
        _address.value = address.address
        return address
    }

    fun updateLastUnusedAddress() {
        viewModelScope.launch {
            QRState.value = QRUIState.Loading
            val address = Wallet.getLastUnusedAddress().address
            delay(800)
            QRState.value = QRUIState.QR
            _address.value = address
        }
    }

    private suspend fun updateBalance() {
        Wallet.sync()
        withContext(Dispatchers.Main) {
            _balance.value = Wallet.getBalance()
        }
    }

    // Refreshing & Syncing
    fun refresh(context: Context) {
        val pendingString = context.getString(R.string.pending)
        if (isOnline.value) {
            // if (!Wallet.blockchainIsInitialized()) { Wallet.createBlockchain() }
            viewModelScope.launch(Dispatchers.IO) {
                _isRefreshing.value = true
                updateBalance()
                syncTransactionHistory(pendingString)
            }.invokeOnCompletion {
                 _isRefreshing.value = false
            }
        } else {
            Toast.makeText(
                context,
                context.getString(R.string.no_internet_access), Toast.LENGTH_LONG
            ).show()
        }
    }

    private fun syncTransactionHistory(pendingString: String) {
        val txHistory = Wallet.listTransactions()
        Log.i(TAG,"Transactions history, number of transactions: ${txHistory.size}")

        for (tx in txHistory) {
            // val details = when (tx.confirmationTime) {
            //     null -> tx.details
            //     is BlockTime -> tx.details
            // }
            var valueIn = 0
            var valueOut = 0
            val satoshisIn = SatoshisIn(tx.received.toInt())
            val satoshisOut = SatoshisOut(tx.sent.toInt())
            val isPayment = isPayment(satoshisOut, satoshisIn)
            when (isPayment) {
                true -> {
                    valueOut = netSendWithoutFees(
                        txSatsOut = satoshisOut,
                        txSatsIn = satoshisIn,
                        fees = tx.fee?.toInt() ?: 0
                    )
                }
                false -> {
                    valueIn = tx.received.toInt()
                }
            }
            val time: String = when (tx.confirmationTime) {
                null -> pendingString
                else -> tx.confirmationTime?.timestamp?.timestampToString() ?: pendingString
            }
            val height: UInt = when (tx.confirmationTime) {
                null -> 100_000_000u
                else -> tx.confirmationTime?.height ?: 100_000_000u
            }
            val transaction = Tx(
                txid = tx.txid,
                date = time,
                valueIn = valueIn,
                valueOut = valueOut,
                fees = tx.fee?.toInt() ?: 0,
                isPayment = isPayment,
                height = height.toInt()
            )
            addTx(transaction)
        }
    }

    private fun addTx(tx: Tx) {
        viewModelScope.launch(Dispatchers.IO) {
            Log.i(TAG, "Adding transaction to DB: $tx")
            repository.addTx(tx)
        }
    }

    fun updateConnectivityStatus(context: Context) {
        Log.i(TAG, "Updating connectivity status...")
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)

        val onlineStatus: Boolean = if (capabilities != null) {
            Log.i(TAG, "updateConnectivityStatus function returned $capabilities")
            when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> {
                    Log.i(TAG, "Network capabilities: TRANSPORT_WIFI")
                    true
                }
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> {
                    Log.i(TAG, "Network capabilities: TRANSPORT_CELLULAR")
                    true
                }
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> {
                    Log.i(TAG, "Network capabilities: TRANSPORT_ETHERNET")
                    true
                }
                else -> false
            }
        } else {
            false
        }
        Log.i(TAG, "Updating online status to $onlineStatus")
        isOnline.value = onlineStatus
    }

    fun broadcastTransaction(
        psbt: PartiallySignedTransaction,
        snackbarHostState: SnackbarHostState,
        successMessage: String
    ) {
            val snackbarMsg: String = try {
                Wallet.sign(psbt)
                Wallet.broadcast(psbt)
                successMessage
            } catch (e: Throwable) {
                Log.i(TAG, "Broadcast error: ${e.message}")
                "Error: ${e.message}"
            }
            viewModelScope.launch {
                snackbarHostState.showSnackbar(message = snackbarMsg, duration = SnackbarDuration.Short)
            }
    }

    private fun requestTestnetCoins(addressInfo: AddressInfo) {
        val faucetUrl: String = BuildConfig.FAUCET_URL
        val faucetUsername: String = BuildConfig.FAUCET_USERNAME
        val faucetPassword: String = BuildConfig.FAUCET_PASSWORD
        // val faucetPassword: String = "password" // Use for testing failed requests

        val faucetService = FaucetService()
        viewModelScope.launch {
            val response = faucetService.callTatooineFaucet(
                addressInfo.address,
                faucetUrl,
                faucetUsername,
                faucetPassword
            )
            when (response) {
                is FaucetCall.Success -> {
                    WalletRepository.faucetCallDone()
                    Log.i(TAG, "Faucet call succeeded with status: ${response.status}, description: ${response.description}")
                }

                is FaucetCall.Error -> {
                    Log.i(TAG, "Faucet call failed with status:${response.status}, description:${response.description}")
                }

                is FaucetCall.ExceptionThrown -> {
                    Log.i(TAG, "Faucet call threw an exception: ${response.exception}")
                }
            }
        }
    }
}

sealed class QRUIState {
    object NoQR : QRUIState()
    object Loading : QRUIState()
    object QR : QRUIState()
}

enum class CurrencyType {
    SATS,
    BTC,
}
