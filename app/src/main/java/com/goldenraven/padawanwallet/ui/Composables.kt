package com.goldenraven.padawanwallet.ui

import androidx.compose.animation.*
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.goldenraven.padawanwallet.R
import com.goldenraven.padawanwallet.theme.*
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import kotlinx.coroutines.delay

val standardBorder = BorderStroke(2.dp, SolidColor(padawan_theme_onPrimary))

@Composable
internal fun FadedVerticalDivider() {
    Divider(
        color = padawan_theme_onBackground_faded,
        modifier = Modifier
            .fillMaxHeight()
            .width(3.dp)
            .padding(vertical = 8.dp)
    )
}

@Composable
internal fun VerticalTextFieldDivider() {
    Divider(
        color = padawan_theme_onPrimary,
        modifier = Modifier
            .fillMaxHeight()
            .width(3.dp)
            .padding(vertical = 14.dp)
    )
}

@Composable
internal fun PadawanAppBar(navController: NavHostController, title: String) {
    val appBarVisibility = remember { mutableStateOf(value = false) }


    LaunchedEffect(key1 = true) {
        delay(500)
        appBarVisibility.value = true
    }
    if (!appBarVisibility.value) { Spacer(Modifier.height(100.dp)) }

    AnimatedVisibility(
        visible = appBarVisibility.value,
        enter = fadeIn(tween(durationMillis = 500))
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .height(100.dp)
        ) {
            IconButton(
                onClick = { navController.popBackStack() },
                modifier = Modifier.align(alignment = Alignment.CenterStart)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_back),
                    contentDescription = "Back Icon",
                    tint = padawan_theme_onPrimary
                )
            }
            Text(
                text = title,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .align(alignment = Alignment.Center)
            )
        }
    }
}

@Composable
internal fun ShowBars() {
    rememberSystemUiController().apply {
        this.isSystemBarsVisible = true
        this.isNavigationBarVisible = true
        this.isStatusBarVisible = true
    }
}

@Composable
fun ConnectivityStatus(isConnected: Boolean) {
    var visibility by remember { mutableStateOf(false) }

    AnimatedVisibility(
        visible = visibility,
        enter = expandVertically(),
        exit = shrinkVertically()
    ) {
        ConnectivityStatusBox(isConnected = isConnected)
    }

    LaunchedEffect(isConnected) {
        visibility = if (!isConnected) {
            true
        } else {
            delay(2000)
            false
        }
    }
}

@Composable
fun ConnectivityStatusBox(isConnected: Boolean) {
    val backgroundColor by animateColorAsState(if (isConnected) connection_available else connection_unavailable)
    val message = if (isConnected) "Back Online!" else "No Internet Connection!"
    val iconResource = if (isConnected) {
        R.drawable.ic_connectivity_available
    } else {
        R.drawable.ic_connectivity_unavailable
    }

    Box(
        modifier = Modifier
            .background(backgroundColor)
            .fillMaxWidth()
            .padding(8.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            androidx.compose.material.Icon(painterResource(id = iconResource), "Connectivity Icon", tint = Color.White)
            Spacer(modifier = Modifier.size(8.dp))
            androidx.compose.material.Text(message, color = Color.White, fontSize = 15.sp)
        }
    }
}