/*
 * Copyright 2020-2024 thunderbiscuit and contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE file.
 */

package com.goldenraven.padawanwallet.presentation.ui.screens.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.goldenraven.padawanwallet.R
import com.goldenraven.padawanwallet.presentation.theme.PadawanTheme
import com.goldenraven.padawanwallet.presentation.theme.PadawanTypography
import com.goldenraven.padawanwallet.presentation.theme.padawan_theme_background_secondary
import com.goldenraven.padawanwallet.presentation.theme.padawan_theme_text_faded_secondary
import com.goldenraven.padawanwallet.presentation.ui.components.PadawanAppBar
import com.goldenraven.padawanwallet.presentation.ui.components.SupportedLanguagesPicker

private const val TAG = "LanguagesScreen"

@Composable
internal fun LanguagesScreen(
    navController: NavHostController
) {
    Scaffold(
        topBar = {
            PadawanAppBar(
                title = stringResource(R.string.change_language),
                onClick = { navController.popBackStack() }
            )
        }
    ) { scaffoldPadding ->
        Column(
            Modifier
                .background(padawan_theme_background_secondary)
                .padding(scaffoldPadding)
                .padding(horizontal = 24.dp)
                .fillMaxSize()
        ) {
            Text(
                text = stringResource(R.string.select_your_preferred_language),
                style = PadawanTypography.bodyMedium,
                color = padawan_theme_text_faded_secondary
            )
            Spacer(modifier = Modifier.padding(vertical = 16.dp))
            SupportedLanguagesPicker()
        }
    }
}

@Preview(device = Devices.PIXEL_7, showBackground = true)
@Composable
internal fun PreviewLanguagesScreen() {
    PadawanTheme {
        LanguagesScreen(navController = rememberNavController())
    }
}
