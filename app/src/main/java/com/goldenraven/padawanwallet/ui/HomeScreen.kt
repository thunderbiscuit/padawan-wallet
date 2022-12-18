package com.goldenraven.padawanwallet.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.goldenraven.padawanwallet.theme.PadawanTypography
import com.goldenraven.padawanwallet.theme.padawan_theme_navigation_bar_unselected
import com.goldenraven.padawanwallet.theme.padawan_theme_onBackground
import com.goldenraven.padawanwallet.theme.white
import com.goldenraven.padawanwallet.ui.tutorials.TutorialViewModel
import com.goldenraven.padawanwallet.utils.NavigationItem
import com.goldenraven.padawanwallet.ui.wallet.WalletNavigation
import com.goldenraven.padawanwallet.ui.wallet.WalletViewModel
import com.google.accompanist.navigation.animation.rememberAnimatedNavController

@OptIn(ExperimentalMaterial3Api::class, androidx.compose.animation.ExperimentalAnimationApi::class)
@Composable
internal fun HomeScreen(walletViewModel: WalletViewModel, tutorialViewModel: TutorialViewModel) {
    val navControllerWalletNavigation: NavHostController = rememberAnimatedNavController()
    // the splash screen hides the system bars
    // we need to bring them back on before continuing
    ShowBars()

    Scaffold(bottomBar = { BottomNavigationBar(navControllerWalletNavigation) }) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            WalletNavigation(navControllerWalletNavigation = navControllerWalletNavigation, walletViewModel = walletViewModel, tutorialViewModel = tutorialViewModel)
        }
    }
}

@Composable
internal fun BottomNavigationBar(navControllerWalletNavigation: NavController) {
    var selectedItem by remember { mutableStateOf(0) }
    val items = listOf(
        NavigationItem.Home,
        NavigationItem.Tutorial,
        NavigationItem.Settings
    )

    NavigationBar(tonalElevation = 0.dp) {
        items.forEachIndexed { index, item ->
            NavigationBarItem(
                icon = {
                    if (selectedItem == index) {
                        Icon(painter = painterResource(id = item.icon_filled), contentDescription = item.title)
                    } else {
                        Icon(painter = painterResource(id = item.icon_outline), contentDescription = item.title)
                    }
                },
                label = {
                    Text(
                        text = item.title,
                        style = PadawanTypography.labelSmall
                    )
                },
                selected = selectedItem == index,
                onClick = {
                    selectedItem = index
                    navControllerWalletNavigation.navigate(item.route) {
                        navControllerWalletNavigation.graph.startDestinationRoute?.let { route ->
                            popUpTo(route)
                        }
                        launchSingleTop = true
                    }
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = padawan_theme_onBackground,
                    selectedTextColor = padawan_theme_onBackground,
                    unselectedIconColor = padawan_theme_navigation_bar_unselected,
                    unselectedTextColor = padawan_theme_navigation_bar_unselected,
                    indicatorColor = white,
                )
            )
        }
    }
}
