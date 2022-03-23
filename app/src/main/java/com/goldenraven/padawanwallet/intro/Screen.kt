/*
 * Copyright 2020-2022 thunderbiscuit and contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE file.
 */

package com.goldenraven.padawanwallet.intro

sealed class Screen(val route: String) {
    // intro screens
    object SplashScreen : Screen("splash_screen")
    object IntroScreen : Screen("intro_screen")
    object WalletChoiceScreen : Screen("wallet_choice_screen")
    object WalletRecoveryScreen : Screen("wallet_recovery_screen")

    // wallet screens
    object WalletScreen : Screen("wallet_screen")
}
