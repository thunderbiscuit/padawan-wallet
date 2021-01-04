/*
 * Copyright 2020 thunderbiscuit and contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE file.
 */

package com.libertysoftware.padawanwallet.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.libertysoftware.padawanwallet.PadawanWalletApplication
import timber.log.Timber

class HomeViewModel(application: Application) : AndroidViewModel(application) {

    val app = application as PadawanWalletApplication

    public var balance: MutableLiveData<Float> = MutableLiveData(0F)
    public var satoshiUnit: MutableLiveData<Boolean> = MutableLiveData(false)

    private var internalBalance: Float = 0F

    public fun updateBalance() {
        app.sync(10)
        internalBalance = app.getBalance().toFloat()
        balance.postValue(internalBalance)
    }

    public fun changeUnit() {
        if (satoshiUnit.value!!) {
            Timber.i("[PADAWANLOGS] Satoshi unit value was true: ${satoshiUnit.value}")
            convertBalance()
            satoshiUnit.value = false
            Timber.i("[PADAWANLOGS] Satoshi unit value after conversion is: ${satoshiUnit.value}")
        } else {
            convertBalance()
            satoshiUnit.value = true
        }
    }

    private fun convertBalance() {
        if (satoshiUnit.value!!) {
            Timber.i("[PADAWANLOGS] Satoshi balance before converting is: ${balance.value}")
            internalBalance = internalBalance / 100_000_000
            balance.postValue(internalBalance)
            Timber.i("[PADAWANLOGS] Satoshi balance after conversion is: ${balance.value}")
        } else {
            Timber.i("[PADAWANLOGS] Satoshi balance before conversion is: ${balance.value}")
            internalBalance = internalBalance * 100_000_000
            balance.postValue(internalBalance)
            Timber.i("[PADAWANLOGS] Satoshi balance after conversion is: ${balance.value}")
        }
    }
}
