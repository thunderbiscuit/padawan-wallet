/*
 * Copyright 2020-2023 thunderbiscuit and contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE file.
 */

package com.goldenraven.padawanwallet.ui.settings

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.goldenraven.padawanwallet.BuildConfig
import com.goldenraven.padawanwallet.R
import com.goldenraven.padawanwallet.theme.PadawanTypography
import com.goldenraven.padawanwallet.theme.padawan_theme_background_secondary
import com.goldenraven.padawanwallet.theme.padawan_theme_button_primary
import com.goldenraven.padawanwallet.theme.padawan_theme_text_faded_secondary
import com.goldenraven.padawanwallet.ui.Screen
import com.goldenraven.padawanwallet.ui.chapters.ChaptersViewModel
import kotlinx.coroutines.launch

private const val TAG = "SettingsRootScreen"

@Composable
internal fun SettingsRootScreen(
    navController: NavController,
    viewModel: ChaptersViewModel
) {
    val scope = rememberCoroutineScope()
    val scrollState = rememberScrollState()
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(padawan_theme_background_secondary)
                .padding(padding)
                .verticalScroll(scrollState)
        ) {
            // Title
            Text(
                text = stringResource(R.string.settings),
                style = PadawanTypography.headlineSmall,
                color = Color(0xff1f0208),
                modifier = Modifier
                    .padding(top = 48.dp, start = 24.dp, end = 24.dp, bottom = 8.dp)
            )
            Text(
                text = stringResource(R.string.everything_else),
                color = Color(0xff787878),
                modifier = Modifier
                    .padding(start = 24.dp, end = 24.dp, bottom = 12.dp)
            )

            Button(
                onClick = {
                    navController.navigate(Screen.RecoveryPhraseScreen.route)
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xffffffff)),
                shape = RoundedCornerShape(8.dp),
                border = BorderStroke(1.dp, Color(0xff2f2f2f)),
                modifier = Modifier
                    .size(width = 400.dp, height = 80.dp)
                    .padding(start = 24.dp, end = 24.dp, top = 24.dp)
            ) {
                Text(
                    text = stringResource(R.string.recovery_phrase),
                    fontWeight = FontWeight.Normal,
                    color = Color(0xff2f2f2f)

                )
                Spacer(modifier = Modifier.weight(1f))
                Icon(
                    painter = painterResource(id = R.drawable.ic_hicon_right_2),
                    contentDescription = stringResource(R.string.scan_icon),
                    tint = Color(0xff2f2f2f)
                )
            }

            Button(
                onClick = {
                    navController.navigate(Screen.SendCoinsBackScreen.route)
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xffffffff)),
                shape = RoundedCornerShape(8.dp),
                border = BorderStroke(1.dp, Color(0xff2f2f2f)),
                modifier = Modifier
                    .size(width = 400.dp, height = 80.dp)
                    .padding(start = 24.dp, end = 24.dp, top = 24.dp)
            ) {
                Text(
                    text = stringResource(R.string.send_testnet_coins_back),
                    fontWeight = FontWeight.Normal,
                    color = Color(0xff2f2f2f)

                )
                Spacer(modifier = Modifier.weight(1f))
                Icon(
                    painter = painterResource(id = R.drawable.ic_hicon_right_2),
                    contentDescription = stringResource(id = R.string.scan_icon),
                    tint = Color(0xff2f2f2f)
                )
            }

            Button(
                onClick = {
                    navController.navigate(Screen.LanguagesScreen.route)
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xffffffff)),
                shape = RoundedCornerShape(8.dp),
                border = BorderStroke(1.dp, Color(0xff2f2f2f)),
                modifier = Modifier
                    .size(width = 400.dp, height = 80.dp)
                    .padding(start = 24.dp, end = 24.dp, top = 24.dp)
            ) {
                Text(
                    text = "Change language",
                    fontWeight = FontWeight.Normal,
                    color = Color(0xff2f2f2f)

                )
                Spacer(modifier = Modifier.weight(1f))
                Icon(
                    painter = painterResource(id = R.drawable.ic_hicon_right_2),
                    contentDescription = stringResource(id = R.string.scan_icon),
                    tint = Color(0xff2f2f2f)
                )
            }

            Button(
                onClick = {
                    navController.navigate(Screen.AboutScreen.route)
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xffffffff)),
                shape = RoundedCornerShape(8.dp),
                border = BorderStroke(1.dp, Color(0xff2f2f2f)),
                modifier = Modifier
                    .size(width = 400.dp, height = 80.dp)
                    .padding(start = 24.dp, end = 24.dp, top = 24.dp)
            ) {
                Text(
                    text = "About",
                    fontWeight = FontWeight.Normal,
                    color = Color(0xff2f2f2f)

                )
                Spacer(modifier = Modifier.weight(1f))
                Icon(
                    painter = painterResource(id = R.drawable.ic_hicon_right_2),
                    contentDescription = stringResource(id = R.string.scan_icon),
                    tint = Color(0xff2f2f2f)
                )
            }

            Divider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 40.dp, bottom = 8.dp, start = 40.dp, end = 40.dp),
                color = Color(0xff8f8f8f),
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
                    color = padawan_theme_text_faded_secondary
                )
            }

            Divider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp, bottom = 40.dp, start = 40.dp, end = 40.dp),
                color = Color(0xff8f8f8f),
                thickness = 1.dp
            )

            val chaptersResetMessage = stringResource(R.string.chapters_reset_successful)
            Button(
                onClick = {
                    viewModel.unsetAllCompleted()
                    scope.launch { snackbarHostState.showSnackbar(chaptersResetMessage) }
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xffFEE6DE)),
                shape = RoundedCornerShape(8.dp),
                border = BorderStroke(1.dp, padawan_theme_button_primary),
                modifier = Modifier
                    .size(width = 400.dp, height = 60.dp)
                    .padding(start = 24.dp, end = 24.dp)
            ) {
                Text(
                    text = stringResource(R.string.reset_completed_chapters),
                    fontWeight = FontWeight.Normal,
                    color = Color(0xff2f2f2f)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
