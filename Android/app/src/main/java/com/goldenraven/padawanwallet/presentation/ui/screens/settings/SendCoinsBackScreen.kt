/*
 * Copyright 2020-2024 thunderbiscuit and contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE file.
 */

package com.goldenraven.padawanwallet.presentation.ui.screens.settings

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.foundation.text.appendInlineContent
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.composables.icons.lucide.CheckCheck
import com.composables.icons.lucide.CircleCheck
import com.composables.icons.lucide.ClipboardCopy
import com.composables.icons.lucide.Lucide
import com.goldenraven.padawanwallet.R
import com.goldenraven.padawanwallet.presentation.theme.PadawanTheme
import com.goldenraven.padawanwallet.presentation.theme.PadawanTypography
import com.goldenraven.padawanwallet.presentation.theme.padawan_theme_background_secondary
import com.goldenraven.padawanwallet.presentation.theme.padawan_theme_text_faded_secondary
import com.goldenraven.padawanwallet.presentation.ui.components.PadawanAppBar
import com.goldenraven.padawanwallet.presentation.ui.components.VerticalTextFieldDivider
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
        // appendInlineContent(id = "copyAddressImageId")
        if (!copyClicked) append(stringResource(id = R.string.copy_address_string)) else append(stringResource(R.string.textCopied))
    }

    val inlineContentMap = mapOf(
        "copyAddressImageId" to InlineTextContent(
            Placeholder(17.sp, 17.sp, PlaceholderVerticalAlign.TextCenter)
        ) {
            if (copyClicked) {
                Image(
                    imageVector = Lucide.CircleCheck,
                    contentDescription = stringResource(R.string.checkmark_image),
                    colorFilter = ColorFilter.tint(Color.Green)
                )
            } else {
                Image(
                    imageVector = Lucide.ClipboardCopy,
                    contentDescription = stringResource(R.string.copy_to_clipboard_image),
                )
            }
        }
    )
    Scaffold(
        topBar = {
            PadawanAppBar(
                title = stringResource(R.string.send_your_coins_back_to_us),
                onClick = { navController.popBackStack() }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { scaffoldPadding ->
        Column(
            Modifier
                .fillMaxSize()
                .verticalScroll(state = scrollState)
                .background(padawan_theme_background_secondary)
                .padding(scaffoldPadding)
        ) {
            val returnAddress: String = stringResource(R.string.send_coins_back_address)
            Image(
                painterResource(R.drawable.return_sats_faucet_address),
                contentDescription = stringResource(R.string.return_sats_faucet_address_image),
                modifier = Modifier.padding(start = 50.dp, end = 50.dp, bottom = 20.dp)
            )
            Row(
                Modifier
                    // .align(Alignment.CenterHorizontally)
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
                VerticalTextFieldDivider()
                Image(
                    imageVector = Lucide.ClipboardCopy,
                    contentDescription = stringResource(R.string.copy_to_clipboard_image),
                )
                // Text(
                //     text = copyAddressString,
                //     inlineContent = inlineContentMap,
                //     modifier = Modifier.align(Alignment.End)
                // )
            }
            // Column(
            //     Modifier
            //         .align(Alignment.CenterHorizontally)
            //         .clickable(onClick = {
            //             setCopyClicked(true)
            //             copyToClipboard(
            //                 returnAddress,
            //                 context,
            //                 scope,
            //                 snackbarHostState,
            //                 setCopyClicked
            //             )
            //         })
            //         .padding(start = 24.dp, end = 24.dp, bottom = 20.dp)
            // ) {
            //     Text(
            //         text = stringResource(R.string.send_coins_back_address),
            //         fontSize = 14.sp
            //     )
            //     HorizontalDivider(
            //         color = padawan_theme_text_faded_secondary,
            //         thickness = 1.dp,
            //         modifier = Modifier.padding(all = 3.dp)
            //     )
            //     Image(
            //         imageVector = Lucide.ClipboardCopy,
            //         contentDescription = stringResource(R.string.copy_to_clipboard_image),
            //     )
            //     // Text(
            //     //     text = copyAddressString,
            //     //     inlineContent = inlineContentMap,
            //     //     modifier = Modifier.align(Alignment.End)
            //     // )
            // }
            Text(
                text = stringResource(id = R.string.send_coins_back),
                style = PadawanTypography.bodyMedium,
                color = padawan_theme_text_faded_secondary,
                modifier = Modifier.padding(start = 24.dp, end = 24.dp)
            )
        }
    }
}

@Preview(device = Devices.PIXEL_7, showBackground = true)
@Composable
internal fun PreviewSendCoinsBackScreen() {
    PadawanTheme {
        SendCoinsBackScreen(navController = rememberNavController())
    }
}
