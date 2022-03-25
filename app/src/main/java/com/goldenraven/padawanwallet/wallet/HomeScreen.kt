package com.goldenraven.padawanwallet.wallet

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import com.goldenraven.padawanwallet.R
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.goldenraven.padawanwallet.ui.Screen
import com.goldenraven.padawanwallet.theme.*
import com.goldenraven.padawanwallet.ui.ShowBars
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun HomeScreen(navController: NavController) {

    // the splash screen hides the system bars
    // we need to bring them back on before continuing
    ShowBars()

    val scope = rememberCoroutineScope()

    @OptIn(ExperimentalMaterial3Api::class)
    val drawerState = rememberDrawerState(DrawerValue.Closed)

    val items = listOf(Icons.Default.Favorite, Icons.Default.Face, Icons.Default.Email, Icons.Default.Favorite)
    val selectedItem = remember { mutableStateOf(items[0]) }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContainerColor = MaterialTheme.colorScheme.background,
        drawerContent = {
            NavigationDrawerItem(
                label = { Text("About") },
                selected = items[0] == selectedItem.value,
                onClick = { navController.navigate(Screen.AboutScreen.route) },
                modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
            )
            NavigationDrawerItem(
                label = { Text("Settings") },
                selected = items[1] == selectedItem.value,
                onClick = { navController.navigate(Screen.SettingsScreen.route) },
                modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
            )
            NavigationDrawerItem(
                label = { Text("Recovery Phrase") },
                selected = items[2] == selectedItem.value,
                onClick = { navController.navigate(Screen.RecoveryPhrase.route) },
                modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
            )
            NavigationDrawerItem(
                label = { Text("Send Coins Back") },
                selected = items[3] == selectedItem.value,
                onClick = { navController.navigate(Screen.SendCoinsBackScreen.route) },
                modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
            )
        },
        content = {
            Scaffold(
                topBar = { PadawanAppBar(scope, drawerState) },
                bottomBar = { BottomNavigationBar() },
            ) {

            }
        }
    )
}


@Composable
internal fun BottomNavigationBar() {
    var selectedItem by remember { mutableStateOf(0) }
    val items = listOf("Wallet", "Tutorials")

    NavigationBar(
        containerColor = md_theme_dark_surfaceLight,
        tonalElevation = 0.dp
    ) {
        NavigationBarItem(
            icon = { Icon(painterResource(id = R.drawable.ic_bitcoin_logo), contentDescription = null) },
            label = { Text(items[0]) },
            selected = selectedItem == 0,
            onClick = { selectedItem = 0 },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = MaterialTheme.colorScheme.primary,
                selectedTextColor = MaterialTheme.colorScheme.primary,
                unselectedIconColor = md_theme_dark_onBackgroundFaded,
                unselectedTextColor = md_theme_dark_onBackgroundFaded,
                indicatorColor = md_theme_dark_surfaceLight
            )
        )
        NavigationBarItem(
            icon = { Icon(painterResource(id = R.drawable.school), contentDescription = null) },
            label = { Text(items[1]) },
            selected = selectedItem == 1,
            onClick = { selectedItem = 1 },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = MaterialTheme.colorScheme.primary,
                selectedTextColor = MaterialTheme.colorScheme.primary,
                unselectedIconColor = md_theme_dark_onBackgroundFaded,
                unselectedTextColor = md_theme_dark_onBackgroundFaded,
                indicatorColor = md_theme_dark_surfaceLight
            ),
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun PadawanAppBar(scope: CoroutineScope, drawerState: DrawerState) {
    SmallTopAppBar(
        title = { AppTitle() },
        navigationIcon = {
            IconButton(onClick = { scope.launch { drawerState.open() } }) {
                Icon(
                    imageVector = Icons.Filled.Menu,
                    contentDescription = "Open drawer",
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }
        },
        actions = { }
    )
}

@Composable
internal fun AppTitle() {
    Text(
        text = "Padawan Wallet",
        style = MaterialTheme.typography.headlineMedium,
        color = MaterialTheme.colorScheme.primary
    )
}
