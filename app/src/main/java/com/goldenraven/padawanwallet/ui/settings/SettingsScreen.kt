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
import com.goldenraven.padawanwallet.R
import com.goldenraven.padawanwallet.theme.PadawanTypography
import com.goldenraven.padawanwallet.theme.padawan_theme_background_secondary
import com.goldenraven.padawanwallet.ui.Screen
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.features.auth.*
import io.ktor.client.features.auth.providers.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.http.content.*

private const val TAG = "SettingsScreen"

@Composable
internal fun SettingsScreen(
    navController: NavController
) {
    Column(
        modifier = Modifier
            .padding(top = 48.dp)
            .fillMaxSize()
            // .standardBackground()
            .background(padawan_theme_background_secondary)
    ) {
        // Title
        Text(
            text = "Settings",
            style = PadawanTypography.headlineSmall,
            color = Color(0xff1f0208),
            modifier = Modifier
                .padding(start = 24.dp, end = 24.dp, bottom = 8.dp)
        )
        Text(
            text = "A collection of everything else you need in the app.",
            // style = GargoyleTypography.bodyMedium,
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

        Button(
            onClick = {},
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xffffffff)),
            shape = RoundedCornerShape(8.dp),
            border = BorderStroke(1.dp, Color(0xff2f2f2f)),
            modifier = Modifier
                .size(width = 400.dp, height = 80.dp)
                .padding(start = 24.dp, end = 24.dp, top = 24.dp)
        ) {
            Text(
                text = "Reset completed tutorials",
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
    }
}

// @OptIn(ExperimentalMaterial3Api::class)
// @Composable
// internal fun SettingsScreen(navController: NavController) {
//
//     val faucetCallDone = WalletRepository.wasFaucetCallDone()
//     val coroutineScope = rememberCoroutineScope()
//     val snackbarHostState = remember { SnackbarHostState() }
//     val context = LocalContext.current
//
//     Scaffold(
//         topBar = { DrawerAppBar(navController, title = "Settings") },
//         bottomBar = { SnackbarHost(hostState = snackbarHostState) }
//     ) {
//         Column(
//             Modifier.padding(all = 16.dp)
//         ) {
//             if (!faucetCallDone) {
//                 Button(
//                     onClick = {
//                         callTatooineFaucet(Wallet.getNewAddress(), coroutineScope, snackbarHostState, context)
//                     },
//                     modifier = Modifier.fillMaxWidth()
//                 ) {
//                     Text(text = stringResource(id = R.string.request_testnet_coins))
//                 }
//             } else {
//                 Button(
//                     onClick = {
//                         coroutineScope.launch {
//                             snackbarHostState.showSnackbar("Error: You have already gotten testnet coins before.")
//                         }
//                     },
//                     modifier = Modifier.fillMaxWidth(),
//                     colors = ButtonDefaults.buttonColors(colorResource(id = R.color.bg2))
//                 ) {
//                     Text(text = stringResource(id = R.string.request_testnet_coins))
//                 }
//             }
//         }
//     }
// }
//
//
// private fun callTatooineFaucet(
//     address: AddressInfo,
//     coroutineScope: CoroutineScope,
//     snackbarHostState: SnackbarHostState,
//     context: Context
// ) {
//     val faucetUrl: String = BuildConfig.FAUCET_URL
//     val faucetUsername: String = BuildConfig.FAUCET_USERNAME
//     val faucetPassword: String = BuildConfig.FAUCET_PASSWORD
//
//     coroutineScope.launch {
//         val ktorClient = HttpClient(CIO) {
//             install(Auth) {
//                 basic {
//                     credentials {
//                         BasicAuthCredentials(
//                             username = faucetUsername,
//                             password = faucetPassword
//                         )
//                     }
//                 }
//             }
//         }
//
//         Log.i(TAG, "API call to Tatooine will request coins at $address")
//         try {
//             val response: HttpResponse = ktorClient.post(faucetUrl) {
//                 body = TextContent(address.address, ContentType.Text.Plain)
//             }
//             WalletRepository.faucetCallDone()
//             Log.i(TAG, "API call to Tatooine was performed. Response is ${response.status}, ${response.readText()}")
//         } catch (cause: Throwable) {
//             Log.i(TAG, "Tatooine call failed: $cause")
//             coroutineScope.launch { snackbarHostState.showSnackbar(context.resources.getString(R.string.errorFaucet)) }
//         }
//         ktorClient.close()
//     }
// }