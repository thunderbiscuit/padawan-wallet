/*
 * Copyright 2020-2025 thunderbiscuit and contributors.
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
    background = Color(0xFF121212),                   // Deep matte black
    background2 = Color(0xFF1E1E1E),                  // Slightly elevated background
    accent1 = Color(0xFFEF3E36),                      // Imperial red (main accent)
    accent2 = Color(0xFFB71C1C),                      // Deeper, Vader-crimson red
    accent3 = Color(0xFF37474F),                      // Steel blue-grey (used like bronze in light mode)
    accent1Light = Color(0xFFFA8072),                 // Lighter red accent (for hover or highlights)
    text = Color(0xFFECECEC),                         // Primary light text
    textLight = Color(0xFFB0B0B0),                    // Secondary/faded text
    textFaded = Color(0x40808080),                    // Extra faded, 25% opacity gray
    goGreen = Color(0xFF81C784),                      // Soft green for success (readable on dark bg)
    errorRed = Color(0xFFE57373),                     // Softer red for error states
    navigationBarUnselected = Color(0xFF888888),      // Medium-light gray
    darkBackground = Color(0xFF0A0A0A),               // Even deeper for nav drawers, etc.
)
