/*
 * Copyright 2020-2023 thunderbiscuit and contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE file.
 */

package com.goldenraven.padawanwallet.ui

sealed class Screen(val route: String) {
    // onboarding screens
    object OnboardingScreen : Screen("onboarding_screen")
    object WalletRecoveryScreen : Screen("wallet_recovery_screen")

    // Home screen
    object SplashScreen : Screen("splash_screen")
    object HomeScreen : Screen("home_screen")

    // Wallet screens
    object WalletRootScreen : Screen("wallet_screen")
    object ReceiveScreen : Screen("receive_screen")
    object SendScreen : Screen("send_screen")
    object TransactionScreen : Screen("transaction_screen")
    object QRScanScreen : Screen("qr_scan_screen")

    // Chapters screens
    object ChaptersRootScreen : Screen("chapters_home_screen")
    object ChapterScreen : Screen("chapter_screen")

    // Settings screens
    object SettingsRootScreen : Screen("settings_root_screen")
    object AboutScreen : Screen("about_screen")
    object RecoveryPhraseScreen : Screen("recovery_phrase_screen")
    object SendCoinsBackScreen : Screen("send_coins_back_screen")
}
