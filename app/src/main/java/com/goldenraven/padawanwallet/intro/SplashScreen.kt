/*
 * Copyright 2020-2022 thunderbiscuit and contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE file.
 */

package com.goldenraven.padawanwallet.intro

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import com.goldenraven.padawanwallet.R
import com.goldenraven.padawanwallet.theme.md_theme_dark_lightBackground
import kotlinx.coroutines.delay
import com.google.accompanist.systemuicontroller.rememberSystemUiController

@Composable
internal fun SplashScreen(navController: NavController) {

    // val systemUiController = rememberSystemUiController().apply {
    //     this.isSystemBarsVisible = false
    //     this.isNavigationBarVisible = false
    //     this.isSystemBarsVisible = false
    // }

    HideBars()

    LaunchedEffect(key1 = true) {
        delay(1000)
        navController.navigate(Screen.IntroScreen.route) {
            popUpTo(0)
        }
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
            .background(md_theme_dark_lightBackground)
    ) {
        Image(painter = painterResource(id = R.drawable.ic_logo_v1_0_0), contentDescription = "Padawan Logo")
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