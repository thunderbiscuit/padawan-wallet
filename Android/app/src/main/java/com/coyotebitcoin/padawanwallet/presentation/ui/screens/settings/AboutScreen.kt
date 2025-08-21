/*
 * Copyright 2020-2025 thunderbiscuit and contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE file.
 */

package com.coyotebitcoin.padawanwallet.presentation.ui.screens.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.coyotebitcoin.padawanwallet.R
import com.coyotebitcoin.padawanwallet.presentation.theme.LocalPadawanColors
import com.coyotebitcoin.padawanwallet.presentation.theme.PadawanColorsTatooineDesert
import com.coyotebitcoin.padawanwallet.presentation.theme.PadawanTheme
import com.coyotebitcoin.padawanwallet.presentation.theme.PadawanTypography
import com.coyotebitcoin.padawanwallet.presentation.ui.components.PadawanAppBar

@Composable
internal fun AboutScreen(
    onBack: () -> Unit
) {
    val scrollState = rememberScrollState()
    val mUriHandler = LocalUriHandler.current
    val privacyLink = stringResource(id = R.string.privacy_link)
    val openPrivacyLink = remember { { mUriHandler.openUri(privacyLink) } }
    val colors = LocalPadawanColors.current

    Scaffold(
        topBar = {
            PadawanAppBar(
                title = stringResource(R.string.about_padawan),
                onClick = { onBack() }
            )
        },
        modifier = Modifier.fillMaxHeight()
    ) { scaffoldPadding ->
        Column(
            Modifier
                .background(PadawanColorsTatooineDesert.background)
                .padding(scaffoldPadding)
                .padding(horizontal = 24.dp)
                .fillMaxSize()
                .verticalScroll(state = scrollState)
        ) {
            Text(
                text = stringResource(R.string.about_text),
                style = PadawanTypography.bodyMedium,
                color = colors.textLight
            )
            Spacer(Modifier.height(24.dp))
            Text(
                text = stringResource(R.string.privacy_text),
                style = PadawanTypography.bodyMedium,
                color = colors.textLight,
            )
            Spacer(Modifier.height(24.dp))
            Text(
                text = "Read our privacy policy here.",
                style = PadawanTypography.bodyMedium,
                color = PadawanColorsTatooineDesert.accent2,
                textDecoration = TextDecoration.Underline,
                modifier = Modifier.clickable(onClick = openPrivacyLink)
            )
        }
    }
}

@Preview(device = Devices.PIXEL_7, showBackground = true)
@Composable
internal fun PreviewAboutScreen() {
    PadawanTheme {
        AboutScreen(onBack = { })
    }
}
