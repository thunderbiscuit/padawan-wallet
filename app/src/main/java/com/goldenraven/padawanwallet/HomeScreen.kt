package com.goldenraven.padawanwallet.ui.wallet

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import com.goldenraven.padawanwallet.R
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.goldenraven.padawanwallet.BuildConfig
import com.goldenraven.padawanwallet.ui.Screen
import com.goldenraven.padawanwallet.theme.*
import com.goldenraven.padawanwallet.ui.ShowBars
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, androidx.compose.animation.ExperimentalAnimationApi::class)
@Composable
internal fun HomeScreen(navController: NavController) {

    // the splash screen hides the system bars
    // we need to bring them back on before continuing
    ShowBars()

    val scope = rememberCoroutineScope()

    val drawerState = rememberDrawerState(DrawerValue.Closed)

    val items = listOf(Icons.Default.Favorite, Icons.Default.Face, Icons.Default.Email, Icons.Default.Face)
    val selectedItem = remember { mutableStateOf(items[0]) }

    val navControllerWalletNavigation: NavHostController = rememberAnimatedNavController()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContainerColor = MaterialTheme.colorScheme.background,
        drawerContent = {
            Column(
                Modifier
                    .background(color = md_theme_dark_lightBackground)
                    .height(200.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_logo_v2_0_0),
                    tint = Color.Unspecified,
                    contentDescription = "Padawan logo",
                    modifier = Modifier.width(200.dp)
                )
                Spacer(Modifier.height(24.dp))
                Text(
                    BuildConfig.VERSION_NAME,
                    color = md_theme_dark_background,
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            NavigationDrawerItem(
                label = { Text("About") },
                selected = items[0] == selectedItem.value,
                onClick = { navController.navigate(Screen.AboutScreen.route) },
                modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding),
                colors = NavigationDrawerItemDefaults.colors(
                    selectedContainerColor = MaterialTheme.colorScheme.background,
                    unselectedContainerColor = MaterialTheme.colorScheme.background,
                )
            )
            NavigationDrawerItem(
                // icon = { painterResource(id = R.drawable.ic_baseline_tune_24) },
                label = { Text("Settings") },
                selected = items[1] == selectedItem.value,
                onClick = { navController.navigate(Screen.SettingsScreen.route) },
                modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding),
                colors = NavigationDrawerItemDefaults.colors(
                    selectedContainerColor = MaterialTheme.colorScheme.background,
                    unselectedContainerColor = MaterialTheme.colorScheme.background,
                    )
            )
            NavigationDrawerItem(
                label = { Text("Recovery Phrase") },
                selected = items[2] == selectedItem.value,
                onClick = { navController.navigate(Screen.RecoveryPhrase.route) },
                modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding),
                colors = NavigationDrawerItemDefaults.colors(
                    selectedContainerColor = MaterialTheme.colorScheme.background,
                    unselectedContainerColor = MaterialTheme.colorScheme.background,
                )
            )
            NavigationDrawerItem(
                label = { Text("Send Testnet Coins Back") },
                selected = items[3] == selectedItem.value,
                onClick = { navController.navigate(Screen.SendCoinsBackScreen.route) },
                modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding),
                colors = NavigationDrawerItemDefaults.colors(
                    selectedContainerColor = MaterialTheme.colorScheme.background,
                    unselectedContainerColor = MaterialTheme.colorScheme.background,
                )
            )
        },
        content = {
            Scaffold(
                topBar = { PadawanAppBar(scope, drawerState) },
                bottomBar = { BottomNavigationBar(navControllerWalletNavigation) },
            ) { innerPadding ->
                Box(
                    modifier = Modifier.padding(innerPadding)
                ) {
                    WalletNavigation(navControllerWalletNavigation)
                }
            }
        }
    )
}

@Composable
internal fun BottomNavigationBar(navControllerWalletNavigation: NavController) {
    var selectedItem by remember { mutableStateOf(0) }
    val items = listOf("Wallet", "Tutorials")

    NavigationBar(
        containerColor = md_theme_dark_surfaceLight,
        tonalElevation = 0.dp
    ) {
        NavigationBarItem(
            icon = { Icon(painterResource(id = R.drawable.ic_bitcoin_logo), contentDescription = null) },
            label = {
                Text(
                    text = items[0],
                )
            },
            selected = selectedItem == 0,
            onClick = {
                selectedItem = 0
                navControllerWalletNavigation.navigate(Screen.WalletScreen.route)
            },
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
            label = {
                Text(
                    text = items[1],
                )
            },
            selected = selectedItem == 1,
            onClick = {
                selectedItem = 1
                navControllerWalletNavigation.navigate(Screen.TutorialsHomeScreen.route)
            },
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
        colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = md_theme_dark_surfaceLight),
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
        text = stringResource(R.string.app_name),
        color = MaterialTheme.colorScheme.primary
    )
}
