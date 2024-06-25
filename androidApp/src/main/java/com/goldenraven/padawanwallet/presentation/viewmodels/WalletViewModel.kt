package com.goldenraven.padawanwallet.presentation.viewmodels

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
import com.goldenraven.padawanwallet.BuildConfig
import com.goldenraven.padawanwallet.domain.bitcoin.Wallet
import com.goldenraven.padawanwallet.domain.bitcoin.WalletRepository
import com.goldenraven.padawanwallet.domain.tx.Tx
import com.goldenraven.padawanwallet.domain.tx.TxDao
import com.goldenraven.padawanwallet.domain.tx.TxDatabase
import com.goldenraven.padawanwallet.domain.tx.TxRepository
import com.goldenraven.padawanwallet.padawankmp.FaucetCall
import com.goldenraven.padawanwallet.padawankmp.FaucetService
import com.goldenraven.padawanwallet.presentation.viewmodels.mvi.MessageType
import com.goldenraven.padawanwallet.presentation.viewmodels.mvi.WalletAction
import com.goldenraven.padawanwallet.presentation.viewmodels.mvi.WalletState
import com.goldenraven.padawanwallet.utils.SatoshisIn
import com.goldenraven.padawanwallet.utils.SatoshisOut
import com.goldenraven.padawanwallet.utils.isPayment
import com.goldenraven.padawanwallet.utils.netSendWithoutFees
import com.goldenraven.padawanwallet.utils.timestampToString
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.bitcoindevkit.PartiallySignedTransaction

private const val TAG = "WalletViewModel"

class WalletViewModel(application: Application) : AndroidViewModel(application) {
    private var txList: List<Tx> by mutableStateOf(emptyList())
    private var isOnline: Boolean by mutableStateOf(false)
    private var qrCode: String? by mutableStateOf(null)
    private val repository: TxRepository

    init {
        isOnline = updateNetworkStatus(application)
        firstSync()
        val txDao: TxDao = TxDatabase.getDatabase(application).txDao()
        repository = TxRepository(txDao)
        viewModelScope.launch {
            repository.readAllData
                .collect { result ->
                    txList = result
                }
        }
    }

    var walletState: WalletState by mutableStateOf(WalletState(0u, txList, isOnline, currentlySyncing = false))
        private set

    fun onAction(action: WalletAction) {
        when (action) {
            is WalletAction.Sync -> sync()
            is WalletAction.RequestCoins -> requestCoins()
            is WalletAction.CheckNetworkStatus -> updateNetworkStatus()
            is WalletAction.QRCodeScanned -> updateQRCode(action.address)
            is WalletAction.Broadcast -> broadcastTransaction(action.tx)
            is WalletAction.UiMessageDelivered -> uiMessageDelivered()
        }
    }

    private fun sync() {
        if (isOnline) {
            walletState = walletState.copy(currentlySyncing = true)

            viewModelScope.launch(Dispatchers.IO) {
                updateBalance()
                syncTransactionHistory()
                walletState = walletState.copy(currentlySyncing = false)
            }
        }
        // else {
        //     Toast.makeText(
        //         context,
        //         context.getString(R.string.no_internet_access), Toast.LENGTH_LONG
        //     ).show()
        // }
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

    private fun updateBalance() {
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
        // val faucetPassword: String = "password" // Use for testing failed requests

        val faucetService = FaucetService()
        viewModelScope.launch {
            val response = faucetService.callTatooineFaucet(
                address,
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
                    sendMessageToUi(MessageType.Error, "Status ${response.status}, ${response.description}")
                    Log.i(TAG, "Faucet call failed with status:${response.status}, description:${response.description}")
                }
                is FaucetCall.ExceptionThrown -> {
                    Log.i(TAG, "Faucet call threw an exception: ${response.exception}")
                }
            }
            delay(4000)
            sync()
        }
    }

    private fun sendMessageToUi(type: MessageType, message: String) {
        walletState = walletState.copy(messageForUi = Pair(type, message))
    }

    private fun updateQRCode(address: String) {
        qrCode = address
    }

    private fun broadcastTransaction(psbt: PartiallySignedTransaction) {
        try {
            Wallet.sign(psbt)
            Wallet.broadcast(psbt)
        } catch (e: Throwable) {
            Log.i(TAG, "Broadcast error: ${e.message}")
            "Error: ${e.message}"
        }
    }

    private fun uiMessageDelivered() {
        walletState = walletState.copy(messageForUi = null)
    }

    private fun firstSync() {
        viewModelScope.launch {
            delay(4000)
            sync()
        }
    }

    private fun syncTransactionHistory() {
        val txHistory = Wallet.listTransactions()
        Log.i(TAG, "Transactions history, number of transactions: ${txHistory.size}")

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
                null -> "pending"
                else -> tx.confirmationTime?.timestamp?.timestampToString() ?: "pending"
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
}
