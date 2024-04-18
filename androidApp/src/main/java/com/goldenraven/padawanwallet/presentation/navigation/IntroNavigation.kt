/*
 * Copyright 2020-2023 thunderbiscuit and contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE file.
 */

package com.goldenraven.padawanwallet.presentation.navigation

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.goldenraven.padawanwallet.utils.Screen
import com.goldenraven.padawanwallet.presentation.ui.screens.SplashScreen
import com.goldenraven.padawanwallet.presentation.ui.screens.intro.OnboardingScreen
import com.goldenraven.padawanwallet.presentation.ui.screens.intro.WalletRecoveryScreen
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun IntroNavigation(onBuildWalletButtonClicked: (com.goldenraven.padawanwallet.presentation.ui.WalletCreateType) -> Unit) {
    val navController: NavHostController = rememberAnimatedNavController()
    val animationDuration = 400

    AnimatedNavHost(
        navController = navController,
        startDestination = Screen.SplashScreen.route,
    ) {

        composable(
            route = Screen.SplashScreen.route
        ) { SplashScreen(navController = navController, moveToIntro = true) }

        composable(
            route = Screen.OnboardingScreen.route,
            exitTransition = {
                slideOutOfContainer(AnimatedContentScope.SlideDirection.Start, animationSpec = tween(animationDuration))
            },
            popEnterTransition = {
                slideIntoContainer(AnimatedContentScope.SlideDirection.End, animationSpec = tween(animationDuration))
            },
        ) { OnboardingScreen(navController = navController, onBuildWalletButtonClicked) }

        composable(
            route = Screen.WalletRecoveryScreen.route,
            enterTransition = {
                slideIntoContainer(AnimatedContentScope.SlideDirection.Start, animationSpec = tween(animationDuration))
            },
            popExitTransition = {
                slideOutOfContainer(AnimatedContentScope.SlideDirection.End, animationSpec = tween(animationDuration))
            }
        ) { WalletRecoveryScreen(onBuildWalletButtonClicked) }
    }
}
