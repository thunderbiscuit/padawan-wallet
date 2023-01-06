package com.goldenraven.padawanwallet.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.goldenraven.padawanwallet.theme.*
import com.goldenraven.padawanwallet.ui.chapters.ChaptersViewModel
import com.goldenraven.padawanwallet.utils.NavigationItem
import com.goldenraven.padawanwallet.ui.wallet.WalletNavigation
import com.goldenraven.padawanwallet.ui.wallet.WalletViewModel
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.google.accompanist.systemuicontroller.rememberSystemUiController

@OptIn(ExperimentalMaterial3Api::class, androidx.compose.animation.ExperimentalAnimationApi::class)
@Composable
internal fun HomeScreen(walletViewModel: WalletViewModel, chaptersViewModel: ChaptersViewModel) {
    val navControllerWalletNavigation: NavHostController = rememberAnimatedNavController()
    // the splash screen hides the system bars
    // we need to bring them back on before continuing
    ShowBars()

    Scaffold(bottomBar = { BottomNavigationBar(navControllerWalletNavigation) }) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            WalletNavigation(navControllerWalletNavigation = navControllerWalletNavigation, walletViewModel = walletViewModel, chaptersViewModel = chaptersViewModel)
        }
    }
}

@Composable
internal fun BottomNavigationBar(
    navControllerWalletNavigation: NavController,
) {
    var selectedItem by remember { mutableStateOf(0) }
    val items = listOf(
        NavigationItem.Home,
        NavigationItem.Chapters,
        NavigationItem.Settings
    )

    SystemBars()

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
                    // selectedIconColor = padawan_theme_onBackground,
                    // selectedTextColor = padawan_theme_onBackground,
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

@Composable
internal fun SystemBars() {
    rememberSystemUiController().apply {
        // setStatusBarColor(
        //     color = Color.Transparent,
        //     darkIcons = false
        // )
        // this.isStatusBarVisible = false

        setNavigationBarColor(
            color = Color.Transparent,
            darkIcons = false
        )
        this.isNavigationBarVisible = false
    }
}
