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
import com.goldenraven.padawanwallet.domain.bitcoin.ChainPosition
import com.goldenraven.padawanwallet.domain.bitcoin.TransactionDetails
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
import com.goldenraven.padawanwallet.utils.TxType
import com.goldenraven.padawanwallet.utils.netSendWithoutFees
import com.goldenraven.padawanwallet.utils.timestampToString
import com.goldenraven.padawanwallet.utils.txType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.rustbitcoin.bitcoin.Amount
import org.rustbitcoin.bitcoin.FeeRate
import org.bitcoindevkit.Transaction

private const val TAG = "WalletViewModel"

class WalletViewModel(application: Application) : AndroidViewModel(application) {
    private var txList: List<Tx> by mutableStateOf(emptyList())
    private var isOnline: Boolean by mutableStateOf(false)
    private var sendAddress: String? by mutableStateOf(null)
    private val repository: TxRepository
    private var singleTxDetails: TransactionDetails? = null

    init {
        isOnline = updateNetworkStatus(application)
        firstAutoSync()
        val txDao: TxDao = TxDatabase.getDatabase(application).txDao()
        repository = TxRepository(txDao)
        viewModelScope.launch {
            repository.readAllTxs.collect { result ->
                txList = result
                walletState = walletState.copy(transactions = txList)
            }
        }
    }

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
                updateBalance()
                syncTransactionHistory()
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
        Log.i(TAG, "Calling server with url: $faucetUrl")
        Log.i(TAG, "Calling server with username: $faucetUsername")
        Log.i(TAG, "Calling server with password: $faucetPassword")
        Log.i(TAG, "############################################")
        // val faucetPassword: String = "password" // Use for testing failed requests

        val faucetService = FaucetService()
        viewModelScope.launch {
            val response = faucetService.callTatooineFaucet(
                address.toString(),
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

    private fun updateSendAddress(address: String) {
        Log.i(TAG, "Updating send address to $address")
        walletState = walletState.copy(sendAddress = address)
    }

    private fun broadcastTransaction(tx: Transaction) {
        try {
            Wallet.broadcast(tx)
        } catch (e: Throwable) {
            Log.i(TAG, "Broadcast error: ${e.message}")
            "Error: ${e.message}"
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
        Log.i(TAG, "Transactions history, number of transactions: ${txHistory.size}")

        for (tx in txHistory) {
            var valueIn = 0uL
            var valueOut = 0uL
            val txType = txType(sent = tx.sent.toSat(), received = tx.received.toSat())
            Log.i(TAG, "Sent: ${tx.sent.toSat()}")
            Log.i(TAG, "Received: ${tx.received.toSat()}")
            Log.i(TAG, "Transaction type: $txType")
            when (txType) {
                TxType.PAYMENT -> {
                    valueOut = netSendWithoutFees(
                        txSatsOut = tx.sent.toSat(),
                        txSatsIn = tx.received.toSat(),
                        fee = tx.fee.toSat()
                    )
                }
                TxType.RECEIVE -> {
                    valueIn = tx.received.toSat()
                }
            }
            val time: String = when (tx.chainPosition) {
                is ChainPosition.Unconfirmed -> "pending"
                is ChainPosition.Confirmed -> tx.chainPosition.timestamp.timestampToString()
            }
            val height: UInt = when (tx.chainPosition) {
                is ChainPosition.Unconfirmed -> 100_000_000u
                is ChainPosition.Confirmed -> tx.chainPosition.height
            }
            val transaction = Tx(
                txid = tx.txid,
                date = time,
                valueIn = valueIn.toLong(),
                valueOut = valueOut.toLong(),
                fee = tx.fee.toSat().toLong(),
                isPayment = txType == TxType.PAYMENT,
                height = height.toInt()
            )
            addTx(transaction)
        }
        viewModelScope.launch {
            repository.readAllTxs.collect { result ->
                Log.i(TAG, "Updating transactions list with $result")
                txList = result
                walletState = walletState.copy(transactions = txList)
            }
        }
    }

    private fun addTx(tx: Tx) {
        viewModelScope.launch(Dispatchers.IO) {
            Log.i(TAG, "Adding transaction to DB: $tx")
            repository.addTx(tx)
        }
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
