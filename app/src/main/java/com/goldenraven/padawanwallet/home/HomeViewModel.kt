/*
 * Copyright 2020 thunderbiscuit and contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE file.
 */

package com.goldenraven.padawanwallet.home

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.goldenraven.padawanwallet.Repository
import com.goldenraven.padawanwallet.Wallet
import timber.log.Timber

class HomeViewModel(application: Application) : AndroidViewModel(application) {

    val app: Application = application

    public var balance: MutableLiveData<Long> = MutableLiveData(0)
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

    public fun retrieveDoneTutorials() {
        val editor = app.getSharedPreferences("current_wallet", Context.MODE_PRIVATE)!!
        val newTutorialsDone = Repository.loadTutorialsDone(editor)
        tutorialsDone.postValue(newTutorialsDone)
    }

    public fun updateBalance() {
        Wallet.sync(100)
        val newBalance = Wallet.getBalance()
        balance.postValue(newBalance)
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
        val editor = app.getSharedPreferences("current_wallet", Context.MODE_PRIVATE)!!.edit()
        when (tutorialNumber) {
            1 -> {
                Repository.updateTutorialsDone(1, editor)
                newTutorialsDoneMap?.set("e1", true)
                tutorialsDone.postValue(newTutorialsDoneMap)
            }
            2 -> {
                Repository.updateTutorialsDone(2, editor)
                newTutorialsDoneMap?.set("e2", true)
                tutorialsDone.postValue(newTutorialsDoneMap)
            }
            3 -> {
                Repository.updateTutorialsDone(3, editor)
                newTutorialsDoneMap?.set("e3", true)
                tutorialsDone.postValue(newTutorialsDoneMap)
            }
            4 -> {
                Repository.updateTutorialsDone(4, editor)
                newTutorialsDoneMap?.set("e4", true)
                tutorialsDone.postValue(newTutorialsDoneMap)
            }
            5 -> {
                Repository.updateTutorialsDone(5, editor)
                newTutorialsDoneMap?.set("e5", true)
                tutorialsDone.postValue(newTutorialsDoneMap)
            }
            6 -> {
                Repository.updateTutorialsDone(6, editor)
                newTutorialsDoneMap?.set("e6", true)
                tutorialsDone.postValue(newTutorialsDoneMap)
            }
            7 -> {
                Repository.updateTutorialsDone(7, editor)
                newTutorialsDoneMap?.set("e7", true)
                tutorialsDone.postValue(newTutorialsDoneMap)
            }
            8 -> {
                Repository.updateTutorialsDone(8, editor)
                newTutorialsDoneMap?.set("e8", true)
                tutorialsDone.postValue(newTutorialsDoneMap)
            }
            else -> Timber.i("[PADAWANLOGS] Tutorial number was invalid")
        }
    }
}
