package com.libertysoftware.padawanwallet.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.libertysoftware.padawanwallet.PadawanWalletApplication

class HomeViewModel(application: Application) : AndroidViewModel(application) {

    val app = application as PadawanWalletApplication

    public var balance: MutableLiveData<Long> = MutableLiveData(0)

    public fun updateBalance() {
        app.sync(10)
        val newBalance = app.getBalance()
        balance.postValue(newBalance)
    }
}
