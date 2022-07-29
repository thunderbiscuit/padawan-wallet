package com.goldenraven.padawanwallet.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.goldenraven.padawanwallet.R
import com.goldenraven.padawanwallet.theme.*
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import kotlinx.coroutines.delay

fun Modifier.shadowModifier(shadowHeight: Float): Modifier = this.then(
    Modifier
        .drawBehind {
            drawRoundRect(
                color = padawan_theme_onPrimary,
                size = Size(
                    width = size.width,
                    height = size.height / 2 + shadowHeight,
                ),
                topLeft = Offset(x = 0f, y = size.height / 2),
                cornerRadius = CornerRadius(
                    x = 20.dp.toPx(),
                    y = 20.dp.toPx()
                )
            )
            drawRect(
                color = padawan_theme_onPrimary,
                size = Size(
                    width = size.width,
                    height = size.height / 4 + shadowHeight / 2,
                ),
                topLeft = Offset(x = 0f, y = size.height / 2)
            )
        }
)

inline fun Modifier.noRippleClickable(crossinline onClick: () -> Unit): Modifier = composed {
    clickable(
        indication = null,
        interactionSource = remember { MutableInteractionSource() }
    ) {
        onClick()
    }
}

class BackgroundShape : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        return Outline.Generic(
            path = Path().apply {
                reset()
                moveTo(x = 0f, y = (size.height / 12) * 3)
                quadraticBezierTo(x1 = size.width / 2, y1 = size.height / 12, x2 = size.width, y2 = size.height / 4)
                addRect(rect = Rect(
                    top = size.height / 4,
                    bottom = size.height,
                    left = 0f,
                    right = size.width,
                ))
                close()
            }
        )
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
internal fun DrawerAppBar(navController: NavController, title: String) {
    SmallTopAppBar(
        title = { DrawerScreenAppBarTitle(title) },
        colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = md_theme_dark_surfaceLight),
        // modifier = Modifier.background(color = md_theme_dark_background2),
        navigationIcon = {
            IconButton(onClick = { navController.navigate(Screen.HomeScreen.route) }) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = "Open drawer",
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }
        },
        actions = { }
    )
}

@Composable
internal fun DrawerScreenAppBarTitle(title: String) {
    Text(
        text = title,
        style = PadawanTypography.titleLarge,
        color = MaterialTheme.colorScheme.primary
    )
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