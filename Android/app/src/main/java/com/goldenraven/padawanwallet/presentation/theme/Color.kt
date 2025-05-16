/*
 * Copyright 2020-2024 thunderbiscuit and contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE file.
 */

package com.goldenraven.padawanwallet.presentation.theme

import androidx.compose.ui.graphics.Color

val testPink = Color(0xffff1493)

object PadawanMaterialColors {
    val onPrimary = Color(0xFF1a1a1a) // This is used by default for text on buttons
    // val primary = Color(0xFF76dab3)
    val surface = testPink
}

data class PadawanColors(
    val background: Color,
    val background2: Color,
    val accent1: Color,
    val accent2 : Color,
    val accent3: Color,
    val accent1Light : Color,
    val text: Color,
    val textLight: Color,
    val textFaded: Color,
    val goGreen: Color,
    val errorRed: Color,
    val navigationBarUnselected: Color,
    val darkBackground: Color,
)

val PadawanColorsTatooineDesert = PadawanColors(
    background = Color(0xFFFDEBD3),
    background2 = Color(0xFFEAD1B4),
    accent1 = Color(0xFFEF6C57),
    accent2 = Color(0xffFFAA00),
    accent3 = Color(0xFFB34700),
    accent1Light = Color(0xffF9AA77),
    text = Color(0xFF1a1a1a),
    textLight = Color(0xFF5a5a5a),
    textFaded = Color(0x403A3A3C),
    goGreen = Color(0xFF7BAF7B),
    errorRed = Color(0xFFC8644D),
    navigationBarUnselected = Color(0xFF3A3A3C),
    darkBackground = Color(0xFF2E1B0A),
)

val PadawanColorsVaderDark = PadawanColors(
    background = Color(0xFF000000),
    background2 = Color(0xFFEAD1B4),
    accent1 = Color(0xFFEF6C57),
    accent2 = Color(0xffFFAA00),
    accent3 = Color(0xFFB34700),
    accent1Light = Color(0xffF9AA77),
    text = Color(0xFF3A3A3C),
    textLight = Color(0xFF5a5a5a),
    textFaded = Color(0x403A3A3C),
    goGreen = Color(0xFF7BAF7B),
    errorRed = Color(0xFFC8644D),
    navigationBarUnselected = Color(0xFF3A3A3C),
    darkBackground = Color(0xFF2E1B0A),
)
