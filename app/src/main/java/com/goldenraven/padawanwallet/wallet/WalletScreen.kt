package com.goldenraven.padawanwallet.wallet

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material.BottomNavigation
import com.goldenraven.padawanwallet.R
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import com.goldenraven.padawanwallet.theme.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun WalletScreen() {

    val scope = rememberCoroutineScope()
    @OptIn(ExperimentalMaterial3Api::class)
    val drawerState = rememberDrawerState(DrawerValue.Closed)

    Scaffold(
        topBar = { PadawanAppBar(scope, drawerState) },
        bottomBar = { BottomNavigationBar() },
        ) {
        val items = listOf(Icons.Default.Favorite, Icons.Default.Face, Icons.Default.Email)
        val selectedItem = remember { mutableStateOf(items[0]) }

        ModalNavigationDrawer(
            drawerState = drawerState,
            drawerContent = {
                items.forEach { item ->
                    NavigationDrawerItem(
                        icon = { Icon(item, contentDescription = null) },
                        label = { Text(item.name) },
                        selected = item == selectedItem.value,
                        onClick = {
                            scope.launch { drawerState.close() }
                            selectedItem.value = item
                        },
                        modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                    )
                }
            },
            content = {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                }
            }
        )
    }
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
        // val interactionSource = remember { MutableInteractionSource() }
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
            // modifier = Modifier.clickable(
            //     interactionSource = interactionSource,
            //     indication = null
            // )
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