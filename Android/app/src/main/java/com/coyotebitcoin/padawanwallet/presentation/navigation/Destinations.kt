/*
 * Copyright 2020-2025 thunderbiscuit and contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE file.
 */

package com.coyotebitcoin.padawanwallet.presentation.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
sealed class CoreDestinations : NavKey {
    @Serializable
    data object WalletRootScreen : CoreDestinations()
    @Serializable
    data object LessonsRootScreen : CoreDestinations()
    @Serializable
    data object SettingsRootScreen : CoreDestinations()
}

@Serializable
sealed class SecondaryDestinations : NavKey {
    // Onboarding screens
    @Serializable
    data object OnboardingScreens : SecondaryDestinations()
    @Serializable
    data object WalletRecoveryScreen : SecondaryDestinations()

    // Core screens (Wallet, Lessons, More)
    @Serializable
    data object CoreScreens : SecondaryDestinations()

    // Wallet screens
    @Serializable
    data object ReceiveScreen : SecondaryDestinations()
    @Serializable
    data object SendScreen : SecondaryDestinations()
    @Serializable
    data object TransactionScreen : SecondaryDestinations()
    @Serializable
    data object QrScanScreen : SecondaryDestinations()

    // Chapters screens
    @Serializable
    data class ChapterScreen(val chapter: Int) : SecondaryDestinations()

    // More screens
    @Serializable
    data object RecoveryPhraseScreen : SecondaryDestinations()
    @Serializable
    data object SendCoinsBackScreen : SecondaryDestinations()
    @Serializable
    data object LanguagesScreen : SecondaryDestinations()
    @Serializable
    data object AboutScreen : SecondaryDestinations()
}
