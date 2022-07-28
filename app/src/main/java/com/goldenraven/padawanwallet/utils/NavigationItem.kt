package com.goldenraven.padawanwallet.utils

import com.goldenraven.padawanwallet.R
import com.goldenraven.padawanwallet.ui.Screen

sealed class NavigationItem(val route: String, val icon_filled: Int, val icon_outline: Int, val title: String) {
    object Home : NavigationItem(route = Screen.WalletScreen.route, icon_filled = R.drawable.ic_home, icon_outline = R.drawable.ic_home_outline, title = "Home")
    object Tutorial : NavigationItem(route = Screen.TutorialsHomeScreen.route, icon_filled = R.drawable.ic_school, icon_outline = R.drawable.ic_school_outline, title = "Learn")
    object Settings : NavigationItem(route = Screen.SettingsScreen.route, icon_filled = R.drawable.ic_settings, icon_outline = R.drawable.ic_settings_outline, title = "Settings")
}