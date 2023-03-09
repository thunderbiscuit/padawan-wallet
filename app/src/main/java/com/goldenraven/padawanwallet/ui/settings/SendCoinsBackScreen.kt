/*
 * Copyright 2020-2022 thunderbiscuit and contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE file.
 */

package com.goldenraven.padawanwallet.ui.settings

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.foundation.text.appendInlineContent
import androidx.compose.material.ScaffoldState
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.Divider
import androidx.compose.material.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import com.goldenraven.padawanwallet.theme.*
import com.goldenraven.padawanwallet.ui.PadawanAppBar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
internal fun SendCoinsBackScreen(
    navController: NavHostController
) {
    val scrollState = rememberScrollState()
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val scaffoldState: ScaffoldState = rememberScaffoldState()

    val copyAddrString = buildAnnotatedString {
        appendInlineContent(id = "copyAddrImageId")
        append(stringResource(id = R.string.copyAddrStr))
    }

    val inlineContentMap = mapOf(
        "copyAddrImageId" to InlineTextContent(
            Placeholder(17.sp, 17.sp, PlaceholderVerticalAlign.TextCenter)
        ) {
            Image(
                painterResource(R.drawable.hicon_add_square),
                contentDescription = "Copy to clipboard image",
            )
        }
    )
    Scaffold(
        scaffoldState = scaffoldState
    ) { padding ->
        Column(
            Modifier
                .fillMaxSize()
                .verticalScroll(state = scrollState)
                .background(padawan_theme_background_secondary)
                .padding(padding)
        ) {
            PadawanAppBar(navController = navController, title = "Send your coins back to us!")
            Image(
                painterResource(R.drawable.return_sats_faucet_address),
                contentDescription = "Return sats faucet address image",
                modifier = Modifier.padding(start = 50.dp, end = 50.dp, bottom = 20.dp)
            )
            Column(
                Modifier
                    .align(Alignment.CenterHorizontally)
                    .clickable(onClick = { copyToClipboard(context, scope, scaffoldState) })
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
                    text = copyAddrString,
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

fun copyToClipboard(context: Context, scope: CoroutineScope, state: ScaffoldState) {
    val clipboard: ClipboardManager = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val clip: ClipData = ClipData.newPlainText("", context.getString(R.string.send_coins_back_address))
    clipboard.setPrimaryClip(clip)

    scope.launch { state.snackbarHostState.showSnackbar("Copied address to clipboard!") }
}