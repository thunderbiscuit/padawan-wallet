/*
 * Copyright 2020-2025 thunderbiscuit and contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE file.
 */

package com.coyotebitcoin.padawanwallet.presentation.ui.screens.settings

import androidx.compose.foundation.layout.Column
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
import com.coyotebitcoin.padawanwallet.R
import com.coyotebitcoin.padawanwallet.presentation.theme.LocalPadawanColors
import com.coyotebitcoin.padawanwallet.presentation.theme.PadawanTheme
import com.coyotebitcoin.padawanwallet.presentation.theme.PadawanTypography
import com.coyotebitcoin.padawanwallet.presentation.ui.components.ColorThemePicker
import com.coyotebitcoin.padawanwallet.presentation.ui.components.PadawanAppBar
import com.coyotebitcoin.padawanwallet.presentation.ui.components.SectionDivider
import com.coyotebitcoin.padawanwallet.presentation.ui.components.SectionTitle
import com.coyotebitcoin.padawanwallet.presentation.ui.components.SupportedLanguagesPicker

private const val TAG = "SettingsScreen"

@Composable
internal fun LanguagesScreen(
    navController: NavHostController
) {
    val colors = LocalPadawanColors.current

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
                .padding(scaffoldPadding)
                .padding(horizontal = 24.dp)
                .fillMaxSize()
        ) {
            Text(
                text = stringResource(R.string.select_language),
                style = PadawanTypography.bodyMedium,
                color = colors.textLight
            )
            SectionTitle("App-Level Language", false)
            SectionDivider()
            SupportedLanguagesPicker()

            SectionTitle("Color Theme", false)
            SectionDivider()
            ColorThemePicker()
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
