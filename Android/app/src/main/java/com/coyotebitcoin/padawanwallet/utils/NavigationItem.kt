/*
 * Copyright 2020-2025 thunderbiscuit and contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE file.
 */

package com.coyotebitcoin.padawanwallet.utils

import androidx.compose.ui.graphics.vector.ImageVector
import com.composables.icons.lucide.GraduationCap
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.Settings2
import com.composables.icons.lucide.Wallet
import com.composables.icons.lucide.WalletMinimal
import com.coyotebitcoin.padawanwallet.R
import com.coyotebitcoin.padawanwallet.presentation.navigation.AboutScreen
import com.coyotebitcoin.padawanwallet.presentation.navigation.ChapterScreen
import com.coyotebitcoin.padawanwallet.presentation.navigation.ChaptersRootScreen
import com.coyotebitcoin.padawanwallet.presentation.navigation.Destination
import com.coyotebitcoin.padawanwallet.presentation.navigation.QRScanScreen
import com.coyotebitcoin.padawanwallet.presentation.navigation.ReceiveScreen
import com.coyotebitcoin.padawanwallet.presentation.navigation.RecoveryPhraseScreen
import com.coyotebitcoin.padawanwallet.presentation.navigation.SendCoinsBackScreen
import com.coyotebitcoin.padawanwallet.presentation.navigation.SendScreen
import com.coyotebitcoin.padawanwallet.presentation.navigation.SettingsRootScreen
import com.coyotebitcoin.padawanwallet.presentation.navigation.TransactionScreen
import com.coyotebitcoin.padawanwallet.presentation.navigation.WalletRootScreen

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
