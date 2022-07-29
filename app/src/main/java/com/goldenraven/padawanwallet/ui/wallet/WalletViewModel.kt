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
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.goldenraven.padawanwallet.BuildConfig
import com.goldenraven.padawanwallet.data.*
import com.goldenraven.padawanwallet.utils.*
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.features.auth.*
import io.ktor.client.features.auth.providers.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.content.*
import io.ktor.http.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.bitcoindevkit.AddressInfo
import org.bitcoindevkit.Transaction

class WalletViewModel(
    application: Application,
) : AndroidViewModel(application) {
    val readAllData: LiveData<List<Tx>>
    private val repository: TxRepository
    var openFaucetDialog: MutableState<Boolean> = mutableStateOf(!didWeOfferFaucet())

    private var _balance: MutableLiveData<ULong> = MutableLiveData(0u)
    val balance: LiveData<ULong>
        get() = _balance

    private val _isRefreshing: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean>
        get() = _isRefreshing.asStateFlow()

    init {
        Log.i("WalletScreen", "The WalletScreen viewmodel is being initialized...")
        val txDao: TxDao = TxDatabase.getDatabase(application).txDao()
        repository = TxRepository(txDao)
        readAllData = repository.readAllData
    }

    // Faucet Code
    fun onPositiveDialogClick() {
        faucetOfferWasMade()
        callTatooineFaucet(getLastUnusedAddress())
        faucetCallDone()
        openFaucetDialog.value = false
    }

    fun onNegativeDialogClick() {
        faucetOfferWasMade()
        openFaucetDialog.value = false
    }

    private fun didWeOfferFaucet(): Boolean {
        val faucetOfferDone = Repository.didWeOfferFaucet()
        Log.i("WalletScreen", "We have already asked if they wanted testnet coins: $faucetOfferDone")
        return faucetOfferDone
    }

    private fun faucetOfferWasMade() {
        Log.i("WalletScreen", "The offer to call the faucet was made")
        Repository.offerFaucetDone()
    }

    private fun faucetCallDone() {
        Repository.faucetCallDone()
    }

    // Wallet Code
    private fun getLastUnusedAddress(): AddressInfo {
        return Wallet.getLastUnusedAddress()
    }

    private fun updateBalance() {
        Wallet.sync()
        _balance.value = Wallet.getBalance()
    }

    // Refreshing & Syncing
    fun refresh(context: Context) {
        if (isOnline(context = context)) {
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
            Toast.makeText(context, "No Internet Access!", Toast.LENGTH_SHORT)
        }
    }

    private fun syncTransactionHistory() {
        val txHistory = Wallet.listTransactions()
        Log.i("WalletScreen","Transactions history, number of transactions: ${txHistory.size}")

        for (tx in txHistory) {
            val details = when (tx) {
                is Transaction.Confirmed -> tx.details
                is Transaction.Unconfirmed -> tx.details
            }
            var valueIn = 0
            var valueOut = 0
            val satoshisIn = SatoshisIn(details.received.toInt())
            val satoshisOut = SatoshisOut(details.sent.toInt())
            val isPayment = isPayment(satoshisOut, satoshisIn)
            when (isPayment) {
                true -> {
                    valueOut = netSendWithoutFees(
                        txSatsOut = satoshisOut,
                        txSatsIn = satoshisIn,
                        fees = details.fee?.toInt() ?: 0
                    )
                }
                false -> {
                    valueIn = details.received.toInt()
                }
            }
            val time: String = when (tx) {
                is Transaction.Confirmed -> tx.confirmation.timestamp.timestampToString()
                else -> "Pending"
            }
            val height: UInt = when (tx) {
                is Transaction.Confirmed -> tx.confirmation.height
                else -> 100_000_000u
            }
            val transaction = Tx(
                txid = details.txid,
                date = time,
                valueIn = valueIn,
                valueOut = valueOut,
                fees = details.fee?.toInt() ?: 0,
                isPayment = isPayment,
                height = height.toInt()
            )
            addTx(transaction)
        }
    }

    private fun addTx(tx: Tx) {
        viewModelScope.launch(Dispatchers.IO) {
            Log.i("WalletScreen", "Adding transaction to DB: $tx")
            repository.addTx(tx)
        }
    }

    // Internet connectivity
    fun isOnline(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        if (capabilities != null) {
            when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_CELLULAR")
                    return true
                }
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_WIFI")
                    return true
                }
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_ETHERNET")
                    return true
                }
            }
        }
        return false
    }
}

private fun callTatooineFaucet(address: AddressInfo) {
    val faucetUrl: String = BuildConfig.FAUCET_URL
    val faucetUsername: String = BuildConfig.FAUCET_USERNAME
    val faucetPassword: String = BuildConfig.FAUCET_PASSWORD

    // used to be a lifecycleScope.launch because it was in a fragment
    // now simply using a background thread untied to the lifecycle of the composable
    val faucetScope = CoroutineScope(Dispatchers.IO)
    faucetScope.launch {
        val ktorClient = HttpClient(CIO) {
            install(Auth) {
                basic {
                    username = faucetUsername
                    password = faucetPassword
                }
            }
        }

        Log.i("WalletScreen","API call to Tatooine will request coins at $address")
        try {
            val response: HttpResponse = ktorClient.post(faucetUrl) {
                body = TextContent(address.address, ContentType.Text.Plain)
            }
            Repository.faucetCallDone()
            Log.i("WalletScreen","API call to Tatooine was performed. Response is ${response.status}, ${response.readText()}")
        } catch (cause: Throwable) {
            Log.i("WalletScreen","Tatooine call failed: $cause")
        }
        ktorClient.close()
    }
}