/*
 * Copyright 2020-2024 thunderbiscuit and contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE file.
 */

package com.goldenraven.padawanwallet.presentation.navigation

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.goldenraven.padawanwallet.utils.Screen
import com.goldenraven.padawanwallet.presentation.ui.screens.chapters.ChapterScreen
import com.goldenraven.padawanwallet.presentation.ui.screens.settings.AboutScreen
import com.goldenraven.padawanwallet.presentation.ui.screens.settings.RecoveryPhraseScreen
import com.goldenraven.padawanwallet.presentation.ui.screens.settings.SendCoinsBackScreen
import com.goldenraven.padawanwallet.presentation.ui.screens.settings.SettingsRootScreen
import com.goldenraven.padawanwallet.presentation.ui.screens.chapters.ChaptersRootScreen
import com.goldenraven.padawanwallet.presentation.viewmodels.ChaptersViewModel
import com.goldenraven.padawanwallet.presentation.ui.screens.settings.LanguagesScreen
import com.goldenraven.padawanwallet.presentation.ui.screens.wallet.QRScanScreen
import com.goldenraven.padawanwallet.presentation.ui.screens.wallet.ReceiveScreen
import com.goldenraven.padawanwallet.presentation.ui.screens.wallet.SendScreen
import com.goldenraven.padawanwallet.presentation.ui.screens.wallet.TransactionScreen
import com.goldenraven.padawanwallet.presentation.ui.screens.wallet.WalletRootScreen
import com.goldenraven.padawanwallet.presentation.viewmodels.WalletViewModel
import com.goldenraven.padawanwallet.presentation.viewmodels.ReceiveViewModel
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun WalletNavigation(
    navHostController: NavHostController,
) {
    val walletViewModel: WalletViewModel = viewModel()
    val chaptersViewModel: ChaptersViewModel = viewModel()
    val receiveViewModel: ReceiveViewModel = viewModel()
    val animationDuration = 400

    AnimatedNavHost(
        navController = navHostController,
        startDestination = Screen.WalletRootScreen.route,
    ) {
        // Wallet
        composable(
            route = Screen.WalletRootScreen.route,
            enterTransition = {
                val route = initialState.destination.route
                if (route == null) {
                    slideIntoContainer(AnimatedContentScope.SlideDirection.End, animationSpec = tween(animationDuration))
                } else if (route.contains("receive") || route.contains("send") || route.contains("transaction")) {
                    fadeIn(animationSpec = tween(1000))
                } else {
                    slideIntoContainer(AnimatedContentScope.SlideDirection.End, animationSpec = tween(animationDuration))
                }
            },
            popEnterTransition = {
                val route = initialState.destination.route
                if (route == null) {
                    slideIntoContainer(AnimatedContentScope.SlideDirection.End, animationSpec = tween(animationDuration))
                } else if (route.contains("receive") || route.contains("send") || route.contains("transaction")) {
                    fadeIn(animationSpec = tween(1000))
                } else {
                    slideIntoContainer(AnimatedContentScope.SlideDirection.End, animationSpec = tween(animationDuration))
                }
            },
            exitTransition = {
                val route = targetState.destination.route
                if (route == null) {
                    slideOutOfContainer(AnimatedContentScope.SlideDirection.Start, animationSpec = tween(animationDuration))
                } else if (route.contains("receive") || route.contains("send") || route.contains("transaction")) {
                    fadeOut(animationSpec = tween(300))
                } else {
                    slideOutOfContainer(AnimatedContentScope.SlideDirection.Start, animationSpec = tween(animationDuration))
                }
            },
            popExitTransition = {
                val route = targetState.destination.route
                if (route == null) {
                    slideOutOfContainer(AnimatedContentScope.SlideDirection.Start, animationSpec = tween(animationDuration))
                } else if (route.contains("receive") || route.contains("send") || route.contains("transaction")) {
                    fadeOut(animationSpec = tween(300))
                } else {
                    slideOutOfContainer(AnimatedContentScope.SlideDirection.Start, animationSpec = tween(animationDuration))
                }
            }
        ) {
            WalletRootScreen(
                state = walletViewModel.walletState,
                onAction = walletViewModel::onAction,
                navController = navHostController,
            )
        }


        // Receive
        composable(
            route = Screen.ReceiveScreen.route,
            enterTransition = {
                slideIntoContainer(AnimatedContentScope.SlideDirection.Up, animationSpec = tween(animationDuration))
            },
            popEnterTransition = {
                slideIntoContainer(AnimatedContentScope.SlideDirection.Up, animationSpec = tween(animationDuration))
            },
            exitTransition = {
                slideOutOfContainer(AnimatedContentScope.SlideDirection.Down, animationSpec = tween(animationDuration))
            },
            popExitTransition = {
                slideOutOfContainer(AnimatedContentScope.SlideDirection.Down, animationSpec = tween(animationDuration))
            }
        ) {
            ReceiveScreen(
                navController = navHostController,
                receiveViewModel.state,
                receiveViewModel::onAction
            )
        }


        // Send
        val sendScreens: List<String> = listOf("qr_scan_screen")
        composable(
            route = Screen.SendScreen.route,
            enterTransition = {
                when (initialState.destination.route) {
                    in sendScreens -> fadeIn(animationSpec = tween(400))
                    else           -> slideIntoContainer(AnimatedContentScope.SlideDirection.Up, animationSpec = tween(animationDuration))
                }
            },
            popEnterTransition = {
                when (initialState.destination.route) {
                    in sendScreens -> fadeIn(animationSpec = tween(400))
                    else -> slideIntoContainer(AnimatedContentScope.SlideDirection.Up, animationSpec = tween(animationDuration))
                }
            },
            exitTransition = {
                when (targetState.destination.route) {
                    in sendScreens -> fadeOut(animationSpec = tween(400))
                    else           -> slideOutOfContainer(AnimatedContentScope.SlideDirection.Down, animationSpec = tween(animationDuration))
                }
            },
            popExitTransition = {
                when (targetState.destination.route) {
                    in sendScreens -> fadeOut(animationSpec = tween(400))
                    else           -> slideOutOfContainer(AnimatedContentScope.SlideDirection.Down, animationSpec = tween(animationDuration))
                }
            },
        ) {
            SendScreen(
                state = walletViewModel.walletState,
                onAction = walletViewModel::onAction,
                navController = navHostController,
                // walletViewModel = walletViewModel
            )
        }


        // Transaction screen
        composable(
            route = "${Screen.TransactionScreen.route}/txid={txid}",
            enterTransition = {
                slideIntoContainer(AnimatedContentScope.SlideDirection.Up, animationSpec = tween(animationDuration))
            },
            exitTransition = {
                slideOutOfContainer(AnimatedContentScope.SlideDirection.Down, animationSpec = tween(animationDuration))
            },
            popEnterTransition = {
                slideIntoContainer(AnimatedContentScope.SlideDirection.Up, animationSpec = tween(animationDuration))
            },
            popExitTransition = {
                slideOutOfContainer(AnimatedContentScope.SlideDirection.Down, animationSpec = tween(animationDuration))
            }
        ) { backStackEntry -> backStackEntry.arguments?.getString("txid")?.let {
                TransactionScreen(navHostController, backStackEntry.arguments?.getString("txid"))
            }
        }

        // QR Scanner Screen
        composable(
            route = Screen.QRScanScreen.route,
            enterTransition = {
                slideIntoContainer(AnimatedContentScope.SlideDirection.Up, animationSpec = tween(animationDuration))
            },
            popEnterTransition = {
                slideIntoContainer(AnimatedContentScope.SlideDirection.Up, animationSpec = tween(animationDuration))
            },
            exitTransition = {
                slideOutOfContainer(AnimatedContentScope.SlideDirection.Down, animationSpec = tween(animationDuration))
            },
            popExitTransition = {
                slideOutOfContainer(AnimatedContentScope.SlideDirection.Down, animationSpec = tween(animationDuration))
            }
        ) { QRScanScreen(onAction = walletViewModel::onAction, navController = navHostController) }


        // Chapters home
        composable(
            route = Screen.ChaptersRootScreen.route,
            enterTransition = {
                when (initialState.destination.route) {
                    "wallet_screen" -> slideIntoContainer(AnimatedContentScope.SlideDirection.Start, animationSpec = tween(animationDuration))
                    "settings_root_screen" -> slideIntoContainer(AnimatedContentScope.SlideDirection.End, animationSpec = tween(animationDuration))
                    else -> fadeIn(animationSpec = tween(1000))
                }
            },
            popEnterTransition = {
                when (initialState.destination.route) {
                    "wallet_screen" -> slideIntoContainer(AnimatedContentScope.SlideDirection.Start, animationSpec = tween(animationDuration))
                    "settings_root_screen" -> slideIntoContainer(AnimatedContentScope.SlideDirection.End, animationSpec = tween(animationDuration))
                    else -> fadeIn(animationSpec = tween(1000))
                }
            },
            exitTransition = {
                when (targetState.destination.route) {
                    "wallet_screen" -> slideOutOfContainer(AnimatedContentScope.SlideDirection.End, animationSpec = tween(animationDuration))
                    "settings_root_screen" -> slideOutOfContainer(AnimatedContentScope.SlideDirection.Start, animationSpec = tween(animationDuration))
                    else -> fadeOut(animationSpec = tween(300))
                }
            },
            popExitTransition = {
                when (targetState.destination.route) {
                    "wallet_screen" -> slideOutOfContainer(AnimatedContentScope.SlideDirection.End, animationSpec = tween(animationDuration))
                    "settings_root_screen" -> slideOutOfContainer(AnimatedContentScope.SlideDirection.Start, animationSpec = tween(animationDuration))
                    else -> fadeOut(animationSpec = tween(300))
                }
            }
        ) { ChaptersRootScreen(chaptersViewModel.rootState, chaptersViewModel::onAction, navHostController) }


        // Specific chapters
        composable(
            route = Screen.ChapterScreen.route,
            enterTransition = {
                slideIntoContainer(AnimatedContentScope.SlideDirection.Up, animationSpec = tween(animationDuration))
            },
            popEnterTransition = {
                slideIntoContainer(AnimatedContentScope.SlideDirection.Up, animationSpec = tween(animationDuration))
            },
            exitTransition = {
                slideOutOfContainer(AnimatedContentScope.SlideDirection.Down, animationSpec = tween(animationDuration))
            },
            popExitTransition = {
                slideOutOfContainer(AnimatedContentScope.SlideDirection.Down, animationSpec = tween(animationDuration))
            }
        ) { ChapterScreen(chaptersViewModel.pageState, chaptersViewModel::onAction, navHostController) }


        // Settings
        val settingsScreens: List<String> = listOf("about_screen", "recovery_phrase_screen", "send_coins_back_screen", "languages_screen")
        composable(
            route = Screen.SettingsRootScreen.route,
            enterTransition = {
                when (initialState.destination.route) {
                    in settingsScreens -> fadeIn(animationSpec = tween(400))
                    else               -> slideIntoContainer(AnimatedContentScope.SlideDirection.Start, animationSpec = tween(animationDuration))
                }
            },
            popEnterTransition = {
                when (initialState.destination.route) {
                    in settingsScreens -> fadeIn(animationSpec = tween(400))
                    else -> slideIntoContainer(AnimatedContentScope.SlideDirection.Start, animationSpec = tween(animationDuration))
                }
            },
            exitTransition = {
                when (targetState.destination.route) {
                    in settingsScreens -> fadeOut(animationSpec = tween(400))
                    else               -> slideOutOfContainer(AnimatedContentScope.SlideDirection.End, animationSpec = tween(animationDuration))
                }
            },
            popExitTransition = {
                when (targetState.destination.route) {
                    in settingsScreens -> fadeOut(animationSpec = tween(400))
                    else               -> slideOutOfContainer(AnimatedContentScope.SlideDirection.End, animationSpec = tween(animationDuration))
                }
            },
        ) {
            SettingsRootScreen(
                onAction = chaptersViewModel::onAction,
                navController = navHostController
            )
        }


        // About
        composable(
            route = Screen.AboutScreen.route,
            enterTransition = {
                slideIntoContainer(AnimatedContentScope.SlideDirection.Up, animationSpec = tween(animationDuration))
            },
            popEnterTransition = {
                slideIntoContainer(AnimatedContentScope.SlideDirection.Up, animationSpec = tween(animationDuration))
            },
            exitTransition = {
                slideOutOfContainer(AnimatedContentScope.SlideDirection.Down, animationSpec = tween(animationDuration))
            },
            popExitTransition = {
                slideOutOfContainer(AnimatedContentScope.SlideDirection.Down, animationSpec = tween(animationDuration))
            }
        ) { AboutScreen(navController = navHostController) }


        // Recovery phrase
        composable(
            route = Screen.RecoveryPhraseScreen.route,
            enterTransition = {
                slideIntoContainer(AnimatedContentScope.SlideDirection.Up, animationSpec = tween(animationDuration))
            },
            popEnterTransition = {
                slideIntoContainer(AnimatedContentScope.SlideDirection.Up, animationSpec = tween(animationDuration))
            },
            exitTransition = {
                slideOutOfContainer(AnimatedContentScope.SlideDirection.Down, animationSpec = tween(animationDuration))
            },
            popExitTransition = {
                slideOutOfContainer(AnimatedContentScope.SlideDirection.Down, animationSpec = tween(animationDuration))
            }
        ) { RecoveryPhraseScreen(navController = navHostController) }


        // Send coins back
        composable(
            route = Screen.SendCoinsBackScreen.route,
            enterTransition = {
                slideIntoContainer(AnimatedContentScope.SlideDirection.Up, animationSpec = tween(animationDuration))
            },
            popEnterTransition = {
                slideIntoContainer(AnimatedContentScope.SlideDirection.Up, animationSpec = tween(animationDuration))
            },
            exitTransition = {
                slideOutOfContainer(AnimatedContentScope.SlideDirection.Down, animationSpec = tween(animationDuration))
            },
            popExitTransition = {
                slideOutOfContainer(AnimatedContentScope.SlideDirection.Down, animationSpec = tween(animationDuration))
            }
        ) { SendCoinsBackScreen(navController = navHostController) }

        // Language
        composable(
            route = Screen.LanguagesScreen.route,
            enterTransition = {
                slideIntoContainer(AnimatedContentScope.SlideDirection.Up, animationSpec = tween(animationDuration))
            },
            popEnterTransition = {
                slideIntoContainer(AnimatedContentScope.SlideDirection.Up, animationSpec = tween(animationDuration))
            },
            exitTransition = {
                slideOutOfContainer(AnimatedContentScope.SlideDirection.Down, animationSpec = tween(animationDuration))
            },
            popExitTransition = {
                slideOutOfContainer(AnimatedContentScope.SlideDirection.Down, animationSpec = tween(animationDuration))
            }
        ) { LanguagesScreen(navController = navHostController) }
    }
}
