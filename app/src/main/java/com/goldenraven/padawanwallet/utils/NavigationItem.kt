/*
 * Copyright 2020-2023 thunderbiscuit and contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE file.
 */

package com.goldenraven.padawanwallet.utils

import com.goldenraven.padawanwallet.R
import com.goldenraven.padawanwallet.ui.Screen

sealed class NavigationItem(val route: String, val iconOutline: Int, val title: String, val group: Set<String>) {
    object Home : NavigationItem(
        route = Screen.WalletRootScreen.route,
        iconOutline = R.drawable.ic_hicon_wallet,
        title = "Wallet",
        group = setOf("wallet_screen", "receive_screen", "send_screen", "transaction_screen/txid={txid}", "qr_scan_screen")
    )

    object Chapters : NavigationItem(
        route = Screen.ChaptersRootScreen.route,
        iconOutline = R.drawable.ic_hicon_education,
        title = "Learn",
        group = setOf("chapters_home_screen", "chapter_screen/{chapterId}")
    )
    object Settings : NavigationItem(
        route = Screen.SettingsRootScreen.route,
        iconOutline = R.drawable.ic_hicon_menu,
        title = "Menu",
        group = setOf("settings_root_screen", "about_screen", "recovery_phrase_screen", "send_coins_back_screen")
    )
}
