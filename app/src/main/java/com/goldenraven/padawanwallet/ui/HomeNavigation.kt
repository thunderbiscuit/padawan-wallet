/*
 * Copyright 2020-2022 thunderbiscuit and contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE file.
 */

package com.goldenraven.padawanwallet.ui

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.goldenraven.padawanwallet.ui.tutorials.TutorialViewModel
import com.goldenraven.padawanwallet.ui.wallet.WalletViewModel
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun HomeNavigation(
    walletViewModel: WalletViewModel = viewModel(),
    tutorialViewModel: TutorialViewModel = viewModel()
) {
    val navController: NavHostController = rememberAnimatedNavController()
    val animationDuration = 400

    AnimatedNavHost(
        navController = navController,
        startDestination = Screen.SplashScreen.route,
    ) {

        // Splash
        composable(
            route = Screen.SplashScreen.route
        ) { SplashScreen(navController = navController, moveToIntro = false) }


        // Home
        composable(
            route = Screen.HomeScreen.route,
            popEnterTransition = {
                slideIntoContainer(AnimatedContentScope.SlideDirection.End, animationSpec = tween(animationDuration))
            },
            exitTransition = {
                slideOutOfContainer(AnimatedContentScope.SlideDirection.Start, animationSpec = tween(animationDuration))
            },
        ) { HomeScreen(walletViewModel = walletViewModel, tutorialViewModel = tutorialViewModel) }


        // // About
        // composable(
        //     route = Screen.AboutScreen.route,
        //     popEnterTransition = {
        //         slideIntoContainer(AnimatedContentScope.SlideDirection.End, animationSpec = tween(animationDuration))
        //     },
        //     exitTransition = {
        //         slideOutOfContainer(AnimatedContentScope.SlideDirection.Down, animationSpec = tween(animationDuration))
        //     },
        // ) { AboutScreen(navController = navController) }


        // // Settings
        // composable(
        //     route = Screen.SettingsScreen.route,
        //     popEnterTransition = {
        //         slideIntoContainer(AnimatedContentScope.SlideDirection.End, animationSpec = tween(animationDuration))
        //     },
        //     exitTransition = {
        //         slideOutOfContainer(AnimatedContentScope.SlideDirection.Down, animationSpec = tween(animationDuration))
        //     },
        // ) { SettingsScreen(navController = navController) }


        // // Recovery Phrase
        // composable(
        //     route = Screen.RecoveryPhrase.route,
        //     popEnterTransition = {
        //         slideIntoContainer(AnimatedContentScope.SlideDirection.End, animationSpec = tween(animationDuration))
        //     },
        //     exitTransition = {
        //         slideOutOfContainer(AnimatedContentScope.SlideDirection.Down, animationSpec = tween(animationDuration))
        //     },
        // ) { RecoveryPhraseScreen(navController = navController) }


        // // Send your coins back
        // composable(
        //     route = Screen.SendCoinsBackScreen.route,
        //     popEnterTransition = {
        //         slideIntoContainer(AnimatedContentScope.SlideDirection.End, animationSpec = tween(animationDuration))
        //     },
        //     exitTransition = {
        //         slideOutOfContainer(AnimatedContentScope.SlideDirection.Down, animationSpec = tween(animationDuration))
        //     },
        // ) { SendCoinsBackScreen(navController = navController) }
    }
}
