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
import com.goldenraven.padawanwallet.data.Wallet

@Composable
internal fun SendScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(md_theme_dark_background)
    ) {
        Text(text = "Receive screen")
    }
}
