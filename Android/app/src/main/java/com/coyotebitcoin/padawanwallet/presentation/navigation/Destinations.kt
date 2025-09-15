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
    object OnboardingScreens : SecondaryDestinations()
    @Serializable
    object WalletRecoveryScreen : SecondaryDestinations()

    // Core screens (Wallet, Lessons, More)
    @Serializable
    object CoreScreens : SecondaryDestinations()

    // Wallet screens
    @Serializable
    object ReceiveScreen : SecondaryDestinations()
    @Serializable
    object SendScreen : SecondaryDestinations()
    @Serializable
    object TransactionScreen : SecondaryDestinations()
    @Serializable
    object QrScanScreen : SecondaryDestinations()

    // Chapters screens
    @Serializable
    data class ChapterScreen(val chapter: Int) : SecondaryDestinations()

    // More screens
    @Serializable
    object RecoveryPhraseScreen : SecondaryDestinations()
    @Serializable
    object SendCoinsBackScreen : SecondaryDestinations()
    @Serializable
    object LanguagesScreen : SecondaryDestinations()
    @Serializable
    object AboutScreen : SecondaryDestinations()
}
