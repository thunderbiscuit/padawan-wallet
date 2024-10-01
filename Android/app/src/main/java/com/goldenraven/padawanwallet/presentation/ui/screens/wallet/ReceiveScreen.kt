/*
 * Copyright 2020-2024 thunderbiscuit and contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE file.
 */

package com.goldenraven.padawanwallet.presentation.ui.screens.wallet

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.goldenraven.padawanwallet.R
import com.goldenraven.padawanwallet.presentation.theme.ShareTechMono
import com.goldenraven.padawanwallet.presentation.theme.padawan_theme_background
import com.goldenraven.padawanwallet.presentation.theme.padawan_theme_button_primary
import com.goldenraven.padawanwallet.presentation.theme.gradientBackground
import com.goldenraven.padawanwallet.presentation.theme.standardShadow
import com.goldenraven.padawanwallet.presentation.ui.components.LoadingAnimation
import com.goldenraven.padawanwallet.presentation.ui.components.PadawanAppBar
import com.goldenraven.padawanwallet.presentation.ui.components.standardBorder
import com.goldenraven.padawanwallet.utils.ScreenSizeWidth
import com.goldenraven.padawanwallet.utils.addressToQR
import com.goldenraven.padawanwallet.utils.copyToClipboard
import com.goldenraven.padawanwallet.utils.getScreenSizeWidth
import com.goldenraven.padawanwallet.presentation.viewmodels.mvi.ReceiveScreenAction
import com.goldenraven.padawanwallet.presentation.viewmodels.mvi.ReceiveScreenState
import androidx.compose.ui.Alignment.Companion.BottomCenter
import androidx.compose.ui.Alignment.Companion.Center
import com.goldenraven.padawanwallet.utils.QrUiState
import com.goldenraven.padawanwallet.utils.logRecomposition
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

private const val TAG = "ReceiveScreen"

@Composable
internal fun ReceiveScreen(
    state: ReceiveScreenState,
    onAction: (ReceiveScreenAction) -> Unit,
    navController: NavHostController,
) {
    logRecomposition(TAG)
    val snackbarHostState = remember { SnackbarHostState() }
    var qr by remember { mutableStateOf<ImageBitmap?>(null) }

    LaunchedEffect(state.address) {
        if (state.bip21Uri != null) {
            withContext(Dispatchers.IO) {
                qr = addressToQR(state.bip21Uri)
            }
        }
    }

    val qrCodeSize = when (getScreenSizeWidth(LocalConfiguration.current.screenWidthDp)) {
        ScreenSizeWidth.Small -> 220.dp
        ScreenSizeWidth.Phone -> 340.dp
    }

    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            PadawanAppBar(
                title = stringResource(R.string.receive_bitcoin),
                onClick = { navController.popBackStack() }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { scaffoldPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .gradientBackground()
                .padding(scaffoldPadding)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .align(Center)
            ) {
                if (state.qrState == QrUiState.Loading) {
                    LoadingAnimation(circleColor = padawan_theme_background, circleSize = 38.dp)
                } else if (state.qrState == QrUiState.QR && state.bip21Uri != null && state.address != null) {
                    qr?.let {
                        Image(
                            bitmap = it,
                            contentDescription = stringResource(R.string.qr_code),
                            Modifier
                                .size(qrCodeSize)
                                .clickable {
                                    copyToClipboard(
                                        state.address,
                                        context,
                                        scope,
                                        snackbarHostState,
                                        null
                                    )
                                }
                                .padding(12.dp),
                        )
                        Spacer(modifier = Modifier.padding(vertical = 8.dp))
                        Text(
                            modifier = Modifier
                                .clickable {
                                    copyToClipboard(
                                        state.address,
                                        context,
                                        scope,
                                        snackbarHostState,
                                        null
                                    )
                                }
                                .padding(12.dp),
                            text = state.address,
                            fontFamily = ShareTechMono,
                            fontSize = 12.sp
                        )
                    }
                }
            }

            Button(
                onClick = { onAction(ReceiveScreenAction.UpdateAddress) },
                colors = ButtonDefaults.buttonColors(containerColor = padawan_theme_button_primary),
                shape = RoundedCornerShape(20.dp),
                border = standardBorder,
                modifier = Modifier
                    .height(100.dp)
                    .fillMaxWidth(0.9f)
                    .padding(vertical = 8.dp, horizontal = 8.dp)
                    .standardShadow(20.dp)
                    .align(BottomCenter)
            ) {
                Text(
                    text = stringResource(R.string.generate_a_new_address),
                )
            }
        }
    }
}

// @Preview(device = Devices.PIXEL_4, showBackground = true)
// @Composable
// internal fun PreviewIntroScreen() {
//     PadawanTheme {
//         ReceiveScreen(
//             rememberNavController(),
//             WalletViewModel(),
//         )
//     }
// }
