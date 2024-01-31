/*
 * Copyright 2020-2023 thunderbiscuit and contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE file.
 */

package com.goldenraven.padawanwallet.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.goldenraven.padawanwallet.R
import com.goldenraven.padawanwallet.ui.theme.padawan_theme_background_secondary
import com.goldenraven.padawanwallet.ui.theme.padawan_theme_onPrimary
import com.goldenraven.padawanwallet.utils.ScreenSizeWidth
import com.goldenraven.padawanwallet.utils.getScreenSizeWidth
import kotlinx.coroutines.delay

@Composable
internal fun PadawanAppBar(navController: NavHostController, title: String) {
    val screenSizeWidth = getScreenSizeWidth(LocalConfiguration.current.screenWidthDp)
    var appBarVisibility by remember { mutableStateOf(value = false) }


    LaunchedEffect(key1 = true) {
        delay(500)
        appBarVisibility = true
    }
    if (!appBarVisibility) { Spacer(Modifier.height(100.dp)) }

    AnimatedVisibility(
        visible = appBarVisibility,
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
                modifier = Modifier
                    .align(alignment = Alignment.CenterStart)
                    .padding(horizontalPadding)
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SecondaryScreenAppBar(title: String, onClick: () -> Unit) {
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = title,
                fontWeight = FontWeight.Medium
            )
        },
        navigationIcon = {
            IconButton(onClick = onClick) {
                Icon(
                    imageVector = Icons.Rounded.ArrowBack,
                    contentDescription = stringResource(id = R.string.back_icon),
                    tint = padawan_theme_onPrimary
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = padawan_theme_background_secondary
        )
    )
}
