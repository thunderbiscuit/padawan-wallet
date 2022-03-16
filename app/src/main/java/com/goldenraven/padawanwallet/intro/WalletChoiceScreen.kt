/*
 * Copyright 2020-2022 thunderbiscuit and contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE file.
 */

package com.goldenraven.padawanwallet.intro

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.goldenraven.padawanwallet.R
import com.goldenraven.padawanwallet.data.Wallet
import com.goldenraven.padawanwallet.theme.*
import com.goldenraven.padawanwallet.wallet.WalletActivity

@Composable
internal fun WalletChoiceScreen(
    navController: NavController,
    onBuildWalletButtonClicked: (WalletCreateType, String?) -> Unit
) {
    ConstraintLayout {
        val (appName, elevatorPitch, create, alreadyHave) = createRefs()
        val context: Context = LocalContext.current

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize(1f)
                .background(MaterialTheme.colorScheme.background)
        ) {
            Column {
                Text(
                    text = stringResource(R.string.padawan),
                    color = md_theme_dark_primary,
                    fontSize = 70.sp,
                    fontFamily = shareTechMono,
                    modifier = Modifier
                        .padding(top = 70.dp)
                        .align(Alignment.CenterHorizontally)
                )
                Text(
                    stringResource(R.string.elevator_pitch),
                    color = md_theme_dark_onBackground,
                    fontSize = 14.sp,
                    fontFamily = jost,
                    fontStyle = FontStyle.Italic,
                    fontWeight = FontWeight.Light,
                    modifier = Modifier.align(Alignment.End)
                )
            }
        }

        Button(
            onClick = { onBuildWalletButtonClicked(WalletCreateType.FROMSCRATCH, null) },
            colors = ButtonDefaults.buttonColors(md_theme_dark_surfaceLight),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .size(width = 300.dp, height = 170.dp)
                .padding(vertical = 8.dp, horizontal = 8.dp)
                .shadow(elevation = 8.dp, shape = RoundedCornerShape(16.dp))
                .constrainAs(create) {
                    bottom.linkTo(alreadyHave.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
        ) {
            Text(
                stringResource(R.string.create_new_wallet),
                fontSize = 22.sp,
                fontFamily = jost,
                textAlign = TextAlign.Center,
                lineHeight = 28.sp,
            )
        }

        Button(
            onClick = { navController.navigate(Screen.WalletRecoveryScreen.route) },
            colors = ButtonDefaults.buttonColors(md_theme_dark_surface),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .size(width = 300.dp, height = 170.dp)
                .padding(vertical = 8.dp, horizontal = 8.dp)
                .shadow(elevation = 8.dp, shape = RoundedCornerShape(16.dp))
                .constrainAs(alreadyHave) {
                    bottom.linkTo(parent.bottom, margin = 100.dp)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
        ) {
            Text(
                stringResource(R.string.already_have_a_wallet),
                fontSize = 22.sp,
                fontFamily = jost,
                textAlign = TextAlign.Center,
                lineHeight = 28.sp,
            )
        }
    }
}


// @Preview(device = Devices.PIXEL_4, showBackground = true)
// @Composable
// internal fun PreviewWalletChoiceScreen() {
//     PadawanTheme {
//         WalletChoiceScreen(rememberNavController(), { } )
//     }
// }
