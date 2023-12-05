/*
 * Copyright 2020-2023 thunderbiscuit and contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE file.
 */

package com.goldenraven.padawanwallet.utils

import com.goldenraven.padawanwallet.R

sealed class NavigationItem(val route: String, val iconOutline: Int, val title: String, val group: Set<String>) {
    class Home(title: String) : NavigationItem(
        route = Screen.WalletRootScreen.route,
        iconOutline = R.drawable.ic_hicon_wallet,
        title = title,
        group = setOf("wallet_screen", "receive_screen", "send_screen", "transaction_screen/txid={txid}", "qr_scan_screen")
    )

    class Chapters(title: String) : NavigationItem(
        route = Screen.ChaptersRootScreen.route,
        iconOutline = R.drawable.ic_hicon_education,
        title = title,
        group = setOf("chapters_home_screen", "chapter_screen/{chapterId}")
    )

    class Settings(title: String) : NavigationItem(
        route = Screen.SettingsRootScreen.route,
        iconOutline = R.drawable.ic_hicon_menu,
        title = title,
        group = setOf("settings_root_screen", "about_screen", "recovery_phrase_screen", "send_coins_back_screen")
    )
}
