/*
 * Copyright 2020-2023 thunderbiscuit and contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE file.
 */

package com.goldenraven.padawanwallet.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.goldenraven.padawanwallet.R
import com.goldenraven.padawanwallet.theme.PadawanTheme
import com.goldenraven.padawanwallet.theme.connection_available
import com.goldenraven.padawanwallet.theme.connection_unavailable
import com.goldenraven.padawanwallet.theme.padawan_theme_onBackground_faded
import com.goldenraven.padawanwallet.theme.padawan_theme_onPrimary
import com.goldenraven.padawanwallet.utils.ScreenSizeWidth
import com.goldenraven.padawanwallet.utils.getScreenSizeWidth
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
    val screenSizeWidth = getScreenSizeWidth(LocalConfiguration.current.screenWidthDp)
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
        val (fontSize, horizontalPadding) = when (screenSizeWidth) {
            ScreenSizeWidth.Small -> Pair(16.sp, 8.dp)
            ScreenSizeWidth.Phone -> Pair(20.sp, 32.dp)
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
                // .background(Color.Cyan)
        ) {
            IconButton(
                onClick = { navController.popBackStack() },
                modifier = Modifier.align(alignment = Alignment.CenterStart).padding(horizontalPadding)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_back),
                    contentDescription = stringResource(id = R.string.back_icon),
                    tint = padawan_theme_onPrimary
                )
            }
            Text(
                text = title,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                fontSize = fontSize,
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
    val message = if (isConnected) stringResource(R.string.back_online) else stringResource(R.string.no_internet_connection)
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
            Icon(painterResource(id = iconResource), stringResource(R.string.connectivity_icon), tint = Color.White)
            Spacer(modifier = Modifier.size(8.dp))
            Text(message, color = Color.White, fontSize = 15.sp)
        }
    }
}

@Composable
fun LoadingAnimation(
    circleColor: Color = Color(0xfff6cf47),
    circleSize: Dp = 12.dp,
    animationDelay: Int = 800,
    initialAlpha: Float = 0.3f
) {
    val circles = listOf(
        remember { Animatable(initialValue = initialAlpha) },
        remember { Animatable(initialValue = initialAlpha) },
        remember { Animatable(initialValue = initialAlpha) }
    )

    circles.forEachIndexed { index, animatable ->
        LaunchedEffect(Unit) {
            // Use coroutine delay to sync animations
            delay(timeMillis = (animationDelay / circles.size).toLong() * index)

            animatable.animateTo(
                targetValue = 1f,
                animationSpec = infiniteRepeatable(
                    animation = tween(
                        durationMillis = animationDelay
                    ),
                    repeatMode = RepeatMode.Reverse
                )
            )
        }
    }

    // container for circles
    Row {
        circles.forEachIndexed { index, animatable ->
            // gap between the circles
            if (index != 0) Spacer(modifier = Modifier.width(width = 6.dp))

            Box(
                modifier = Modifier
                    .size(size = circleSize)
                    .clip(shape = CircleShape)
                    .background(circleColor.copy(alpha = animatable.value))
            )
        }
    }
}

@Preview(name = "PIXEL 4", device = Devices.PIXEL_4, showBackground = true)
@Preview(name = "PIXEL 2, 270 Wide", device = Devices.PIXEL_2, widthDp = 270, showBackground = true)
@Composable
internal fun PreviewPadawanAppBar() {
    PadawanTheme {
        PadawanAppBar(
            rememberNavController(),
            "Padawan Wallet"
        )
    }
}
