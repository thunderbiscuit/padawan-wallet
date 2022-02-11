package com.goldenraven.padawanwallet.intro

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.goldenraven.padawanwallet.R
import com.goldenraven.padawanwallet.theme.*

@Composable
internal fun WalletChoiceScreen(navController: NavController) {
    PadawanTheme {
        Column(
            modifier = Modifier
                .background(MaterialTheme.colors.background)
                .fillMaxSize(1f),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            AppName()
            CreateNewWalletButton()
            AlreadyHaveWalletButton(navController)
        }
    }
}

@Composable
internal fun CreateNewWalletButton() {
    Button(
        onClick = { /*TODO*/ }
    ) {
        Text(stringResource(R.string.create_new_wallet))
    }
}

@Composable
fun AlreadyHaveWalletButton(navController: NavController) {
    Button(
        onClick = { navController.navigate(Screen.WalletRecoveryScreen.route) }
    ) {
        Text(stringResource(R.string.already_have_a_wallet))
    }
}

@Preview(device = Devices.PIXEL_4, showBackground = true)
@Composable
internal fun PreviewWalletChoiceScreen() {
    WalletChoiceScreen(rememberNavController())
}
