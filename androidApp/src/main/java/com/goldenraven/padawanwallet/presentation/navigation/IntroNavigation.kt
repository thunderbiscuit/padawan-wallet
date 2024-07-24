/*
 * Copyright 2020-2024 thunderbiscuit and contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE file.
 */

package com.goldenraven.padawanwallet.presentation.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.NavHostController
import com.goldenraven.padawanwallet.presentation.ui.WalletCreateType
import com.goldenraven.padawanwallet.presentation.ui.screens.SplashScreen
import com.goldenraven.padawanwallet.presentation.ui.screens.intro.OnboardingScreen
import com.goldenraven.padawanwallet.presentation.ui.screens.intro.WalletRecoveryScreen

@Composable
fun IntroNavigation(onBuildWalletButtonClicked: (WalletCreateType) -> Unit) {
    val navController: NavHostController = rememberNavController()
    val animationDuration = 400

    NavHost(
        navController = navController,
        startDestination = SplashScreen,
    ) {

        composable<SplashScreen> {
            SplashScreen(navController = navController, moveToIntro = true)
        }

        composable<OnboardingScreen>(
            exitTransition = {
                slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Start, animationSpec = tween(animationDuration))
            },
            popEnterTransition = {
                slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.End, animationSpec = tween(animationDuration))
            },
        ) { OnboardingScreen(navController = navController, onBuildWalletButtonClicked) }

        composable<WalletRecoveryScreen>(
            enterTransition = {
                slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Start, animationSpec = tween(animationDuration))
            },
            popExitTransition = {
                slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.End, animationSpec = tween(animationDuration))
            }
        ) { WalletRecoveryScreen(onBuildWalletButtonClicked) }
    }
}
