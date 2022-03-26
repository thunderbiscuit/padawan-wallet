package com.goldenraven.padawanwallet.wallet

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.goldenraven.padawanwallet.theme.md_theme_dark_background
import com.goldenraven.padawanwallet.theme.md_theme_dark_background2
import com.goldenraven.padawanwallet.theme.md_theme_dark_onBackgroundFaded

@Composable
internal fun WalletScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(md_theme_dark_background)
    ) {
        Text(text = "I am a wallet")
    }
}