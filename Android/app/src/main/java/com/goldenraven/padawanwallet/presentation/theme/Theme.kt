/*
 * Copyright 2020-2024 thunderbiscuit and contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE file.
 */

package com.goldenraven.padawanwallet.presentation.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val PadawanThemeColors = lightColorScheme(
    primary = padawan_theme_primary,
    onPrimary = padawan_theme_onPrimary,
)

@Composable
fun PadawanTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = PadawanThemeColors,
        typography = PadawanTypography,
        content = content
    )
}
