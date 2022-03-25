package com.goldenraven.padawanwallet.wallet

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.goldenraven.padawanwallet.ui.DrawerAppBar


@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun SettingsScreen(navController: NavController) {
    Scaffold(
        topBar = { DrawerAppBar(navController, title = "Settings") },
    ) { }
}
