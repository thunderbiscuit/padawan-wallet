/*
 * Copyright 2020-2025 thunderbiscuit and contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE file.
 */

package com.coyotebitcoin.padawanwallet.presentation.ui.screens.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.coyotebitcoin.padawanwallet.BuildConfig
import com.coyotebitcoin.padawanwallet.R
import com.coyotebitcoin.padawanwallet.presentation.navigation.SecondaryDestinations
import com.coyotebitcoin.padawanwallet.presentation.theme.LocalPadawanColors
import com.coyotebitcoin.padawanwallet.presentation.theme.PadawanColorsTatooineDesert
import com.coyotebitcoin.padawanwallet.presentation.theme.PadawanTheme
import com.coyotebitcoin.padawanwallet.presentation.theme.PadawanTypography
import com.coyotebitcoin.padawanwallet.presentation.ui.components.ExtraButton
import com.coyotebitcoin.padawanwallet.presentation.viewmodels.LessonAction
import kotlinx.coroutines.launch

private const val TAG = "PadawanTag/SettingsRootScreen"

@Composable
internal fun SettingsRootScreen(
    onAction: (LessonAction) -> Unit,
    onNavigation: (SecondaryDestinations) -> Unit,
) {
    val scope = rememberCoroutineScope()
    val scrollState = rememberScrollState()
    val snackbarHostState = remember { SnackbarHostState() }
    val colors = LocalPadawanColors.current

    Scaffold(
        snackbarHost = {
            SnackbarHost(
                snackbarHostState,
                Modifier
                    .navigationBarsPadding()
                    .padding(bottom = 70.dp)
            )
        }
    ) { scaffoldPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(scaffoldPadding)
                .verticalScroll(scrollState)
        ) {
            // Title
            Text(
                text = stringResource(R.string.settings),
                style = PadawanTypography.headlineSmall,
                color = colors.text,
                modifier = Modifier
                    .padding(top = 32.dp, start = 24.dp, end = 24.dp, bottom = 8.dp)
            )
            Text(
                text = stringResource(R.string.everything_else),
                color = colors.textLight,
                modifier = Modifier
                    .padding(start = 24.dp, end = 24.dp, bottom = 12.dp)
            )


            ExtraButton(
                label = stringResource(R.string.recovery_phrase),
                onClick = { onNavigation(SecondaryDestinations.RecoveryPhraseScreen) }
            )

            ExtraButton(
                label = stringResource(R.string.send_signet_coins_back),
                onClick = { onNavigation(SecondaryDestinations.SendCoinsBackScreen) }
            )

            ExtraButton(
                label = stringResource(R.string.change_language),
                onClick = { onNavigation(SecondaryDestinations.LanguagesScreen) }
            )

            ExtraButton(
                label = stringResource(R.string.about_padawan),
                onClick = { onNavigation(SecondaryDestinations.AboutScreen) }
            )

            HorizontalDivider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 40.dp, bottom = 8.dp, start = 40.dp, end = 40.dp),
                color = colors.textLight,
                thickness = 1.dp
            )

            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                val padawanVersion = "${stringResource(R.string.padawan_wallet)} ${BuildConfig.VERSION_NAME}"
                Text(
                    text = padawanVersion,
                    style = PadawanTypography.bodySmall,
                    color = PadawanColorsTatooineDesert.textLight
                )
            }

            HorizontalDivider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp, bottom = 40.dp, start = 40.dp, end = 40.dp),
                color = colors.textLight,
                thickness = 1.dp
            )

            val chaptersResetMessage = stringResource(R.string.lessons_reset_successful)
            Button(
                onClick = {
                    onAction(LessonAction.ResetAll)
                    scope.launch { snackbarHostState.showSnackbar(chaptersResetMessage) }
                },
                colors = ButtonDefaults.buttonColors(containerColor = colors.accent1),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .size(width = 400.dp, height = 60.dp)
                    .padding(start = 24.dp, end = 24.dp)
            ) {
                Text(
                    text = stringResource(R.string.reset_completed_chapters),
                    fontWeight = FontWeight.Normal,
                    color = colors.text
                )
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Preview(device = Devices.PIXEL_7, showBackground = true)
@Composable
internal fun PreviewSettingsRootScreen() {
    PadawanTheme {
        SettingsRootScreen(
            onAction = { },
            onNavigation = { }
        )
    }
}
