/*
 * Copyright 2020 thunderbiscuit and contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE file.
 */

package com.libertysoftware.padawanwallet.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.libertysoftware.padawanwallet.PadawanWalletApplication

class HomeViewModel(application: Application) : AndroidViewModel(application) {

    val app = application as PadawanWalletApplication

    public var balance: MutableLiveData<Long> = MutableLiveData(0)
    public var satoshiUnit: MutableLiveData<Boolean> = MutableLiveData(true)

    public fun updateBalance() {
        app.sync(10)
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
}
