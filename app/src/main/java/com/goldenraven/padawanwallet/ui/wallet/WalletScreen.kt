package com.goldenraven.padawanwallet.ui.wallet

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.goldenraven.padawanwallet.theme.md_theme_dark_background
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.lifecycle.LiveData
import androidx.navigation.NavHostController
import com.goldenraven.padawanwallet.data.Wallet
import com.goldenraven.padawanwallet.ui.Screen

@Composable
internal fun WalletScreen(
    walletViewModel: WalletViewModel = viewModel(),
    navController: NavHostController
) {
    val balance by walletViewModel.balance.observeAsState()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(md_theme_dark_background)
    ) {
        Text(text = "The balance is $balance")
        Button(
            onClick = { walletViewModel.updateBalance() },
        ) {
            Text(text = "Balance")
        }
        Button(
            onClick = { navController.navigate(Screen.SendScreen.route) },
        ) {
            Text(text = "Send")
        }
        Button(
            onClick = { navController.navigate(Screen.ReceiveScreen.route) },
        ) {
            Text(text = "Receive")
        }
    }
}

internal class WalletViewModel() : ViewModel() {

    private var _balance: MutableLiveData<ULong> = MutableLiveData(0u)
    val balance: LiveData<ULong>
        get() = _balance

    fun updateBalance() {
        Wallet.sync()
        _balance.value = Wallet.getBalance()
    }
}

