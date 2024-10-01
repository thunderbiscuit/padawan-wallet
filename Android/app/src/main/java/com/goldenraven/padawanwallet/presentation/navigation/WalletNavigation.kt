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
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
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

private const val TAG = "WalletNavigation"

@Composable
fun WalletNavigation(
    paddingValues: PaddingValues,
    navHostController: NavHostController,
) {
    val walletViewModel: WalletViewModel = viewModel()
    val chaptersViewModel: ChaptersViewModel = viewModel()
    val receiveViewModel: ReceiveViewModel = viewModel()
    val animationDuration = 400
    val slowAnimationDuration = 1000

    NavHost(
        navController = navHostController,
        startDestination = WalletRootScreen,
        // modifier = Modifier.padding(paddingValues),
    ) {
        // Wallet
        val fades: List<String> = listOf("ReceiveScreen", "SendScreen", "TransactionScreen")

        composable<WalletRootScreen>(
            enterTransition = {
                when (initialState.destination.route?.substringAfterLast(".")) {
                    in fades -> fadeIn(animationSpec = tween(animationDuration))
                    else -> slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.End, animationSpec = tween(animationDuration))
                }
            },
            popEnterTransition = {
                when (initialState.destination.route?.substringAfterLast(".")) {
                    in fades -> fadeIn(animationSpec = tween(animationDuration))
                    else -> slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.End, animationSpec = tween(animationDuration))
                }
            },
            exitTransition = {
                when (targetState.destination.route?.substringAfterLast(".")) {
                    in fades -> fadeOut(animationSpec = tween(slowAnimationDuration))
                    else -> slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Start, animationSpec = tween(animationDuration))
                }
            },
            popExitTransition = {
                when (targetState.destination.route?.substringAfterLast(".")) {
                    in fades -> fadeOut(animationSpec = tween(slowAnimationDuration))
                    else -> slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Start, animationSpec = tween(animationDuration))
                }
            }
        ) {
            WalletRootScreen(
                state = walletViewModel.walletState,
                onAction = walletViewModel::onAction,
                navController = navHostController,
                paddingValues = paddingValues,
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
                state = receiveViewModel.state,
                onAction = receiveViewModel::onAction,
                navController = navHostController,
            )
        }

        // Send
        composable<SendScreen>(
            enterTransition = {
                when (initialState.destination.route?.substringAfterLast(".")) {
                    "QRScanScreen" -> fadeIn(animationSpec = tween(400))
                    else           -> slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Up, animationSpec = tween(animationDuration))
                }
            },
            popEnterTransition = {
                when (initialState.destination.route?.substringAfterLast(".")) {
                    "QRScanScreen" -> fadeIn(animationSpec = tween(400))
                    else           -> slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Up, animationSpec = tween(animationDuration))
                }
            },
            exitTransition = {
                when (targetState.destination.route?.substringAfterLast(".")) {
                    "QRScanScreen" -> fadeOut(animationSpec = tween(400))
                    else           -> slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Down, animationSpec = tween(animationDuration))
                }
            },
            popExitTransition = {
                when (targetState.destination.route?.substringAfterLast(".")) {
                    "QRScanScreen" -> fadeOut(animationSpec = tween(400))
                    else           -> slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Down, animationSpec = tween(animationDuration))
                }
            },
        ) {
            SendScreen(
                state = walletViewModel.walletState,
                onAction = walletViewModel::onAction,
                navController = navHostController,
                paddingValues = paddingValues,
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
        ) {
            QRScanScreen(
                onAction = walletViewModel::onAction,
                navController = navHostController
            )
        }


        // Chapters Root Screen
        composable<ChaptersRootScreen>(
            enterTransition = {
                when (initialState.destination.route?.substringAfterLast(".")) {
                    "WalletRootScreen"   -> slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Start, animationSpec = tween(animationDuration))
                    "SettingsRootScreen" -> slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.End, animationSpec = tween(animationDuration))
                    else -> fadeIn(animationSpec = tween(1000))
                }
            },
            popEnterTransition = {
                when (initialState.destination.route?.substringAfterLast(".")) {
                    "WalletRootScreen"   -> slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Start, animationSpec = tween(animationDuration))
                    "SettingsRootScreen" -> slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.End, animationSpec = tween(animationDuration))
                    else -> fadeIn(animationSpec = tween(1000))
                }
            },
            exitTransition = {
                when (targetState.destination.route?.substringAfterLast(".")) {
                    "WalletRootScreen"   -> slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.End, animationSpec = tween(animationDuration))
                    "SettingsRootScreen" -> slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Start, animationSpec = tween(animationDuration))
                    else -> fadeOut(animationSpec = tween(300))
                }
            },
            popExitTransition = {
                when (targetState.destination.route?.substringAfterLast(".")) {
                    "WalletRootScreen"   -> slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.End, animationSpec = tween(animationDuration))
                    "SettingsRootScreen" -> slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Start, animationSpec = tween(animationDuration))
                    else -> fadeOut(animationSpec = tween(300))
                }
            }
        ) {
            ChaptersRootScreen(
                state = chaptersViewModel.rootState,
                onAction = chaptersViewModel::onAction,
                paddingValues = paddingValues,
                navController = navHostController
            )
        }


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
        ) {
            ChapterScreen(
                state = chaptersViewModel.pageState,
                onAction = chaptersViewModel::onAction,
                paddingValues = paddingValues,
                navController = navHostController
            )
        }


        // Settings
        val settingsScreens: List<String> = listOf("RecoveryPhraseScreen", "SendCoinsBackScreen", "LanguagesScreen", "AboutScreen")
        composable<SettingsRootScreen>(
            enterTransition = {
                when (initialState.destination.route?.substringAfterLast(".")) {
                    in settingsScreens -> fadeIn(animationSpec = tween(400))
                    else               -> slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Start, animationSpec = tween(animationDuration))
                }
            },
            popEnterTransition = {
                when (initialState.destination.route?.substringAfterLast(".")) {
                    in settingsScreens -> fadeIn(animationSpec = tween(400))
                    else               -> slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Start, animationSpec = tween(animationDuration))
                }
            },
            exitTransition = {
                when (targetState.destination.route?.substringAfterLast(".")) {
                    in settingsScreens -> fadeOut(animationSpec = tween(400))
                    else               -> slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.End, animationSpec = tween(animationDuration))
                }
            },
            popExitTransition = {
                when (targetState.destination.route?.substringAfterLast(".")) {
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
        ) {
            AboutScreen(
                navController = navHostController
            )
        }


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
        ) {
            RecoveryPhraseScreen(
                navController = navHostController
            )
        }


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
        ) {
            SendCoinsBackScreen(
                navController = navHostController
            )
        }

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
        ) {
            LanguagesScreen(
                navController = navHostController
            )
        }
    }
}
