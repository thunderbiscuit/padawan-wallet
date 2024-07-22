/*
 * Copyright 2020-2024 thunderbiscuit and contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE file.
 */

package com.goldenraven.padawanwallet.presentation.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.goldenraven.padawanwallet.R
import com.goldenraven.padawanwallet.presentation.theme.PadawanTypography
import com.goldenraven.padawanwallet.presentation.theme.padawan_theme_background_secondary
import com.goldenraven.padawanwallet.presentation.theme.padawan_theme_button_primary
import com.goldenraven.padawanwallet.presentation.theme.padawan_theme_navigation_bar_unselected
import com.goldenraven.padawanwallet.presentation.ui.components.SystemBars
import com.goldenraven.padawanwallet.utils.NavigationItem
import com.goldenraven.padawanwallet.presentation.navigation.WalletNavigation
import androidx.navigation.compose.rememberNavController

private const val TAG = "HomeScreen"

@OptIn(androidx.compose.animation.ExperimentalAnimationApi::class)
@Composable
internal fun HomeScreen() {
    val navControllerWalletNavigation: NavHostController = rememberNavController()
    // the splash screen hides the system bars
    // we need to bring them back on before continuing
    SystemBars()

    Scaffold(
        bottomBar = { BottomNavigationBar(navControllerWalletNavigation) }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            WalletNavigation(
                navHostController = navControllerWalletNavigation,
            )
        }
    }
}

@Composable
internal fun BottomNavigationBar(
    navControllerWalletNavigation: NavController,
) {
    var selectedItem by remember { mutableIntStateOf(0) }
    val items = listOf(
        NavigationItem.Home(title = stringResource(id = R.string.bottom_nav_wallet)),
        NavigationItem.Chapters(title = stringResource(id = R.string.bottom_nav_chapters)),
        NavigationItem.Settings(title = stringResource(id = R.string.bottom_nav_settings))
    )

    // SystemBars()

    NavigationBar(
        tonalElevation = 0.dp,
        containerColor = padawan_theme_background_secondary,
        modifier = Modifier.drawBehind {
            drawLine(
                padawan_theme_navigation_bar_unselected,
                Offset(0f, 0f),
                Offset(size.width, 0f),
                2.dp.toPx()
            )
        },
    ) {
        val navBackStackEntry by navControllerWalletNavigation.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination

        items.forEachIndexed { index, item ->
            NavigationBarItem(
                icon = {
                    Icon(painter = painterResource(id = item.iconOutline), contentDescription = item.title)
                },
                label = {
                    Text(
                        text = item.title,
                        style = PadawanTypography.labelSmall
                    )
                },
                selected = currentDestination?.hierarchy?.any { it.route in item.group } == true,
                onClick = {
                    if (selectedItem != index) {
                        selectedItem = index
                        navControllerWalletNavigation.navigate(item.route) {
                            popUpTo(navControllerWalletNavigation.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = padawan_theme_button_primary,
                    selectedTextColor = padawan_theme_button_primary,
                    unselectedIconColor = padawan_theme_navigation_bar_unselected,
                    unselectedTextColor = padawan_theme_navigation_bar_unselected,
                    indicatorColor = padawan_theme_background_secondary,
                )
            )
        }
    }
}

// @Composable
// internal fun SystemBars() {
//     val systemUiController = rememberSystemUiController()
//     systemUiController.setSystemBarsColor(
//             color = Color.Transparent,
//             darkIcons = false
//     )
//     systemUiController.isStatusBarVisible = true
//     systemUiController.isNavigationBarVisible = false
//
//     // systemUiController.setNavigationBarColor(
//     //     color = Color.Transparent,
//     //     darkIcons = false
//     // )
// }
