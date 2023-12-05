/*
 * Copyright 2020-2023 thunderbiscuit and contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE file.
 */

package com.goldenraven.padawanwallet.ui.screens.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.goldenraven.padawanwallet.R
import com.goldenraven.padawanwallet.ui.theme.PadawanTypography
import com.goldenraven.padawanwallet.ui.theme.padawan_theme_background_secondary
import com.goldenraven.padawanwallet.ui.theme.padawan_theme_button_primary
import com.goldenraven.padawanwallet.ui.theme.padawan_theme_text_faded_secondary
import com.goldenraven.padawanwallet.ui.components.PadawanAppBar

@Composable
internal fun AboutScreen(
    navController: NavHostController
) {
    val scrollState = rememberScrollState()
    Column(
        Modifier
            .verticalScroll(state = scrollState)
            .background(padawan_theme_background_secondary)
            .padding(bottom = 12.dp)
    ) {
        PadawanAppBar(navController = navController, title = stringResource(R.string.about_padawan))
        Text(
            text = stringResource(R.string.about_text),
            modifier = Modifier.padding(start = 24.dp, end = 24.dp),
            style = PadawanTypography.bodyMedium,
            color = padawan_theme_text_faded_secondary
        )
        Spacer(Modifier.height(24.dp))
        Text(
            text = stringResource(R.string.privacyText),
            modifier = Modifier.padding(start = 24.dp, end = 24.dp),
            style = PadawanTypography.bodyMedium,
            color = padawan_theme_text_faded_secondary
        )
        Spacer(Modifier.height(24.dp))
        val mUriHandler = LocalUriHandler.current
        val privacyLink = stringResource(id = R.string.privacyLink)
        Text(
            text = privacyLink,
            modifier = Modifier
                .clickable { mUriHandler.openUri(privacyLink) }
                .padding(start = 24.dp, end = 24.dp),
            style = PadawanTypography.bodyMedium,
            color = padawan_theme_button_primary,
            textDecoration = TextDecoration.Underline
        )
    }
}
