/*
 * Copyright 2020-2024 thunderbiscuit and contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE file.
 */

package com.goldenraven.padawanwallet.presentation.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.NavHostController
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.PaddingValues
import androidx.lifecycle.viewmodel.compose.viewModel
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

@Composable
fun WalletNavigation(
    paddingValues: PaddingValues,
    navHostController: NavHostController,
) {
    val walletViewModel: WalletViewModel = viewModel()
    val chaptersViewModel: ChaptersViewModel = viewModel()
    val receiveViewModel: ReceiveViewModel = viewModel()
    val animationDuration = 400

    NavHost(
        navController = navHostController,
        startDestination = WalletRootScreen,
    ) {
        // Wallet
        composable<WalletRootScreen>(
            enterTransition = {
                val route = initialState.destination.route
                if (route == null) {
                    slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.End, animationSpec = tween(animationDuration))
                } else if (route.contains("receive") || route.contains("send") || route.contains("transaction")) {
                    fadeIn(animationSpec = tween(1000))
                } else {
                    slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.End, animationSpec = tween(animationDuration))
                }
            },
            popEnterTransition = {
                val route = initialState.destination.route
                if (route == null) {
                    slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.End, animationSpec = tween(animationDuration))
                } else if (route.contains("receive") || route.contains("send") || route.contains("transaction")) {
                    fadeIn(animationSpec = tween(1000))
                } else {
                    slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.End, animationSpec = tween(animationDuration))
                }
            },
            exitTransition = {
                val route = targetState.destination.route
                if (route == null) {
                    slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Start, animationSpec = tween(animationDuration))
                } else if (route.contains("receive") || route.contains("send") || route.contains("transaction")) {
                    fadeOut(animationSpec = tween(300))
                } else {
                    slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Start, animationSpec = tween(animationDuration))
                }
            },
            popExitTransition = {
                val route = targetState.destination.route
                if (route == null) {
                    slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Start, animationSpec = tween(animationDuration))
                } else if (route.contains("receive") || route.contains("send") || route.contains("transaction")) {
                    fadeOut(animationSpec = tween(300))
                } else {
                    slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Start, animationSpec = tween(animationDuration))
                }
            }
        ) {
            WalletRootScreen(
                state = walletViewModel.walletState,
                onAction = walletViewModel::onAction,
                paddingValues = paddingValues,
                navController = navHostController,
            )
        }


        // Receive
        composable<ReceiveScreen>(
            enterTransition = {
                slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Up, animationSpec = tween(animationDuration))
            },
            popEnterTransition = {
                slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Up, animationSpec = tween(animationDuration))
            },
            exitTransition = {
                slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Down, animationSpec = tween(animationDuration))
            },
            popExitTransition = {
                slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Down, animationSpec = tween(animationDuration))
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
        composable<SendScreen>(
            enterTransition = {
                when (initialState.destination.route) {
                    in sendScreens -> fadeIn(animationSpec = tween(400))
                    else           -> slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Up, animationSpec = tween(animationDuration))
                }
            },
            popEnterTransition = {
                when (initialState.destination.route) {
                    in sendScreens -> fadeIn(animationSpec = tween(400))
                    else -> slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Up, animationSpec = tween(animationDuration))
                }
            },
            exitTransition = {
                when (targetState.destination.route) {
                    in sendScreens -> fadeOut(animationSpec = tween(400))
                    else           -> slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Down, animationSpec = tween(animationDuration))
                }
            },
            popExitTransition = {
                when (targetState.destination.route) {
                    in sendScreens -> fadeOut(animationSpec = tween(400))
                    else           -> slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Down, animationSpec = tween(animationDuration))
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
        composable<TransactionScreen>(
            enterTransition = {
                slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Up, animationSpec = tween(animationDuration))
            },
            exitTransition = {
                slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Down, animationSpec = tween(animationDuration))
            },
            popEnterTransition = {
                slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Up, animationSpec = tween(animationDuration))
            },
            popExitTransition = {
                slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Down, animationSpec = tween(animationDuration))
            }
        ) {
            // TODO: Check that this will work by not navigating if txDetails is null
            val singleTxDetails = walletViewModel.getSingleTxDetails()
            if (singleTxDetails != null) {
                TransactionScreen(singleTxDetails, navHostController)
            }
        }

        // QR Scanner Screen
        composable<QRScanScreen>(
            enterTransition = {
                slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Up, animationSpec = tween(animationDuration))
            },
            popEnterTransition = {
                slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Up, animationSpec = tween(animationDuration))
            },
            exitTransition = {
                slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Down, animationSpec = tween(animationDuration))
            },
            popExitTransition = {
                slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Down, animationSpec = tween(animationDuration))
            }
        ) { QRScanScreen(onAction = walletViewModel::onAction, navController = navHostController) }


        // Chapters home
        composable<ChaptersRootScreen>(
            enterTransition = {
                when (initialState.destination.route) {
                    "wallet_screen" -> slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Start, animationSpec = tween(animationDuration))
                    "settings_root_screen" -> slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.End, animationSpec = tween(animationDuration))
                    else -> fadeIn(animationSpec = tween(1000))
                }
            },
            popEnterTransition = {
                when (initialState.destination.route) {
                    "wallet_screen" -> slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Start, animationSpec = tween(animationDuration))
                    "settings_root_screen" -> slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.End, animationSpec = tween(animationDuration))
                    else -> fadeIn(animationSpec = tween(1000))
                }
            },
            exitTransition = {
                when (targetState.destination.route) {
                    "wallet_screen" -> slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.End, animationSpec = tween(animationDuration))
                    "settings_root_screen" -> slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Start, animationSpec = tween(animationDuration))
                    else -> fadeOut(animationSpec = tween(300))
                }
            },
            popExitTransition = {
                when (targetState.destination.route) {
                    "wallet_screen" -> slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.End, animationSpec = tween(animationDuration))
                    "settings_root_screen" -> slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Start, animationSpec = tween(animationDuration))
                    else -> fadeOut(animationSpec = tween(300))
                }
            }
        ) { ChaptersRootScreen(chaptersViewModel.rootState, chaptersViewModel::onAction, navHostController) }


        // Specific chapters
        composable<ChapterScreen>(
            enterTransition = {
                slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Up, animationSpec = tween(animationDuration))
            },
            popEnterTransition = {
                slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Up, animationSpec = tween(animationDuration))
            },
            exitTransition = {
                slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Down, animationSpec = tween(animationDuration))
            },
            popExitTransition = {
                slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Down, animationSpec = tween(animationDuration))
            }
        ) { ChapterScreen(chaptersViewModel.pageState, chaptersViewModel::onAction, navHostController) }


        // Settings
        val settingsScreens: List<String> = listOf("about_screen", "recovery_phrase_screen", "send_coins_back_screen", "languages_screen")
        composable<SettingsRootScreen>(
            enterTransition = {
                when (initialState.destination.route) {
                    in settingsScreens -> fadeIn(animationSpec = tween(400))
                    else               -> slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Start, animationSpec = tween(animationDuration))
                }
            },
            popEnterTransition = {
                when (initialState.destination.route) {
                    in settingsScreens -> fadeIn(animationSpec = tween(400))
                    else               -> slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Start, animationSpec = tween(animationDuration))
                }
            },
            exitTransition = {
                when (targetState.destination.route) {
                    in settingsScreens -> fadeOut(animationSpec = tween(400))
                    else               -> slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.End, animationSpec = tween(animationDuration))
                }
            },
            popExitTransition = {
                when (targetState.destination.route) {
                    in settingsScreens -> fadeOut(animationSpec = tween(400))
                    else               -> slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.End, animationSpec = tween(animationDuration))
                }
            },
        ) {
            SettingsRootScreen(
                onAction = chaptersViewModel::onAction,
                navController = navHostController
            )
        }


        // About
        composable<AboutScreen>(
            enterTransition = {
                slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Up, animationSpec = tween(animationDuration))
            },
            popEnterTransition = {
                slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Up, animationSpec = tween(animationDuration))
            },
            exitTransition = {
                slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Down, animationSpec = tween(animationDuration))
            },
            popExitTransition = {
                slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Down, animationSpec = tween(animationDuration))
            }
        ) { AboutScreen(navController = navHostController) }


        // Recovery phrase
        composable<RecoveryPhraseScreen>(
            enterTransition = {
                slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Up, animationSpec = tween(animationDuration))
            },
            popEnterTransition = {
                slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Up, animationSpec = tween(animationDuration))
            },
            exitTransition = {
                slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Down, animationSpec = tween(animationDuration))
            },
            popExitTransition = {
                slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Down, animationSpec = tween(animationDuration))
            }
        ) { RecoveryPhraseScreen(navController = navHostController) }


        // Send coins back
        composable<SendCoinsBackScreen>(
            enterTransition = {
                slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Up, animationSpec = tween(animationDuration))
            },
            popEnterTransition = {
                slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Up, animationSpec = tween(animationDuration))
            },
            exitTransition = {
                slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Down, animationSpec = tween(animationDuration))
            },
            popExitTransition = {
                slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Down, animationSpec = tween(animationDuration))
            }
        ) { SendCoinsBackScreen(navController = navHostController) }

        // Language
        composable<LanguagesScreen>(
            enterTransition = {
                slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Up, animationSpec = tween(animationDuration))
            },
            popEnterTransition = {
                slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Up, animationSpec = tween(animationDuration))
            },
            exitTransition = {
                slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Down, animationSpec = tween(animationDuration))
            },
            popExitTransition = {
                slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Down, animationSpec = tween(animationDuration))
            }
        ) { LanguagesScreen(navController = navHostController) }
    }
}
