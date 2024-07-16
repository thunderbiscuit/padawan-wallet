/*
 * Copyright 2020-2024 thunderbiscuit and contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE file.
 */

package com.goldenraven.padawanwallet.utils

sealed class Screen(val route: String) {
    // onboarding screens
    data object OnboardingScreen : Screen("onboarding_screen")
    data object WalletRecoveryScreen : Screen("wallet_recovery_screen")

    // Home screen
    data object SplashScreen : Screen("splash_screen")
    data object HomeScreen : Screen("home_screen")

    // Wallet screens
    data object WalletRootScreen : Screen("wallet_screen")
    data object ReceiveScreen : Screen("receive_screen")
    data object SendScreen : Screen("send_screen")
    data object TransactionScreen : Screen("transaction_screen")
    data object QRScanScreen : Screen("qr_scan_screen")

    // Chapters screens
    data object ChaptersRootScreen : Screen("chapters_home_screen")
    data object ChapterScreen : Screen("chapter_screen")

    // Settings screens
    data object SettingsRootScreen : Screen("settings_root_screen")
    data object AboutScreen : Screen("about_screen")
    data object LanguagesScreen : Screen("languages_screen")
    data object RecoveryPhraseScreen : Screen("recovery_phrase_screen")
    data object SendCoinsBackScreen : Screen("send_coins_back_screen")
}
