package com.goldenraven.padawanwallet.ui.components

import androidx.compose.runtime.Composable
import com.google.accompanist.systemuicontroller.rememberSystemUiController

@Composable
internal fun ShowBars() {
    rememberSystemUiController().apply {
        this.isSystemBarsVisible = true
        this.isNavigationBarVisible = true
        this.isStatusBarVisible = true
    }
}
