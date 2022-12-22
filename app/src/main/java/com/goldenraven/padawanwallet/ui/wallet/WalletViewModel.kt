/*
 * Copyright 2020-2022 thunderbiscuit and contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE file.
 */

package com.goldenraven.padawanwallet.ui.wallet

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import android.widget.Toast
import androidx.compose.material.SnackbarDuration
import androidx.compose.material.SnackbarHostState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.goldenraven.padawanwallet.BuildConfig
import com.goldenraven.padawanwallet.data.*
import com.goldenraven.padawanwallet.data.tx.Tx
import com.goldenraven.padawanwallet.data.tx.TxDao
import com.goldenraven.padawanwallet.data.tx.TxDatabase
import com.goldenraven.padawanwallet.data.tx.TxRepository
import com.goldenraven.padawanwallet.utils.*
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.auth.*
import io.ktor.client.plugins.auth.providers.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.content.*
import io.ktor.http.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.bitcoindevkit.AddressInfo
import org.bitcoindevkit.PartiallySignedTransaction

private const val TAG = "WalletViewModel"

class WalletViewModel(
    application: Application,
) : AndroidViewModel(application) {
    val readAllData: LiveData<List<Tx>>
    private val repository: TxRepository
    // var openFaucetDialog: MutableState<Boolean> = mutableStateOf(!didWeOfferFaucet())
    var openFaucetDialog: MutableState<Boolean> = mutableStateOf(false)

    private var _balance: MutableLiveData<ULong> = MutableLiveData(0u)
    val balance: LiveData<ULong>
        get() = _balance

    private var _address: MutableLiveData<String> = MutableLiveData("No address yet")
    val address: LiveData<String>
        get() = _address

    private val _isRefreshing: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean>
        get() = _isRefreshing.asStateFlow()

    init {
        Log.i(TAG, "The WalletScreen viewmodel is being initialized...")
        val txDao: TxDao = TxDatabase.getDatabase(application).txDao()
        repository = TxRepository(txDao)
        readAllData = repository.readAllData

        if (isOnline(context = application) && !Wallet.blockchainIsInitialized()) {
            Wallet.createBlockchain()
        }

        // app will sync on initialization of the viewmodel
        viewModelScope.launch {
            delay(8000)
            refresh(application)
        }
    }

    // Faucet Code
    fun onPositiveDialogClick() {
        // faucetOfferWasMade()
        callTatooineFaucet(getLastUnusedAddress())
        faucetCallDone()
        openFaucetDialog.value = false
    }

    fun onNegativeDialogClick() {
        // faucetOfferWasMade()
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

    fun getLastUnusedAddress(): AddressInfo {
        val address = Wallet.getLastUnusedAddress()
        _address.setValue(address.address)
        return address
    }

    private fun updateBalance() {
        Wallet.sync()
        _balance.value = Wallet.getBalance()
    }

    // Refreshing & Syncing
    fun refresh(context: Context) {
        if (isOnline(context = context)) {
            if (!Wallet.blockchainIsInitialized()) { Wallet.createBlockchain() }
            // This doesn't handle multiple 'refreshing' tasks, don't use this
            viewModelScope.launch {
                // A fake 2 second 'refresh'
                _isRefreshing.emit(true)
                updateBalance()
                syncTransactionHistory()
                delay(300)
                _isRefreshing.emit(false)
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

    // Internet connectivity
    // fun isOnline(): Boolean {
    fun isOnline(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        if (capabilities != null) {
            Log.i(TAG, "isOnline function returned $capabilities")
            when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> {
                    Log.i(TAG, "NetworkCapabilities.TRANSPORT_CELLULAR")
                    return true
                }
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> {
                    Log.i(TAG, "NetworkCapabilities.TRANSPORT_WIFI")
                    return true
                }
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> {
                    Log.i(TAG, "NetworkCapabilities.TRANSPORT_ETHERNET")
                    return true
                }
            }
        }
        return false
    }

    fun broadcastTransaction(
        psbt: PartiallySignedTransaction,
        snackbarHostState: SnackbarHostState,
        // context: Context,
    ) {
        // if (!isOnline(context = context)) {
        //     Toast.makeText(context, "You currently don't have internet access!", Toast.LENGTH_SHORT).show()
        // } else {
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

        // used to be a lifecycleScope.launch because it was in a fragment
        // now simply using a background thread untied to the lifecycle of the composable
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

enum class CurrencyType {
    SATS,
    BTC,
}
