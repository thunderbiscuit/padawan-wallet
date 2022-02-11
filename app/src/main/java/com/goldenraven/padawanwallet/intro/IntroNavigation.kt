/*
 * Copyright 2020 thunderbiscuit and contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE file.
 */

package com.goldenraven.padawanwallet.intro

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun IntroNavigation() {
    val navController: NavHostController = rememberNavController()

    NavHost(navController = navController, startDestination = Screen.IntroScreen.route) {
        composable(route = Screen.IntroScreen.route) {
            IntroScreen(navController = navController)
        }
        composable(route = Screen.WalletChoiceScreen.route) {
            WalletChoiceScreen(navController = navController)
        }
        composable(route = Screen.WalletRecoveryScreen.route) {
            WalletRecoveryScreen()
        }
    }
}
