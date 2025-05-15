/*
 * Copyright 2020-2024 thunderbiscuit and contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE file.
 */

package com.goldenraven.padawanwallet.presentation.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import com.goldenraven.padawanwallet.domain.PadawanColorTheme

val LocalPadawanColors = staticCompositionLocalOf<PadawanColors> { PadawanColorsTatooineDesert }

private val PadawanThemeColors = lightColorScheme(
    primary = PadawanMaterialColors.primary,
    onPrimary = PadawanMaterialColors.onPrimary,
    surface = PadawanMaterialColors.surface
)

@Composable
fun PadawanTheme(
    theme: PadawanColorTheme = PadawanColorTheme.TATOOINE_DESERT,
    content: @Composable () -> Unit
) {
    val PadawanColors = when (theme) {
        PadawanColorTheme.TATOOINE_DESERT -> PadawanColorsTatooineDesert
        PadawanColorTheme.VADER_DARK -> PadawanColorsVaderDark
    }

    MaterialTheme(
        colorScheme = PadawanThemeColors,
        typography = PadawanTypography,
    ) {
        CompositionLocalProvider(LocalPadawanColors provides PadawanColors) {
            content()
        }
    }
}
