/*
 * Copyright 2020-2022 thunderbiscuit and contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE file.
 */

package com.goldenraven.padawanwallet.ui.settings

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
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

private const val TAG = "SettingsRootScreen"

@Composable
internal fun SettingsRootScreen(
    navController: NavController,
    viewModel: ChaptersViewModel
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(padawan_theme_background_secondary)
    ) {
        // Title
        Text(
            text = "Settings",
            style = PadawanTypography.headlineSmall,
            color = Color(0xff1f0208),
            modifier = Modifier
                .padding(top = 48.dp, start = 24.dp, end = 24.dp, bottom = 8.dp)
        )
        Text(
            text = "A collection of everything else you need in the app.",
            color = Color(0xff787878),
            modifier = Modifier
                .padding(start = 24.dp, end = 24.dp, bottom = 12.dp)
        )

        Button(
            onClick = {
                navController.navigate(Screen.RecoveryPhraseScreen.route) {
                    navController.graph.startDestinationRoute?.let { route ->
                        popUpTo(route)
                    }
                    launchSingleTop = true
                }
            },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xffffffff)),
            shape = RoundedCornerShape(8.dp),
            border = BorderStroke(1.dp, Color(0xff2f2f2f)),
            modifier = Modifier
                .size(width = 400.dp, height = 80.dp)
                .padding(start = 24.dp, end = 24.dp, top = 24.dp)
        ) {
            Text(
                text = "Recovery phrase",
                fontWeight = FontWeight.Normal,
                color = Color(0xff2f2f2f)

            )
            Spacer(modifier = Modifier.weight(1f))
            Icon(
                painter = painterResource(id = R.drawable.ic_hicon_right_2),
                contentDescription = "Scan icon",
                tint = Color(0xff2f2f2f)
            )
        }

        Button(
            onClick = {
                navController.navigate(Screen.SendCoinsBackScreen.route) {
                    navController.graph.startDestinationRoute?.let { route ->
                        popUpTo(route)
                    }
                    launchSingleTop = true
                }
            },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xffffffff)),
            shape = RoundedCornerShape(8.dp),
            border = BorderStroke(1.dp, Color(0xff2f2f2f)),
            modifier = Modifier
                .size(width = 400.dp, height = 80.dp)
                .padding(start = 24.dp, end = 24.dp, top = 24.dp)
        ) {
            Text(
                text = "Send testnet coins back",
                fontWeight = FontWeight.Normal,
                color = Color(0xff2f2f2f)

            )
            Spacer(modifier = Modifier.weight(1f))
            Icon(
                painter = painterResource(id = R.drawable.ic_hicon_right_2),
                contentDescription = "Scan icon",
                tint = Color(0xff2f2f2f)
            )
        }

        Button(
            onClick = {
                navController.navigate(Screen.AboutScreen.route) {
                    navController.graph.startDestinationRoute?.let { route ->
                        popUpTo(route)
                    }
                    launchSingleTop = true
                }
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
                contentDescription = "Scan icon",
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
            Text(
                text = "Padawan Wallet ${BuildConfig.VERSION_NAME}",
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

        Button(
            onClick = {
                viewModel.unsetAllCompleted()
            },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xffFEE6DE)),
            shape = RoundedCornerShape(8.dp),
            border = BorderStroke(1.dp, padawan_theme_button_primary),
            modifier = Modifier
                .size(width = 400.dp, height = 60.dp)
                .padding(start = 24.dp, end = 24.dp)
        ) {
            Text(
                text = "Reset completed chapters",
                fontWeight = FontWeight.Normal,
                color = Color(0xff2f2f2f)
            )
        }
    }
}
