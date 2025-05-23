/*
 * Copyright 2020-2025 thunderbiscuit and contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE file.
 */

package com.goldenraven.padawanwallet.presentation.ui.screens

import androidx.compose.foundation.clickable
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.goldenraven.padawanwallet.R
import com.goldenraven.padawanwallet.presentation.theme.PadawanTypography
import com.goldenraven.padawanwallet.presentation.theme.PadawanColorsTatooineDesert
import com.goldenraven.padawanwallet.utils.NavigationItem
import com.goldenraven.padawanwallet.presentation.navigation.WalletNavigation
import androidx.navigation.compose.rememberNavController

private const val TAG = "RootScreen"

@OptIn(androidx.compose.animation.ExperimentalAnimationApi::class)
@Composable
internal fun MainScreen() {
    val navControllerWalletNavigation: NavHostController = rememberNavController()

    Scaffold(
        bottomBar = { BottomNavigationBar(navControllerWalletNavigation) },
    ) { scaffoldPadding ->
        // We pass the insets for the system bars down to the composables that need them.
        WalletNavigation(
            paddingValues = scaffoldPadding,
            navHostController = navControllerWalletNavigation,
        )
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

    val navBackStackEntry by navControllerWalletNavigation.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route?.substringAfterLast(".") ?: ""
    val screensWithBottomBar = listOf("WalletRootScreen", "ChaptersRootScreen", "SettingsRootScreen")
    val showNavigationBar = currentRoute in screensWithBottomBar

    if (showNavigationBar) {
        NavigationBar(
            tonalElevation = 0.dp,
            containerColor = PadawanColorsTatooineDesert.background2,
        ) {
            // This odd code is necessary to ensure the ripple effect is not shown on any part of the
            // navigation bar item. See commit a1fceda for more information.
            items.forEachIndexed { index, item ->
                NavigationBarItem(
                    icon = {
                        Icon(
                            imageVector = item.icon,
                            contentDescription = item.title,
                            modifier = Modifier.clickable(
                                interactionSource = null,
                                indication = null,
                                enabled = true,
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
                                }
                            )
                        )
                    },
                    label = {
                        Text(
                            text = item.title,
                            style = PadawanTypography.labelMedium,
                            modifier = Modifier.clickable(
                                interactionSource = null,
                                indication = null,
                                enabled = true,
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
                                }
                            )
                        )
                    },
                    selected = selectedItem == index,
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
                        selectedIconColor = PadawanColorsTatooineDesert.accent1,
                        selectedTextColor = PadawanColorsTatooineDesert.accent1,
                        unselectedIconColor = PadawanColorsTatooineDesert.navigationBarUnselected,
                        unselectedTextColor = PadawanColorsTatooineDesert.navigationBarUnselected,
                        indicatorColor = Color.Transparent,
                    ),
                )
            }
        }
    }
}
