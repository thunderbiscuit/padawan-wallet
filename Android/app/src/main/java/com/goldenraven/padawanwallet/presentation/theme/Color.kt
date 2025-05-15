/*
 * Copyright 2020-2024 thunderbiscuit and contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE file.
 */

package com.goldenraven.padawanwallet.presentation.theme

import androidx.compose.ui.graphics.Color

object PadawanMaterialColors {
    val primary = Color(0xFF76dab3)
    val onPrimary = Color(0xFF000000)
    val surface = Color(0xFFF5EAD1)
}

data class PadawanColors(
    val background: Color,
    val accent1: Color,
    val accent2 : Color,
    val tealAccent : Color,
    val text: Color,
    val navigationBarUnselected: Color,
    val textFaded: Color,
    val accent3: Color,
    val navigationBar: Color,
    val darkBackground: Color,
    // val navigationBar = Color(0xFFEAD1B4)

    val backgroundSecondary: Color,
    val primary: Color,
    val onPrimary: Color,
    val tutorialBackground: Color,
    val textFadedSecondary: Color,
    val receivePrimary: Color,
    val sendPrimary: Color,
    val textHeadline: Color,
    val buttonSecondaryFaded: Color,
    val padawanFaded: Color,
    val error: Color,
    val md_theme_dark_onBackgroundFaded: Color,
    val md_theme_dark_onLightBackground: Color,
    val md_theme_dark_warning: Color,

    val testPink: Color
)

// object PadawanColors {
//     val primary = Color(0xFF76dab3)
//     val onPrimary = Color(0xFF000000)
//     val background = Color(0xFF76dab3)
//     val backgroundSecondary = Color(0xFFf3f4ff)
//     val onBackgroundSecondary = Color(0xFFdadeff)
//     val onBackgroundFaded = Color(0x668480e5)
//     val navigationBarUnselected = Color(0xFF8a8a8a)
//     val buttonPrimary = Color(0xffff6d3b)
//     val buttonSecondary = Color(0xffffd1bf)
//     val tutorialBackground = Color(0xfff6cf47)
//     val textFaded = Color(0x60000000)
//     val textFadedSecondary = Color(0xff787878)
//     val receivePrimary = Color(0x668480e5)
//     val sendPrimary = Color(0x6676dab3)
//     val textHeadline = Color(0xff1f0208) // default dark text (not quite black)
//     val buttonSecondaryFaded = Color(0xc0ffd1bf)
//     val padawanFaded = Color(0xA6000000)
//     val lazyColumnBackground = Color(0xFFfee7e0)
//     val error = Color(0xffcc241d)
//     val md_theme_dark_onBackgroundFaded = Color(0x80ebdbb2)
//     val md_theme_dark_onLightBackground = Color(0xff282828)
//     val md_theme_dark_warning = Color(0xfffabd2f)
//
//     val testPink = Color(0xffff1493)
// }

// New theme teal
// object PadawanColors {
//     val background = Color(0xFFF5EAD1)                // Main background color
//     val onBackgroundSecondary = Color(0xFF4CB7C1)     // Balance card teal
//     val buttonPrimary = Color(0xffFFCC00)             // Primary button yellow
//     val tealAccent = Color(0xff7CCED6)                // Lighter teal for unit accent
//     val text = Color(0xFF2B2B2B)                      // Text
//     val navigationBarUnselected = Color(0xFF2B2B2B)   // Navigation bar unselected, same as text
//     val textFaded = Color(0x402B2B2B)                 // Faded text
//     val accent1 = Color(0xFFFF6B00)                   // Navigation bar selected
//     val navigationBar = Color(0xFFEDE0C4)
//
//     val backgroundSecondary = Color(0xFFf3f4ff)       // New!
//     val primary = Color(0xFF76dab3)fire
//     val onPrimary = Color(0xFF000000)
//     val tutorialBackground = Color(0xfff6cf47)
//     val textFadedSecondary = Color(0xff787878)
//     val receivePrimary = Color(0x668480e5)
//     val sendPrimary = Color(0x6676dab3)
//     val textHeadline = Color(0xff1f0208) // default dark text (not quite black)
//     val buttonSecondaryFaded = Color(0xc0ffd1bf)
//     val padawanFaded = Color(0xA6000000)
//     val error = Color(0xffcc241d)
//     val md_theme_dark_onBackgroundFaded = Color(0x80ebdbb2)
//     val md_theme_dark_onLightBackground = Color(0xff282828)
//     val md_theme_dark_warning = Color(0xfffabd2f)
//
//     val testPink = Color(0xffff1493)
// }

// New theme sand
val PadawanColorsTatooineDesert = PadawanColors(
    background = Color(0xFFFDEBD3),                // Soft peach background
    accent1 = Color(0xFFEF6C57),     // Coral card
    accent2 = Color(0xffFFAA00),             // Sunset gold button
    tealAccent = Color(0xffF9AA77),                // Warm tangerine unit toggle
    text = Color(0xFF3A3A3C),                      // Slightly cool charcoal
    navigationBarUnselected = Color(0xFF3A3A3C),   // Match text
    textFaded = Color(0x403A3A3C),                 // Faded text
    accent3 = Color(0xFFB34700),                   // Burnt orange selection
    navigationBar = Color(0xFFEAD1B4),
    darkBackground = Color(0xFF2E1B0A),

    backgroundSecondary = Color(0xFFf3f4ff),       // New!
    primary = Color(0xFF76dab3),
    onPrimary = Color(0xFF000000),
    tutorialBackground = Color(0xfff6cf47),
    textFadedSecondary = Color(0xff787878),
    receivePrimary = Color(0x668480e5),
    sendPrimary = Color(0x6676dab3),
    textHeadline = Color(0xff1f0208), // default dark text (not quite black)
    buttonSecondaryFaded = Color(0xc0ffd1bf),
    padawanFaded = Color(0xA6000000),
    error = Color(0xffcc241d),
    md_theme_dark_onBackgroundFaded = Color(0x80ebdbb2),
    md_theme_dark_onLightBackground = Color(0xff282828),
    md_theme_dark_warning = Color(0xfffffabd),

    testPink = Color(0xffff1493),
)


val PadawanColorsVaderDark = PadawanColors(
    background = Color(0xFF000000),                // Soft peach background
    accent1 = Color(0xFFEF6C57),     // Coral card
    accent2 = Color(0xffFFAA00),             // Sunset gold button
    tealAccent = Color(0xffF9AA77),                // Warm tangerine unit toggle
    text = Color(0xFF3A3A3C),                      // Slightly cool charcoal
    navigationBarUnselected = Color(0xFF3A3A3C),   // Match text
    textFaded = Color(0x403A3A3C),                 // Faded text
    accent3 = Color(0xFFB34700),                   // Burnt orange selection
    navigationBar = Color(0xFFEAD1B4),
    darkBackground = Color(0xFF2E1B0A),

    backgroundSecondary = Color(0xFFf3f4ff),       // New!
    primary = Color(0xFF76dab3),
    onPrimary = Color(0xFF000000),
    tutorialBackground = Color(0xfff6cf47),
    textFadedSecondary = Color(0xff787878),
    receivePrimary = Color(0x668480e5),
    sendPrimary = Color(0x6676dab3),
    textHeadline = Color(0xff1f0208), // default dark text (not quite black)
    buttonSecondaryFaded = Color(0xc0ffd1bf),
    padawanFaded = Color(0xA6000000),
    error = Color(0xffcc241d),
    md_theme_dark_onBackgroundFaded = Color(0x80ebdbb2),
    md_theme_dark_onLightBackground = Color(0xff282828),
    md_theme_dark_warning = Color(0xfffffabd),

    testPink = Color(0xffff1493),
)


// // New theme
// object PadawanColors {
//     val background = Color(0xFFF2E5D0)                // Sun-faded sand
//     val onBackgroundSecondary = Color(0xFF6A0DAD)     // Deep techno purple card
//     val buttonPrimary = Color(0xffFFD300)             // Cyber yellow
//     val tealAccent = Color(0xffFF007F)                // Raspberry pink toggle
//     val text = Color(0xFF121212)                      // Deep neutral black
//     val navigationBarUnselected = Color(0xFF121212)
//     val textFaded = Color(0x40121212)
//     val accent1 = Color(0xFF00FFC6)                   // Aqua green nav highlight
//
//     val backgroundSecondary = Color(0xFFf3f4ff)       // New!
//     val primary = Color(0xFF76dab3)
//     val onPrimary = Color(0xFF000000)
//     val tutorialBackground = Color(0xfff6cf47)
//     val textFadedSecondary = Color(0xff787878)
//     val receivePrimary = Color(0x668480e5)
//     val sendPrimary = Color(0x6676dab3)
//     val textHeadline = Color(0xff1f0208) // default dark text (not quite black)
//     val buttonSecondaryFaded = Color(0xc0ffd1bf)
//     val padawanFaded = Color(0xA6000000)
//     val error = Color(0xffcc241d)
//     val md_theme_dark_onBackgroundFaded = Color(0x80ebdbb2)
//     val md_theme_dark_onLightBackground = Color(0xff282828)
//     val md_theme_dark_warning = Color(0xfffabd2f)
//
//     val testPink = Color(0xffff1493)
// }









// New theme (new4)
// object PadawanColors {
//     val background = Color(0xFFf3f4ff)                // New!
//     val onBackgroundSecondary = Color(0xFF4CB7C1)     // New!
//     val buttonPrimary = Color(0xffFFCC00)             // New!
//     val buttonSecondary = Color(0xffFF6B00)           // New!
//     val backgroundSecondary = Color(0xFFf3f4ff)
//
//     val primary = Color(0xFF76dab3)
//     val onPrimary = Color(0xFF000000)
//     val onBackgroundFaded = Color(0x668480e5)
//     val navigationBarUnselected = Color(0xFF8a8a8a)
//     val tutorialBackground = Color(0xfff6cf47)
//     val textFaded = Color(0x60000000)
//     val textFadedSecondary = Color(0xff787878)
//     val receivePrimary = Color(0x668480e5)
//     val sendPrimary = Color(0x6676dab3)
//     val textHeadline = Color(0xff1f0208) // default dark text (not quite black)
//     val buttonSecondaryFaded = Color(0xc0ffd1bf)
//     val padawanFaded = Color(0xA6000000)
//     val lazyColumnBackground = Color(0xFFfee7e0)
//     val error = Color(0xffcc241d)
//     val md_theme_dark_onBackgroundFaded = Color(0x80ebdbb2)
//     val md_theme_dark_onLightBackground = Color(0xff282828)
//     val md_theme_dark_warning = Color(0xfffabd2f)
//
//     val testPink = Color(0xffff1493)
// }

// // New theme
// object PadawanColors {
//     val background = Color(0xFFffffff)                // New!
//     val onBackgroundSecondary = Color(0xFFffffff)     // New!
//     val buttonPrimary = Color(0xffFFCC00)             // New!
//     val buttonSecondary = Color(0xffFF6B00)           // New!
//     val backgroundSecondary = Color(0xFFf3f4ff)
//
//     val primary = Color(0xFF76dab3)
//     val onPrimary = Color(0xFF000000)
//     val onBackgroundFaded = Color(0x668480e5)
//     val navigationBarUnselected = Color(0xFF8a8a8a)
//     val tutorialBackground = Color(0xfff6cf47)
//     val textFaded = Color(0x60000000)
//     val textFadedSecondary = Color(0xff787878)
//     val receivePrimary = Color(0x668480e5)
//     val sendPrimary = Color(0x6676dab3)
//     val textHeadline = Color(0xff1f0208) // default dark text (not quite black)
//     val buttonSecondaryFaded = Color(0xc0ffd1bf)
//     val padawanFaded = Color(0xA6000000)
//     val lazyColumnBackground = Color(0xFFfee7e0)
//     val error = Color(0xffcc241d)
//     val md_theme_dark_onBackgroundFaded = Color(0x80ebdbb2)
//     val md_theme_dark_onLightBackground = Color(0xff282828)
//     val md_theme_dark_warning = Color(0xfffabd2f)
//
//     val testPink = Color(0xffff1493)
// }

// New theme
// object PadawanColors {
//     val background = Color(0xFF1A9AAA)                // New!
//     val onBackgroundSecondary = Color(0xFF4CB7C1)     // New!
//     val buttonPrimary = Color(0xffFFCC00)             // New!
//     val buttonSecondary = Color(0xffFF6B00)           // New!
//
//     val primary = Color(0xFF76dab3)
//     val onPrimary = Color(0xFF000000)
//     val backgroundSecondary = Color(0xFFf3f4ff)
//     val onBackgroundFaded = Color(0x668480e5)
//     val navigationBarUnselected = Color(0xFF8a8a8a)
//     val tutorialBackground = Color(0xfff6cf47)
//     val textFaded = Color(0x60000000)
//     val textFadedSecondary = Color(0xff787878)
//     val receivePrimary = Color(0x668480e5)
//     val sendPrimary = Color(0x6676dab3)
//     val textHeadline = Color(0xff1f0208) // default dark text (not quite black)
//     val buttonSecondaryFaded = Color(0xc0ffd1bf)
//     val padawanFaded = Color(0xA6000000)
//     val lazyColumnBackground = Color(0xFFfee7e0)
//     val error = Color(0xffcc241d)
//     val md_theme_dark_onBackgroundFaded = Color(0x80ebdbb2)
//     val md_theme_dark_onLightBackground = Color(0xff282828)
//     val md_theme_dark_warning = Color(0xfffabd2f)
//
//     val testPink = Color(0xffff1493)
// }

