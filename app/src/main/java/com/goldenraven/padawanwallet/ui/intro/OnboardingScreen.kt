/*
 * Copyright 2020-2022 thunderbiscuit and contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE file.
 */

package com.goldenraven.padawanwallet.ui.intro

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.AlertDialog
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ChainStyle
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavController
import com.goldenraven.padawanwallet.R
import com.goldenraven.padawanwallet.WalletCreateType
import com.goldenraven.padawanwallet.theme.*
import com.goldenraven.padawanwallet.ui.Screen
import com.goldenraven.padawanwallet.ui.ShowBars
import com.goldenraven.padawanwallet.ui.standardBorder

private const val TAG = "OnboardingScreen"

@Composable
internal fun OnboardingScreen(
    navController: NavController,
    onBuildWalletButtonClicked: (WalletCreateType) -> Unit
) {

    ShowBars()

    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xffffffff))
    ) {
        val (header, body, recover) = createRefs()
        val verticalChain =
            createVerticalChain(header, body, recover, chainStyle = ChainStyle.SpreadInside)

        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 70.dp)
                .constrainAs(header) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    bottom.linkTo(body.top)
                }
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_logo_v2_0_0),
                contentDescription = "Padawan Logo",
                Modifier.size(140.dp)
            )
            // Icon(
            //     painter = painterResource(id = R.drawable.ic_keyring),
            //     contentDescription = "Keyring logo",
            //     tint = Color(0xff000000),
            //     modifier = Modifier.size(80.dp)
            // )
            Text(
                text = "Padawan Wallet",
                style = PadawanTypography.headlineLarge,
                fontSize = 34.sp,
                color = Color(0xff1f0208),
                modifier = Modifier
                    .padding(start = 24.dp, end = 24.dp, bottom = 8.dp)
            )
        }

        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.Start,
            modifier = Modifier
                .constrainAs(body) {
                    top.linkTo(header.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    bottom.linkTo(recover.top)
                }
        ) {
            Text(
                stringResource(R.string.welcome_statement),
                style = PadawanTypography.bodyMedium,
                fontSize = 16.sp,
                color = Color(0xff787878),
                modifier = Modifier
                    .padding(start = 24.dp, end = 24.dp, bottom = 12.dp)
            )

            Button(
                onClick = {
                    Log.i(TAG, "Creating a wallet")
                    onBuildWalletButtonClicked(WalletCreateType.FROMSCRATCH)
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xfff6cf47)),
                shape = RoundedCornerShape(20.dp),
                border = standardBorder,
                modifier = Modifier
                    .padding(top = 4.dp, start = 4.dp, end = 4.dp, bottom = 24.dp)
                    .standardShadow(20.dp)
                    .height(70.dp)
                    .width(240.dp)
                    .align(Alignment.CenterHorizontally)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(vertical = 4.dp)
                ) {
                    Text(
                        text = "Create a wallet",
                        // style = GargoyleTypography.labelLarge,
                        color = Color(0xff000000)
                    )
                    // Spacer(modifier = Modifier.width(8.dp))
                }
            }
        }

        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.Bottom,
            modifier = Modifier
                .constrainAs(recover) {
                    top.linkTo(body.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom)
                }
                .padding(bottom = 24.dp)
        ) {
            Text(
                text = "Already have a wallet?",
                // style = GargoyleTypography.bodyMedium,
                color = Color(0xff787878),
                modifier = Modifier
                    .padding(start = 24.dp, bottom = 8.dp)
                    .height(40.dp)
            )
            TextButton(
                onClick = {
                    Log.i("OnboardScreen", "Recovering a wallet")
                    navController.navigate(Screen.WalletRecoveryScreen.route)
                },
                modifier = Modifier
                    .width(120.dp)
                    .padding(0.dp)
                    .height(70.dp)
            ) {
                Text(
                    text = "Recover it here",
                    color = Color(0xff787878),
                    style = TextStyle(textDecoration = TextDecoration.Underline)
                )
            }
        }
    }
}

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
            fontFamily = ShareTechMono,
        )
        Text(
            stringResource(R.string.elevator_pitch),
            color = md_theme_dark_onBackground,
            style = PadawanTypography.headlineMedium,
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
                    style = PadawanTypography.bodyMedium,
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
                        style = PadawanTypography.labelLarge,
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
                        style = PadawanTypography.labelLarge,
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
        modifier = Modifier.padding(horizontal = 24.dp),
        style = PadawanTypography.bodyLarge,
        color = md_theme_dark_onBackground,
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
            style = PadawanTypography.labelLarge
        )
    }
}

// @Preview(device = Devices.PIXEL_4, showBackground = true)
// @Composable
// internal fun PreviewIntroScreen() {
//     PadawanTheme {
//         IntroScreen(rememberNavController())
//     }
// }
