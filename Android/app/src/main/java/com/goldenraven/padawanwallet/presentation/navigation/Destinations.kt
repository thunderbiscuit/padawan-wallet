/*
 * Copyright 2020-2024 thunderbiscuit and contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE file.
 */

package com.goldenraven.padawanwallet.presentation.navigation

import kotlinx.serialization.Serializable

interface Destination

// Onboarding screens
@Serializable
object OnboardingScreen : Destination
@Serializable
object WalletRecoveryScreen : Destination

// Wallet screens
@Serializable
object MainScreen : Destination
@Serializable
object WalletRootScreen : Destination
@Serializable
object ReceiveScreen : Destination
@Serializable
object SendScreen : Destination
@Serializable
object TransactionScreen : Destination
@Serializable
object QRScanScreen : Destination

// Chapters screens
@Serializable
object ChaptersRootScreen : Destination
@Serializable
object ChapterScreen : Destination

// Settings screens
@Serializable
object SettingsRootScreen : Destination
@Serializable
object RecoveryPhraseScreen : Destination
@Serializable
object SendCoinsBackScreen : Destination
@Serializable
object LanguagesScreen : Destination
@Serializable
object AboutScreen : Destination
