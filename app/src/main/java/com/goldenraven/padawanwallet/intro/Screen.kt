package com.goldenraven.padawanwallet.intro

sealed class Screen(val route: String) {
    object IntroScreen : Screen("intro_screen")
    object WalletChoiceScreen : Screen("wallet_choice_screen")
    object WalletRecoveryScreen : Screen("wallet_recovery_screen")
}
