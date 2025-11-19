/*
 * Copyright 2020-2025 thunderbiscuit and contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE file.
 */

package com.coyotebitcoin.padawanwallet.presentation.ui.screens.settings

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.Placeholder
import androidx.compose.ui.text.PlaceholderVerticalAlign
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.composables.icons.lucide.CircleCheck
import com.composables.icons.lucide.ClipboardCopy
import com.composables.icons.lucide.Lucide
import com.coyotebitcoin.padawanwallet.R
import com.coyotebitcoin.padawanwallet.domain.utils.addressToQR
import com.coyotebitcoin.padawanwallet.presentation.theme.LocalPadawanColors
import com.coyotebitcoin.padawanwallet.presentation.theme.PadawanTheme
import com.coyotebitcoin.padawanwallet.presentation.theme.PadawanTypography
import com.coyotebitcoin.padawanwallet.presentation.ui.components.PadawanAppBar
import com.coyotebitcoin.padawanwallet.presentation.ui.components.VerticalTextFieldDivider
import com.coyotebitcoin.padawanwallet.presentation.utils.copyToClipboard

@Composable
internal fun SendCoinsBackScreen(
    onBackArrow: () -> Unit,
) {
    val scrollState = rememberScrollState()
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val colors = LocalPadawanColors.current

    val (copyClicked, setCopyClicked) = remember { mutableStateOf(false) }

    val copyAddressString = buildAnnotatedString {
        if (!copyClicked) append(stringResource(id = R.string.copy_address_string)) else append(stringResource(R.string.text_copied))
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
                title = stringResource(R.string.send_signet_coins_back),
                onClick = { onBackArrow() }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { scaffoldPadding ->
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(state = scrollState)
                .padding(scaffoldPadding)
        ) {
            val returnAddress: String = stringResource(R.string.send_coins_back_address)
            val returnQr = addressToQR(returnAddress)

            returnQr?.let {
                Image(
                    bitmap = it,
                    contentDescription = stringResource(R.string.qr_code),
                    Modifier
                        .padding(vertical = 16.dp)
                        .size(340.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .clickable {
                            copyToClipboard(
                                returnAddress,
                                context,
                                scope,
                                snackbarHostState,
                                null
                            )
                        }
                )
            }
            Row(
                Modifier
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
            }

            Text(
                text = stringResource(id = R.string.send_coins_back),
                style = PadawanTypography.bodyMedium,
                color = colors.textLight,
                modifier = Modifier.padding(start = 24.dp, end = 24.dp)
            )
        }
    }
}

@Preview(device = Devices.PIXEL_7, showBackground = true)
@Composable
internal fun PreviewSendCoinsBackScreen() {
    PadawanTheme {
        SendCoinsBackScreen(
            onBackArrow = {}
        )
    }
}
