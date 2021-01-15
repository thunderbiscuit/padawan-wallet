/*
 * Copyright 2020 thunderbiscuit and contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE file.
 */

package com.goldenraven.padawanwallet.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.goldenraven.padawanwallet.PadawanWalletApplication
import timber.log.Timber

class HomeViewModel(application: Application) : AndroidViewModel(application) {

    val app = application as PadawanWalletApplication

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

    public fun updateBalance() {
        app.sync(100)
        val newBalance = app.getBalance()
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
        when (tutorialNumber) {
            1 -> {
                newTutorialsDoneMap?.set("e1", true)
                tutorialsDone.postValue(newTutorialsDoneMap)
            }
            2 -> {
                newTutorialsDoneMap?.set("e2", true)
                tutorialsDone.postValue(newTutorialsDoneMap)
            }
            3 -> {
                newTutorialsDoneMap?.set("e3", true)
                tutorialsDone.postValue(newTutorialsDoneMap)
            }
            4 -> {
                newTutorialsDoneMap?.set("e4", true)
                tutorialsDone.postValue(newTutorialsDoneMap)
            }
            5 -> {
                newTutorialsDoneMap?.set("e5", true)
                tutorialsDone.postValue(newTutorialsDoneMap)
            }
            6 -> {
                newTutorialsDoneMap?.set("e6", true)
                tutorialsDone.postValue(newTutorialsDoneMap)
            }
            7 -> {
                newTutorialsDoneMap?.set("e7", true)
                tutorialsDone.postValue(newTutorialsDoneMap)
            }
            8 -> {
                newTutorialsDoneMap?.set("e8", true)
                tutorialsDone.postValue(newTutorialsDoneMap)
            }
            else -> Timber.i("[PADAWANLOGS] Tutorial number was invalid")
        }
    }
}
