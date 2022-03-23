package com.goldenraven.padawanwallet.wallet

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.material3.rememberDrawerState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import com.goldenraven.padawanwallet.theme.jost
import com.goldenraven.padawanwallet.theme.shareTechMono
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
        ) {
        // icons to mimic drawer destinations
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
                    Text(text = if (drawerState.isClosed) ">>> Swipe >>>" else "<<< Swipe <<<")
                    Spacer(Modifier.height(20.dp))
                    Button(onClick = { scope.launch { drawerState.open() } }) {
                        Text("Click to open")
                    }
                }
            }
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
                    contentDescription = "Localized description",
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