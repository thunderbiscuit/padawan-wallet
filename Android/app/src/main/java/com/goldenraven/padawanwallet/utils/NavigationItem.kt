/*
 * Copyright 2020-2024 thunderbiscuit and contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE file.
 */

package com.goldenraven.padawanwallet.utils

import androidx.compose.ui.graphics.vector.ImageVector
import com.composables.icons.lucide.GraduationCap
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.Settings2
import com.composables.icons.lucide.Wallet
import com.composables.icons.lucide.WalletMinimal
import com.goldenraven.padawanwallet.R
import com.goldenraven.padawanwallet.presentation.navigation.AboutScreen
import com.goldenraven.padawanwallet.presentation.navigation.ChapterScreen
import com.goldenraven.padawanwallet.presentation.navigation.ChaptersRootScreen
import com.goldenraven.padawanwallet.presentation.navigation.Destination
import com.goldenraven.padawanwallet.presentation.navigation.QRScanScreen
import com.goldenraven.padawanwallet.presentation.navigation.ReceiveScreen
import com.goldenraven.padawanwallet.presentation.navigation.RecoveryPhraseScreen
import com.goldenraven.padawanwallet.presentation.navigation.SendCoinsBackScreen
import com.goldenraven.padawanwallet.presentation.navigation.SendScreen
import com.goldenraven.padawanwallet.presentation.navigation.SettingsRootScreen
import com.goldenraven.padawanwallet.presentation.navigation.TransactionScreen
import com.goldenraven.padawanwallet.presentation.navigation.WalletRootScreen

sealed class NavigationItem(val route: Destination, val icon: ImageVector, val title: String, val group: Set<Destination>) {
    class Home(title: String) : NavigationItem(
        route = WalletRootScreen,
        icon = Lucide.WalletMinimal,
        title = title,
        group = setOf(WalletRootScreen, ReceiveScreen, SendScreen, TransactionScreen, QRScanScreen)
    )

    class Chapters(title: String) : NavigationItem(
        route = ChaptersRootScreen,
        icon = Lucide.GraduationCap,
        title = title,
        group = setOf(ChaptersRootScreen, ChapterScreen)
    )

    class Settings(title: String) : NavigationItem(
        route = SettingsRootScreen,
        icon = Lucide.Settings2,
        title = title,
        group = setOf(SettingsRootScreen, AboutScreen, RecoveryPhraseScreen, SendCoinsBackScreen)
    )
}
