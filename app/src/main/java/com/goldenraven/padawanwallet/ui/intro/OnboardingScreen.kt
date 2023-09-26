/*
 * Copyright 2020-2023 thunderbiscuit and contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE file.
 */

package com.goldenraven.padawanwallet.ui.intro

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ChainStyle
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavController
import com.goldenraven.padawanwallet.R
import com.goldenraven.padawanwallet.WalletCreateType
import com.goldenraven.padawanwallet.theme.PadawanTypography
import com.goldenraven.padawanwallet.theme.standardShadow
import com.goldenraven.padawanwallet.ui.Screen
import com.goldenraven.padawanwallet.ui.ShowBars
import com.goldenraven.padawanwallet.ui.standardBorder
import com.goldenraven.padawanwallet.utils.ScreenSizeHeight
import com.goldenraven.padawanwallet.utils.getScreenSizeHeight
import androidx.compose.material3.Text
import com.goldenraven.padawanwallet.theme.bodyMediumUnderlined
import com.goldenraven.padawanwallet.theme.noRippleClickable

private const val TAG = "OnboardingScreen"

@Composable
internal fun OnboardingScreen(
    navController: NavController,
    onBuildWalletButtonClicked: (WalletCreateType) -> Unit
) {
    val screenSizeHeight: ScreenSizeHeight = getScreenSizeHeight(LocalConfiguration.current.screenHeightDp)
    val pageScrollState: ScrollState = rememberScrollState()

    ShowBars()

    if (screenSizeHeight == ScreenSizeHeight.Small) {
        SmallOnboarding(
            pageScrollState = pageScrollState,
            navController = navController,
            onBuildWalletButtonClicked = onBuildWalletButtonClicked
        )
    } else {
        PhoneOnboarding(
            navController = navController,
            onBuildWalletButtonClicked = onBuildWalletButtonClicked
        )
    }
}

@Composable
internal fun SmallOnboarding(
    pageScrollState: ScrollState,
    navController: NavController,
    onBuildWalletButtonClicked: (WalletCreateType) -> Unit
) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxSize()
            .background(Color(0xffffffff))
            // .padding(top = 70.dp)
            .verticalScroll(pageScrollState)
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_padawan_colour_foreground),
            contentDescription = stringResource(R.string.padawan_logo),
            Modifier.size(140.dp)
        )
        Text(
            text = stringResource(R.string.padawan_wallet),
            style = PadawanTypography.headlineLarge,
            fontSize = 34.sp,
            color = Color(0xff1f0208),
            modifier = Modifier.padding(start = 24.dp, end = 24.dp, bottom = 8.dp)
        )

        Text(
            stringResource(R.string.welcome_statement),
            style = PadawanTypography.bodyMedium,
            fontSize = 16.sp,
            color = Color(0xff787878),
            modifier = Modifier.padding(start = 24.dp, end = 24.dp, bottom = 12.dp)
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
                .padding(top = 4.dp, start = 4.dp, end = 4.dp, bottom = 4.dp)
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
                    text = stringResource(R.string.create_a_wallet),
                    // style = GargoyleTypography.labelLarge,
                    color = Color(0xff000000)
                )
                // Spacer(modifier = Modifier.width(8.dp))
            }
        }

        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.Bottom,
            modifier = Modifier
                .padding(bottom = 24.dp)
        ) {
            Text(
                text = stringResource(id = R.string.already_have_a_wallet),
                color = Color(0xff787878),
                modifier = Modifier
                    .padding(start = 24.dp, bottom = 8.dp)
                    .height(40.dp)
            )
            Text(
                modifier = Modifier
                    .padding(start = 8.dp, bottom = 8.dp)
                    .height(40.dp)
                    .noRippleClickable { navController.navigate(Screen.WalletRecoveryScreen.route) },
                text = stringResource(R.string.recover_it_here),
                color = Color(0xff787878),
                style = bodyMediumUnderlined
            )
        }
    }
}

@Composable
internal fun PhoneOnboarding(
    navController: NavController,
    onBuildWalletButtonClicked: (WalletCreateType) -> Unit
) {
    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xffffffff))
    ) {
        val (header, body, recover) = createRefs()
        val verticalChain = createVerticalChain(header, body, recover, chainStyle = ChainStyle.SpreadInside)

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
                painter = painterResource(id = R.drawable.ic_padawan_colour_foreground),
                contentDescription = stringResource(id = R.string.padawan_logo),
                Modifier.size(140.dp)
            )
            // Icon(
            //     painter = painterResource(id = R.drawable.ic_keyring),
            //     contentDescription = "Keyring logo",
            //     tint = Color(0xff000000),
            //     modifier = Modifier.size(80.dp)
            // )
            Text(
                text = stringResource(R.string.padawan_wallet),
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
                    .padding(top = 4.dp, start = 4.dp, end = 4.dp, bottom = 4.dp)
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
                        text = stringResource(R.string.create_a_wallet),
                        // style = GargoyleTypography.labelLarge,
                        color = Color(0xff000000)
                    )
                    // Spacer(modifier = Modifier.width(8.dp))
                }
            }
        }

        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
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
                text = stringResource(id = R.string.already_have_a_wallet),
                color = Color(0xff787878),
                modifier = Modifier
                    .padding(bottom = 8.dp)
                    .height(40.dp)
            )
            Text(
                modifier = Modifier
                    .padding(start = 8.dp, bottom = 8.dp)
                    .height(40.dp)
                    .noRippleClickable { navController.navigate(Screen.WalletRecoveryScreen.route) },
                text = stringResource(R.string.recover_it_here),
                color = Color(0xff787878),
                style = bodyMediumUnderlined
            )
        }
    }
}

// @Preview(device = Devices.PIXEL_4, showBackground = true)
// @Composable
// internal fun PreviewIntroScreen() {
//     PadawanTheme {
//         IntroScreen(rememberNavController())
//     }
// }
