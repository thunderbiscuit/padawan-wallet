/*
 * Copyright 2020-2024 thunderbiscuit and contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE file.
 */

package com.goldenraven.padawanwallet.presentation.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.goldenraven.padawanwallet.presentation.ui.screens.HomeScreen
// import com.goldenraven.padawanwallet.presentation.ui.screens.SplashScreen

@Composable
fun HomeNavigation() {
    val navController: NavHostController = rememberNavController()
    val animationDuration = 400

    NavHost(
        navController = navController,
        startDestination = HomeScreen,
    ) {

        // Splash
        // composable<SplashScreen> {
        //     SplashScreen(navController = navController, moveToIntro = false)
        // }


        // Home
        composable<HomeScreen>(
            popEnterTransition = {
                slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.End, animationSpec = tween(animationDuration))
            },
            exitTransition = {
                slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Start, animationSpec = tween(animationDuration))
            },
        ) { HomeScreen() }
    }
}
