/*
 * Copyright 2020-2022 thunderbiscuit and contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE file.
 */

package com.goldenraven.padawanwallet.ui.wallet

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.goldenraven.padawanwallet.ui.Screen
import com.goldenraven.padawanwallet.ui.settings.AboutScreen
import com.goldenraven.padawanwallet.ui.settings.RecoveryPhraseScreen
import com.goldenraven.padawanwallet.ui.settings.SendCoinsBackScreen
import com.goldenraven.padawanwallet.ui.settings.SettingsScreen
import com.goldenraven.padawanwallet.ui.tutorials.*
import com.goldenraven.padawanwallet.ui.tutorials.TutorialsHomeScreen
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun WalletNavigation(
    navControllerWalletNavigation: NavHostController,
    walletViewModel: WalletViewModel,
    tutorialViewModel: TutorialViewModel
) {
    val animationDuration = 400

    AnimatedNavHost(
        navController = navControllerWalletNavigation,
        startDestination = Screen.WalletScreen.route,
    ) {

        // Wallet
        composable(
            route = Screen.WalletScreen.route,
            enterTransition = {
                when (initialState.destination.route) {
                    "receive_screen" -> fadeIn(animationSpec = tween(1000))
                    "send_screen" -> fadeIn(animationSpec = tween(1000))
                    "wallet_screen" -> null
                    else -> slideIntoContainer(AnimatedContentScope.SlideDirection.End, animationSpec = tween(animationDuration))
                }
            },
            popEnterTransition = {
                when (initialState.destination.route) {
                    "receive_screen" -> fadeIn(animationSpec = tween(1000))
                    "send_screen" -> fadeIn(animationSpec = tween(1000))
                    "wallet_screen" -> null
                    else -> slideIntoContainer(AnimatedContentScope.SlideDirection.End, animationSpec = tween(animationDuration))
                }
            },
            exitTransition = {
                when (targetState.destination.route) {
                    "receive_screen" -> fadeOut(animationSpec = tween(300))
                    "send_screen" -> fadeOut(animationSpec = tween(300))
                    "wallet_screen" -> null
                    else -> slideOutOfContainer(AnimatedContentScope.SlideDirection.Start, animationSpec = tween(animationDuration))
                }
            },
            popExitTransition = {
                when (targetState.destination.route) {
                    "receive_screen" -> fadeOut(animationSpec = tween(300))
                    "send_screen" -> fadeOut(animationSpec = tween(300))
                    "wallet_screen" -> null
                    else -> slideOutOfContainer(AnimatedContentScope.SlideDirection.Start, animationSpec = tween(animationDuration))
                }
            }
        ) { WalletScreen(navController = navControllerWalletNavigation, walletViewModel = walletViewModel) }


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
        ) { ReceiveScreen(navController = navControllerWalletNavigation) }


        // Send
        composable(
            route = Screen.SendScreen.route,
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
        ) { SendScreen(navController = navControllerWalletNavigation, walletViewModel = walletViewModel) }


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
        ) { QRScanScreen(navController = navControllerWalletNavigation) }


        // Tutorials home
        composable(
            route = Screen.TutorialsHomeScreen.route,
            enterTransition = {
                when (initialState.destination.route) {
                    "wallet_screen" -> slideIntoContainer(AnimatedContentScope.SlideDirection.Start, animationSpec = tween(animationDuration))
                    "settings_screen" -> slideIntoContainer(AnimatedContentScope.SlideDirection.End, animationSpec = tween(animationDuration))
                    else -> fadeIn(animationSpec = tween(1000))
                }
            },
            popEnterTransition = {
                when (initialState.destination.route) {
                    "wallet_screen" -> slideIntoContainer(AnimatedContentScope.SlideDirection.Start, animationSpec = tween(animationDuration))
                    "settings_screen" -> slideIntoContainer(AnimatedContentScope.SlideDirection.End, animationSpec = tween(animationDuration))
                    else -> fadeIn(animationSpec = tween(1000))
                }
            },
            exitTransition = {
                when (targetState.destination.route) {
                    "wallet_screen" -> slideOutOfContainer(AnimatedContentScope.SlideDirection.End, animationSpec = tween(animationDuration))
                    "settings_screen" -> slideOutOfContainer(AnimatedContentScope.SlideDirection.Start, animationSpec = tween(animationDuration))
                    else -> fadeOut(animationSpec = tween(300))
                }
            },
            popExitTransition = {
                when (targetState.destination.route) {
                    "wallet_screen" -> slideOutOfContainer(AnimatedContentScope.SlideDirection.End, animationSpec = tween(animationDuration))
                    "settings_screen" -> slideOutOfContainer(AnimatedContentScope.SlideDirection.Start, animationSpec = tween(animationDuration))
                    else -> fadeOut(animationSpec = tween(300))
                }
            }
        ) { TutorialsHomeScreen(tutorialViewModel = tutorialViewModel, navController = navControllerWalletNavigation) }


        // Specific tutorials
        composable(
            route = Screen.TutorialsScreen.route + "/{tutorialId}",
            arguments = listOf(navArgument("tutorialId") { type = NavType.IntType }),
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
        ) { backStackEntry ->
            backStackEntry.arguments?.getInt("tutorialId")?.let {
                TutorialsScreen(tutorialId = it, tutorialViewModel = tutorialViewModel, navController = navControllerWalletNavigation)
            }
        }


        // Settings
        val settingsScreens: List<String> = listOf("about_screen", "recovery_phrase_screen", "send_coins_back_screen", )
        composable(
            route = Screen.SettingsScreen.route,
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
        ) { SettingsScreen(navController = navControllerWalletNavigation) }


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
        ) { AboutScreen() }


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
        ) { RecoveryPhraseScreen() }


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
        ) { SendCoinsBackScreen() }
    }
}
