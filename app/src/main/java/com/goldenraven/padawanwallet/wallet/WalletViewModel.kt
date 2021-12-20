/*
 * Copyright 2020 thunderbiscuit and contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE file.
 */

package com.goldenraven.padawanwallet.wallet

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.goldenraven.padawanwallet.data.*
import com.goldenraven.padawanwallet.utils.isSend
import com.goldenraven.padawanwallet.utils.netSendWithoutFees
import com.goldenraven.padawanwallet.utils.timestampToString
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.bitcoindevkit.Transaction


class WalletViewModel(application: Application) : AndroidViewModel(application) {

    val app: Application = application

    val readAllData: LiveData<List<Tx>>
    private val repository: TxRepository

    init {
        val txDao: TxDao = TxDatabase.getDatabase(application).txDao()
        repository = TxRepository(txDao)
        readAllData = repository.readAllData
    }

    public var balance: MutableLiveData<ULong> = MutableLiveData(0u)
    public var timestamp: MutableLiveData<String> = MutableLiveData("Pending")
    public var satoshiUnit: MutableLiveData<Boolean> = MutableLiveData(true)
    public var tutorialsDone: MutableLiveData<MutableMap<String, Boolean>> = MutableLiveData(
            mutableMapOf(
                    "e1" to false,
                    "e2" to false,
                    "e3" to false,
                    "e4" to false,
                    "e5" to false,
                    "e6" to false,
                    "e7" to false,
                    "e8" to false,
            )
    )

    public fun addTx(tx: Tx) {
        viewModelScope.launch(Dispatchers.IO) {
            Log.i("smallP", "tx: ${tx}")
            repository.addTx(tx)
        }
    }

    public fun retrieveDoneTutorials() {
        val newTutorialsDone = Repository.loadTutorialsDone()
        tutorialsDone.postValue(newTutorialsDone)
    }

    public fun updateBalance() {
        Wallet.sync(100u)
        val newBalance = Wallet.getBalance()
        Log.i("Padalogs","New balance: $newBalance")
        balance.postValue(newBalance)
    }

    public fun syncTransactionHistory() {
        val txHistory = Wallet.listTransactions()
        Log.i("Padalogs","Transactions history, number of transactions: ${txHistory.size}")
        for (tx in txHistory) {
            val details = when (tx) {
                is Transaction.Confirmed -> tx.details
                is Transaction.Unconfirmed -> tx.details
            }
            val isSend: Boolean = isSend(sent = details.sent.toInt(), received = details.received.toInt())
            var valueIn: Int = 0
            var valueOut: Int = 0
            when (isSend) {
                true -> {
                    valueOut = netSendWithoutFees(
                        txSatsOut = details.sent.toInt(),
                        txSatsIn = details.received.toInt(),
                        fees = details.fees?.toInt() ?: 0
                    )
                }
                false -> {
                    valueIn = details.received.toInt()
                }
            }
            val time : String = when (tx) {
                is Transaction.Confirmed -> tx.confirmation.timestamp.timestampToString()
                else -> "Pending"
            }
            val height : UInt = when (tx) {
                is Transaction.Confirmed -> tx.confirmation.height
                else -> 100_000_000u
            }
            val transaction: Tx = Tx(
                    txid = details.txid,
                    date = time,
                    valueIn = valueIn,
                    valueOut = valueOut,
                    fees = details.fees?.toInt() ?: 0,
                    isSend = isSend,
                    height = height.toInt()
            )
            addTx(transaction)
        }
    }

    public fun changeUnit() {
        if (satoshiUnit.value == true) {
            satoshiUnit.value = false
            balance.postValue(balance.value)
        } else {
            satoshiUnit.value = true
            balance.postValue(balance.value)
        }
    }

    public fun markAsDone(tutorialNumber: Int) {
        val newTutorialsDoneMap = tutorialsDone.value
        when (tutorialNumber) {
            1 -> {
                Repository.updateTutorialsDone(1)
                newTutorialsDoneMap?.set("e1", true)
                tutorialsDone.postValue(newTutorialsDoneMap)
            }
            2 -> {
                Repository.updateTutorialsDone(2)
                newTutorialsDoneMap?.set("e2", true)
                tutorialsDone.postValue(newTutorialsDoneMap)
            }
            3 -> {
                Repository.updateTutorialsDone(3)
                newTutorialsDoneMap?.set("e3", true)
                tutorialsDone.postValue(newTutorialsDoneMap)
            }
            4 -> {
                Repository.updateTutorialsDone(4)
                newTutorialsDoneMap?.set("e4", true)
                tutorialsDone.postValue(newTutorialsDoneMap)
            }
            5 -> {
                Repository.updateTutorialsDone(5)
                newTutorialsDoneMap?.set("e5", true)
                tutorialsDone.postValue(newTutorialsDoneMap)
            }
            6 -> {
                Repository.updateTutorialsDone(6)
                newTutorialsDoneMap?.set("e6", true)
                tutorialsDone.postValue(newTutorialsDoneMap)
            }
            7 -> {
                Repository.updateTutorialsDone(7)
                newTutorialsDoneMap?.set("e7", true)
                tutorialsDone.postValue(newTutorialsDoneMap)
            }
            8 -> {
                Repository.updateTutorialsDone(8)
                newTutorialsDoneMap?.set("e8", true)
                tutorialsDone.postValue(newTutorialsDoneMap)
            }
            else -> Log.i("Padalogs","Tutorial number was invalid")
        }
    }
}
