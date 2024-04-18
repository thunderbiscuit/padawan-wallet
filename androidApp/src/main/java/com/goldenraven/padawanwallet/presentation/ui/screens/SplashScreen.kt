/*
 * Copyright 2020-2023 thunderbiscuit and contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE file.
 */

package com.goldenraven.padawanwallet.presentation.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.goldenraven.padawanwallet.R
import com.goldenraven.padawanwallet.utils.Screen
import kotlinx.coroutines.delay
import com.google.accompanist.systemuicontroller.rememberSystemUiController

@Composable
internal fun SplashScreen(navController: NavController, moveToIntro: Boolean) {

    HideBars()

    LaunchedEffect(key1 = true) {
        delay(1000)
        if (moveToIntro) {
            navController.navigate(Screen.OnboardingScreen.route) {
                popUpTo(0)
            }
        } else {
            navController.navigate(Screen.HomeScreen.route) {
                popUpTo(0)
            }
        }
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_padawan_colour_foreground),
            contentDescription = stringResource(id = R.string.padawan_logo),
            Modifier.size(200.dp)
        )
    }
}

@Composable
internal fun HideBars() {
    rememberSystemUiController().apply {
        this.isSystemBarsVisible = false
        this.isNavigationBarVisible = false
        this.isStatusBarVisible = false
    }
}
