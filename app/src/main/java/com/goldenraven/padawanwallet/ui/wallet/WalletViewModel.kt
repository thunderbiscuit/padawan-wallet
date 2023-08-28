/*
 * Copyright 2020-2023 thunderbiscuit and contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE file.
 */

package com.goldenraven.padawanwallet.ui.wallet

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
import com.goldenraven.padawanwallet.data.Wallet
import com.goldenraven.padawanwallet.data.WalletRepository
import com.goldenraven.padawanwallet.data.tx.Tx
import com.goldenraven.padawanwallet.data.tx.TxDao
import com.goldenraven.padawanwallet.data.tx.TxDatabase
import com.goldenraven.padawanwallet.data.tx.TxRepository
import com.goldenraven.padawanwallet.utils.SatoshisIn
import com.goldenraven.padawanwallet.utils.SatoshisOut
import com.goldenraven.padawanwallet.utils.isPayment
import com.goldenraven.padawanwallet.utils.netSendWithoutFees
import com.goldenraven.padawanwallet.utils.timestampToString
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BasicAuthCredentials
import io.ktor.client.plugins.auth.providers.basic
import io.ktor.client.request.request
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpMethod
import io.ktor.http.content.TextContent
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

    private var _address: MutableStateFlow<String> = MutableStateFlow("No address yet")
    val address: StateFlow<String>
        get() = _address

    var QRState: MutableStateFlow<QRUIState> = MutableStateFlow(QRUIState.NoQR)

    private val _isRefreshing: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean>
        get() = _isRefreshing.asStateFlow()

    var isOnlineVariable: MutableStateFlow<Boolean> = MutableStateFlow(false)

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

        if (isOnlineVariable.value == true && !Wallet.blockchainIsInitialized()) {
            Wallet.createBlockchain()
        }

        updateConnectivityStatus(application)

        // app will sync on initialization of the viewmodel
        viewModelScope.launch {
            delay(8000)
            refresh(application)
        }
    }

    // Faucet Code
    fun onPositiveDialogClick() {
        callTatooineFaucet(getLastUnusedAddress())
        faucetCallDone()
        openFaucetDialog.value = false
    }

    fun onNegativeDialogClick() {
        openFaucetDialog.value = false
    }

    // private fun didWeOfferFaucet(): Boolean {
    //     val faucetOfferDone = WalletRepository.didWeOfferFaucet()
    //     Log.i(TAG, "We have already asked if they wanted testnet coins: $faucetOfferDone")
    //     return faucetOfferDone
    // }

    // private fun faucetOfferWasMade() {
    //     Log.i(TAG, "The offer to call the faucet was made")
    //     WalletRepository.offerFaucetDone()
    // }

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
        if (isOnlineVariable.value == true) {
            if (!Wallet.blockchainIsInitialized()) { Wallet.createBlockchain() }
            viewModelScope.launch(Dispatchers.IO) {
                _isRefreshing.value = true
                updateBalance()
                syncTransactionHistory()
            }.invokeOnCompletion {
                 _isRefreshing.value = false
            }
        } else {
            Toast.makeText(context, "No Internet Access!", Toast.LENGTH_LONG).show()
        }
    }

    private fun syncTransactionHistory() {
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
                null -> "Pending"
                else -> tx.confirmationTime?.timestamp?.timestampToString() ?: "Pending"
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
        isOnlineVariable.value = onlineStatus
    }

    fun broadcastTransaction(
        psbt: PartiallySignedTransaction,
        snackbarHostState: SnackbarHostState,
    ) {
            val snackbarMsg: String = try {
                Wallet.sign(psbt)
                Wallet.broadcast(psbt)
                "Transaction was broadcast successfully"
            } catch (e: Throwable) {
                Log.i(TAG, "Broadcast error: ${e.message}")
                "Error: ${e.message}"
            }
            viewModelScope.launch {
                snackbarHostState.showSnackbar(message = snackbarMsg, duration = SnackbarDuration.Short)
            }
    }

    private fun callTatooineFaucet(addressInfo: AddressInfo) {
        val faucetUrl: String = BuildConfig.FAUCET_URL
        val faucetUsername: String = BuildConfig.FAUCET_USERNAME
        val faucetPassword: String = BuildConfig.FAUCET_PASSWORD

        viewModelScope.launch {
            val ktorClient = HttpClient(CIO) {
                install(Auth) {
                    basic {
                        credentials {
                            BasicAuthCredentials(
                                username = faucetUsername,
                                password = faucetPassword
                            )
                        }
                    }
                }
            }

            Log.i(TAG, "API call to Tatooine will request coins at $addressInfo")
            try {
                val response: HttpResponse = ktorClient.request(faucetUrl) {
                    method = HttpMethod.Post
                    setBody(TextContent(addressInfo.address, ContentType.Text.Plain))
                }
                WalletRepository.faucetCallDone()
                Log.i(
                    TAG,
                    "API call to Tatooine was performed. Response is ${response.status}, ${response.bodyAsText()}"
                )
            } catch (cause: Throwable) {
                Log.i(TAG, "Tatooine call failed: $cause")
            }
            ktorClient.close()
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
