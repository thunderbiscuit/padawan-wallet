package com.goldenraven.padawanwallet.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.google.accompanist.systemuicontroller.rememberSystemUiController

@Composable
internal fun ShowBars() {
    rememberSystemUiController().apply {
        this.isSystemBarsVisible = true
        this.isNavigationBarVisible = true
        this.isStatusBarVisible = true
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun DrawerAppBar(navController: NavController, title: String) {
    SmallTopAppBar(
        title = { DrawerScreenAppBarTitle(title) },
        navigationIcon = {
            IconButton(onClick = { navController.navigate(Screen.HomeScreen.route) }) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = "Open drawer",
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }
        },
        actions = { }
    )
}

@Composable
internal fun DrawerScreenAppBarTitle(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.headlineMedium,
        color = MaterialTheme.colorScheme.primary
    )
}
