package com.goldenraven.padawanwallet.intro

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import com.goldenraven.padawanwallet.R
import com.goldenraven.padawanwallet.theme.*

@Composable
internal fun WalletRecoveryScreen() {
    PadawanTheme {
        Column(
            modifier = Modifier.background(MaterialTheme.colors.background)
                .fillMaxSize(1f),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            AppName()
            RecoverFromMnemonicButton()
        }
    }
}

@Composable
fun RecoverFromMnemonicButton() {
    Button(onClick = { /*TODO*/ }) {
        Text(stringResource(R.string.recover_wallet))
    }
}

@Preview(device = Devices.PIXEL_4, showBackground = true)
@Composable
internal fun PreviewWalletRecoveryScreen() {
    WalletRecoveryScreen()
}
