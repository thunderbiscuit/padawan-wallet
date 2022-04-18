package com.goldenraven.padawanwallet.ui.wallet

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.foundation.text.appendInlineContent
import androidx.compose.material.SnackbarHost
import androidx.compose.material.SnackbarHostState
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.Placeholder
import androidx.compose.ui.text.PlaceholderVerticalAlign
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.goldenraven.padawanwallet.R
import com.goldenraven.padawanwallet.ui.DrawerAppBar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun SendCoinsBackScreen(navController: NavController) {

    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    val copyAddrString = buildAnnotatedString {
        appendInlineContent(id = "copyAddrImageId")
        append(stringResource(id = R.string.copyAddrStr))
    }

    val inlineContentMap = mapOf(
        "copyAddrImageId" to InlineTextContent(
            Placeholder(17.sp, 17.sp, PlaceholderVerticalAlign.TextCenter)
        ) {
            Image(
                painterResource(R.drawable.ic_copy_outline),
                contentDescription = "Copy to clipboard image",
            )
        }
    )

    Scaffold(
        topBar = { DrawerAppBar(navController, title = "Send Coins Back") },
        bottomBar = { SnackbarHost(hostState = snackbarHostState) }
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
                    .clickable(onClick = { copyToClipboard(context, scope, snackbarHostState) })
                    .padding(bottom = 20.dp)
            ) {
                Text(text = stringResource(id = R.string.send_coins_back_address))
                Divider(
                    color = colorResource(id = R.color.fg1),
                    thickness = 1.dp,
                    modifier = Modifier.padding(all = 3.dp)
                )
                Text(
                    text = copyAddrString,
                    inlineContent = inlineContentMap,
                    modifier = Modifier.align(Alignment.End)
                )
            }
            Text(text = stringResource(id = R.string.send_coins_back))
        }
    }
}

fun copyToClipboard(context: Context, scope: CoroutineScope, snackbarHostState: SnackbarHostState) {
    val clipboard: ClipboardManager = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val clip: ClipData = ClipData.newPlainText("", context.getString(R.string.send_coins_back_address))
    clipboard.setPrimaryClip(clip)

    scope.launch { snackbarHostState.showSnackbar("Copied address to clipboard!") }
}