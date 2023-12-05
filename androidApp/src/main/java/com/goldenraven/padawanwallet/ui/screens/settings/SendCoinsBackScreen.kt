/*
 * Copyright 2020-2023 thunderbiscuit and contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE file.
 */

package com.goldenraven.padawanwallet.ui.screens.settings

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.foundation.text.appendInlineContent
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Divider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.Placeholder
import androidx.compose.ui.text.PlaceholderVerticalAlign
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.goldenraven.padawanwallet.R
import com.goldenraven.padawanwallet.ui.theme.PadawanTypography
import com.goldenraven.padawanwallet.ui.theme.padawan_theme_background_secondary
import com.goldenraven.padawanwallet.ui.theme.padawan_theme_text_faded_secondary
import com.goldenraven.padawanwallet.ui.components.PadawanAppBar
import com.goldenraven.padawanwallet.utils.copyToClipboard

@Composable
internal fun SendCoinsBackScreen(
    navController: NavHostController
) {
    val scrollState = rememberScrollState()
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    val (copyClicked, setCopyClicked) = remember { mutableStateOf(false) }

    val copyAddressString = buildAnnotatedString {
        appendInlineContent(id = "copyAddressImageId")
        if (!copyClicked) append(stringResource(id = R.string.copyAddrStr)) else append(stringResource(R.string.textCopied))
    }

    val inlineContentMap = mapOf(
        "copyAddressImageId" to InlineTextContent(
            Placeholder(17.sp, 17.sp, PlaceholderVerticalAlign.TextCenter)
        ) {
            if (copyClicked) {
                Image(
                    painter = painterResource(R.drawable.ic_checked),
                    contentDescription = stringResource(R.string.checkmark_image),
                    colorFilter = ColorFilter.tint(Color.Green)

                )
            } else {
                Image(
                    painter = painterResource(R.drawable.hicon_add_square),
                    contentDescription = stringResource(R.string.copy_to_clipboard_image),
                )
            }
        }
    )
    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        Column(
            Modifier
                .fillMaxSize()
                .verticalScroll(state = scrollState)
                .background(padawan_theme_background_secondary)
                .padding(padding)
        ) {
            val returnAddress: String = stringResource(R.string.send_coins_back_address)
            PadawanAppBar(navController = navController, title = stringResource(R.string.send_your_coins_back_to_us))
            Image(
                painterResource(R.drawable.return_sats_faucet_address),
                contentDescription = stringResource(R.string.return_sats_faucet_address_image),
                modifier = Modifier.padding(start = 50.dp, end = 50.dp, bottom = 20.dp)
            )
            Column(
                Modifier
                    .align(Alignment.CenterHorizontally)
                    .clickable(onClick = {
                        setCopyClicked(true)
                        copyToClipboard(
                            returnAddress,
                            context,
                            scope,
                            snackbarHostState,
                            setCopyClicked
                        )
                    })
                    .padding(start = 24.dp, end = 24.dp, bottom = 20.dp)
            ) {
                Text(
                    text = stringResource(R.string.send_coins_back_address),
                    fontSize = 14.sp
                )
                Divider(
                    color = padawan_theme_text_faded_secondary,
                    thickness = 1.dp,
                    modifier = Modifier.padding(all = 3.dp)
                )
                Text(
                    text = copyAddressString,
                    inlineContent = inlineContentMap,
                    modifier = Modifier.align(Alignment.End)
                )
            }
            Text(
                text = stringResource(id = R.string.send_coins_back),
                style = PadawanTypography.bodyMedium,
                color = padawan_theme_text_faded_secondary,
                modifier = Modifier.padding(start = 24.dp, end = 24.dp)
            )
        }
    }
}

// fun copyToClipboard(context: Context, scope: CoroutineScope, snackbarHostState: SnackbarHostState, setCopyClicked: (Boolean) -> Unit) {
//     val clipboard: ClipboardManager = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
//     val clip: ClipData = ClipData.newPlainText("", context.getString(R.string.send_coins_back_address))
//     clipboard.setPrimaryClip(clip)
//     scope.launch {
//         snackbarHostState.showSnackbar("Copied address to clipboard!")
//         delay(1000)
//         setCopyClicked(false)
//     }
// }
