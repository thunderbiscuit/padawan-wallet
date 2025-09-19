/*
 * Copyright 2020-2025 thunderbiscuit and contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE file.
 */

package com.coyotebitcoin.padawanwallet.presentation.ui.screens

import androidx.compose.animation.ExperimentalAnimationApi
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
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import com.composables.icons.lucide.GraduationCap
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.Settings2
import com.composables.icons.lucide.WalletMinimal
import com.coyotebitcoin.padawanwallet.R
import com.coyotebitcoin.padawanwallet.presentation.navigation.CoreDestinations
import com.coyotebitcoin.padawanwallet.presentation.navigation.NavigationCoreScreens
import com.coyotebitcoin.padawanwallet.presentation.theme.PadawanColorsTatooineDesert
import com.coyotebitcoin.padawanwallet.presentation.theme.PadawanTypography
import com.coyotebitcoin.padawanwallet.presentation.viewmodels.LessonsViewModel
import com.coyotebitcoin.padawanwallet.presentation.viewmodels.WalletViewModel

private const val TAG = "PadawanTag/RootScreen"

/**
 * This is the entry point for the "core" screens of the app, namely the wallet, chapters, and more screens.
 * These 3 screens exist in a bottom navigation bar structure.
 * When navigating between these 3 screens, there is no back stack maintained, i.e. the backstack is always 1 screen
 * deep (the current screen). Using the onBack callback will do (hopefully) nothing.
 */
@OptIn(ExperimentalAnimationApi::class)
@Composable
internal fun CoreScreens(
    backStack: NavBackStack<NavKey>,
    bottomBarBackStack: NavBackStack<NavKey>,
    walletViewModel: WalletViewModel,
    chaptersViewModel: LessonsViewModel,
) {
    Scaffold(
        bottomBar = { BottomNavigationBar(
            onWalletNavigation = {
                bottomBarBackStack.clear()
                bottomBarBackStack.add(CoreDestinations.WalletRootScreen)
            },
            onLessonsNavigation = {
                bottomBarBackStack.clear()
                bottomBarBackStack.add(CoreDestinations.LessonsRootScreen)
            },
            onMoreNavigation = {
                bottomBarBackStack.clear()
                bottomBarBackStack.add(CoreDestinations.SettingsRootScreen)
            },
        ) },
    ) { scaffoldPadding ->
        NavigationCoreScreens(
            backStack = backStack,
            bottomBarBackStack = bottomBarBackStack,
            walletViewModel = walletViewModel,
            chaptersViewModel = chaptersViewModel,
            scaffoldPadding = scaffoldPadding,
        )
    }
}

@Composable
internal fun BottomNavigationBar(
    onWalletNavigation: () -> Unit,
    onLessonsNavigation: () -> Unit,
    onMoreNavigation: () -> Unit,
) {
    var selectedItem by remember { mutableIntStateOf(0) }

    // This odd code where you define the onClick in 3 separate places is necessary to ensure the ripple effect is not
    // shown on any part of the navigation bar item. See commit a1fceda for more information.
    NavigationBar(
        tonalElevation = 0.dp,
        containerColor = PadawanColorsTatooineDesert.background2,
    ) {
        NavigationBarItem(
            icon = {
                Icon(
                    imageVector = Lucide.WalletMinimal,
                    contentDescription = stringResource(id = R.string.bottom_nav_wallet),
                    modifier = Modifier.clickable(
                        interactionSource = null,
                        indication = null,
                        enabled = true,
                        onClick = {
                            selectedItem = 0
                            onWalletNavigation()
                        }
                    )
                )
            },
            label = {
                Text(
                    text = stringResource(id = R.string.bottom_nav_wallet),
                    style = PadawanTypography.labelMedium,
                    modifier = Modifier.clickable(
                        interactionSource = null,
                        indication = null,
                        enabled = true,
                        onClick = {
                            selectedItem = 0
                            onWalletNavigation()
                        }
                    )
                )
            },
            selected = selectedItem == 0,
            onClick = {
                selectedItem = 0
                onWalletNavigation()
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = PadawanColorsTatooineDesert.accent1,
                selectedTextColor = PadawanColorsTatooineDesert.accent1,
                unselectedIconColor = PadawanColorsTatooineDesert.navigationBarUnselected,
                unselectedTextColor = PadawanColorsTatooineDesert.navigationBarUnselected,
                indicatorColor = Color.Transparent,
            ),
        )

        NavigationBarItem(
            icon = {
                Icon(
                    imageVector = Lucide.GraduationCap,
                    contentDescription = stringResource(id = R.string.bottom_nav_chapters),
                    modifier = Modifier.clickable(
                        interactionSource = null,
                        indication = null,
                        enabled = true,
                        onClick = {
                            selectedItem = 1
                            onLessonsNavigation()
                        }
                    )
                )
            },
            label = {
                Text(
                    text = stringResource(id = R.string.bottom_nav_chapters),
                    style = PadawanTypography.labelMedium,
                    modifier = Modifier.clickable(
                        interactionSource = null,
                        indication = null,
                        enabled = true,
                        onClick = {
                            selectedItem = 1
                            onLessonsNavigation()
                        }
                    )
                )
            },
            selected = selectedItem == 1,
            onClick = {
                selectedItem = 1
                onLessonsNavigation()
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = PadawanColorsTatooineDesert.accent1,
                selectedTextColor = PadawanColorsTatooineDesert.accent1,
                unselectedIconColor = PadawanColorsTatooineDesert.navigationBarUnselected,
                unselectedTextColor = PadawanColorsTatooineDesert.navigationBarUnselected,
                indicatorColor = Color.Transparent,
            ),
        )

        NavigationBarItem(
            icon = {
                Icon(
                    imageVector = Lucide.Settings2,
                    contentDescription = stringResource(id = R.string.bottom_nav_settings),
                    modifier = Modifier.clickable(
                        interactionSource = null,
                        indication = null,
                        enabled = true,
                        onClick = {
                            selectedItem = 2
                            onMoreNavigation()
                        }
                    )
                )
            },
            label = {
                Text(
                    text = stringResource(id = R.string.bottom_nav_settings),
                    style = PadawanTypography.labelMedium,
                    modifier = Modifier.clickable(
                        interactionSource = null,
                        indication = null,
                        enabled = true,
                        onClick = {
                            selectedItem = 2
                            onMoreNavigation()
                        }
                    )
                )
            },
            selected = selectedItem == 2,
            onClick = {
                selectedItem = 2
                onMoreNavigation()
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
