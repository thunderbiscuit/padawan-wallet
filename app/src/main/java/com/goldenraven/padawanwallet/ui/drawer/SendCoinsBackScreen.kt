package com.goldenraven.padawanwallet.ui.wallet

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.goldenraven.padawanwallet.R
import com.goldenraven.padawanwallet.ui.DrawerAppBar


@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun SendCoinsBackScreen(navController: NavController) {

    val context = LocalContext.current

    Scaffold(
        topBar = { DrawerAppBar(navController, title = "Send Coins Back") },
    ) {
        Column(
            Modifier.padding(all = 32.dp)
        ) {
            Image(
                painterResource(R.drawable.return_sats_faucet_address),
                contentDescription = "Return sats faucet address image",
                modifier = Modifier.padding(start = 50.dp, end = 50.dp, bottom = 20.dp)
            )
            Column(
                Modifier
                    .align(Alignment.CenterHorizontally)
                    .clickable(onClick = { copyToClipboard(context) })
                    .padding(bottom = 20.dp)
            ) {
                Text(
                    text = stringResource(id = R.string.send_coins_back_address),
                    maxLines = 1
                )
                Divider(
                    color = colorResource(id = R.color.fg1),
                    thickness = 1.dp,
                    modifier = Modifier.padding(all = 3.dp)
                )
                Row(
                    Modifier.align(Alignment.End)
                ) {
                    Image(
                        painterResource(R.drawable.ic_copy_outline),
                        contentDescription = "Copy to clipboard image",
                    )
                    Text(text = "Copy")
                }
            }
            Text(text = stringResource(id = R.string.send_coins_back))
        }
    }
}

fun copyToClipboard(context: Context) {
    val clipboard: ClipboardManager =
        context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val clip: ClipData =
        ClipData.newPlainText("Copied address", context.getString(R.string.send_coins_back_address))
    clipboard.setPrimaryClip(clip)
}