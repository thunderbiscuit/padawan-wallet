/*
 * Copyright 2020-2022 thunderbiscuit and contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE file.
 */

package com.goldenraven.padawanwallet.ui

sealed class Screen(val route: String) {
    // intro screens
    object SplashScreen : Screen("splash_screen")
    object IntroScreen : Screen("intro_screen")
    object WalletChoiceScreen : Screen("wallet_choice_screen")
    object WalletRecoveryScreen : Screen("wallet_recovery_screen")

    // home screen
    object HomeScreen : Screen("home_screen")

    // wallet screens
    object WalletScreen : Screen("wallet_screen")
    object TutorialsScreen : Screen("tutorials_screen")

    // drawer screens
    object AboutScreen : Screen("about_screen")
    object SettingsScreen : Screen("settings_screen")
    object RecoveryPhrase : Screen("backup_screen")
    object SendCoinsBackScreen : Screen("send_coins_back_screen")
}
