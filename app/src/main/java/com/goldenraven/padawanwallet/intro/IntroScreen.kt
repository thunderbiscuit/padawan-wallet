/*
 * Copyright 2020-2022 thunderbiscuit and contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE file.
 */

package com.goldenraven.padawanwallet.intro

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.goldenraven.padawanwallet.R
import com.goldenraven.padawanwallet.theme.*
import com.goldenraven.padawanwallet.ui.Screen
import com.goldenraven.padawanwallet.ui.ShowBars

@Composable
internal fun IntroScreen(navController: NavController) {

    // the splash screen hides the system bars
    // we need to bring them back on before continuing
    ShowBars()

    val (showDialog, setShowDialog) =  remember { mutableStateOf(false) }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceAround,
        modifier = Modifier
            .background(color = md_theme_dark_background)
            .fillMaxSize(1f),
    ) {
        AppName()
        IntroText()
        LetsGoButton(setShowDialog)
        Dialog(
            showDialog = showDialog,
            setShowDialog = setShowDialog,
            navController
        )
    }
}

@Composable
fun AppName() {
    Column {
        Text(
            text = stringResource(R.string.padawan),
            color = md_theme_dark_primary,
            fontSize = 70.sp,
            fontFamily = shareTechMono,
        )
        Text(
            stringResource(R.string.elevator_pitch),
            color = md_theme_dark_onBackground,
            fontSize = 16.sp,
            fontFamily = jost,
            fontStyle = FontStyle.Italic,
            fontWeight = FontWeight.Light,
            modifier = Modifier.align(Alignment.End)
        )
    }
}

@Composable
fun Dialog(showDialog: Boolean, setShowDialog: (Boolean) -> Unit, navController: NavController) {
    if (showDialog) {
        AlertDialog(
            // modifier = Modifier.background(md_theme_dark_lightBackground),
            backgroundColor = md_theme_dark_lightBackground,
            onDismissRequest = {},
            title = {
                Text(
                    text = stringResource(R.string.intro_dialog_title),
                    style = MaterialTheme.typography.headlineMedium,
                    color = md_theme_dark_onLightBackground
                )
            },
            text = {
                Text(
                    text = stringResource(R.string.intro_dialog_message),
                    color = md_theme_dark_onLightBackground
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        setShowDialog(false)
                        navController.navigate(Screen.WalletChoiceScreen.route)
                    },
                ) {
                    Text(
                        text = stringResource(R.string.intro_dialog_positive),
                        color = md_theme_dark_onLightBackground
                    )
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        setShowDialog(false)
                    },
                ) {
                    Text(
                        text = stringResource(R.string.intro_dialog_negative),
                        color = md_theme_dark_onLightBackground
                    )
                }
            },
        )
    }
}

@Composable
internal fun IntroText() {
    Text(
        stringResource(R.string.welcome_statement),
        color = md_theme_dark_onBackground,
        fontSize = 20.sp,
        fontFamily = jost,
        fontWeight = FontWeight.Light,
        modifier = Modifier.padding(horizontal = 24.dp)
    )
}

@Composable
internal fun LetsGoButton(setShowDialog: (Boolean) -> Unit) {
    Button(
        onClick = { setShowDialog(true) },
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .size(width = 200.dp, height = 100.dp)
            .padding(top = 24.dp)
    ) {
        Text(
            stringResource(R.string.entry_button),
            fontSize = 22.sp,
            fontFamily = jost,
        )
    }
}

@Preview(device = Devices.PIXEL_4, showBackground = true)
@Composable
internal fun PreviewIntroScreen() {
    PadawanTheme {
        IntroScreen(rememberNavController())
    }
}
