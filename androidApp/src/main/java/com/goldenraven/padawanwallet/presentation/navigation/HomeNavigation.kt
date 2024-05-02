/*
 * Copyright 2020-2023 thunderbiscuit and contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE file.
 */

package com.goldenraven.padawanwallet.presentation.navigation

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.goldenraven.padawanwallet.utils.Screen
import com.goldenraven.padawanwallet.presentation.ui.screens.HomeScreen
import com.goldenraven.padawanwallet.presentation.ui.screens.SplashScreen
import com.goldenraven.padawanwallet.presentation.viewmodels.ChaptersViewModel
import com.goldenraven.padawanwallet.presentation.viewmodels.WalletViewModel
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun HomeNavigation(
    walletViewModel: WalletViewModel = viewModel(),
    chaptersViewModel: ChaptersViewModel = viewModel()
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
        ) { HomeScreen(walletViewModel = walletViewModel, chaptersViewModel = chaptersViewModel) }
    }
}
