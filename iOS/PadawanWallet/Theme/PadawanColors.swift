/*
 * Copyright 2020-2025 thunderbiscuit and contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE file.
 */

import SwiftUI

struct PadawanColors {
    let background: Color
    let background2: Color
    let accent1: Color
    let accent2: Color
    let accent3: Color
    let accent1Light: Color
    let text: Color
    let textLight: Color
    let textFaded: Color
    let goGreen: Color
    let errorRed: Color
    let navigationBarUnselected: Color
    let darkBackground: Color
}

extension PadawanColors {
    static let tatooineDesert = PadawanColors(
        background: Color(red: 0.992, green: 0.922, blue: 0.827),        // #FDEBD3
        background2: Color(red: 0.918, green: 0.820, blue: 0.706),       // #EAD1B4
        accent1: Color(red: 0.937, green: 0.424, blue: 0.341),           // #EF6C57
        accent2: Color(red: 1.0, green: 0.667, blue: 0.0),               // #FFAA00
        accent3: Color(red: 0.702, green: 0.278, blue: 0.0),             // #B34700
        accent1Light: Color(red: 0.976, green: 0.667, blue: 0.467),      // #F9AA77
        text: Color(red: 0.102, green: 0.102, blue: 0.102),              // #1A1A1A
        textLight: Color(red: 0.353, green: 0.353, blue: 0.353),         // #5A5A5A
        textFaded: Color(red: 0.227, green: 0.227, blue: 0.235).opacity(0.25), // #403A3A3C
        goGreen: Color(red: 0.482, green: 0.686, blue: 0.482),           // #7BAF7B
        errorRed: Color(red: 0.784, green: 0.392, blue: 0.302),          // #C8644D
        navigationBarUnselected: Color(red: 0.227, green: 0.227, blue: 0.235), // #3A3A3C
        darkBackground: Color(red: 0.180, green: 0.106, blue: 0.039)     // #2E1B0A
    )
    
    static let vaderDark = PadawanColors(
        background: Color(red: 0.071, green: 0.071, blue: 0.071),        // #121212
        background2: Color(red: 0.118, green: 0.118, blue: 0.118),       // #1E1E1E
        accent1: Color(red: 0.937, green: 0.243, blue: 0.212),           // #EF3E36
        accent2: Color(red: 0.718, green: 0.110, blue: 0.110),           // #B71C1C
        accent3: Color(red: 0.216, green: 0.278, blue: 0.310),           // #37474F
        accent1Light: Color(red: 0.980, green: 0.502, blue: 0.447),      // #FA8072
        text: Color(red: 0.925, green: 0.925, blue: 0.925),              // #ECECEC
        textLight: Color(red: 0.690, green: 0.690, blue: 0.690),         // #B0B0B0
        textFaded: Color(red: 0.502, green: 0.502, blue: 0.502).opacity(0.25), // #40808080
        goGreen: Color(red: 0.506, green: 0.784, blue: 0.518),           // #81C784
        errorRed: Color(red: 0.898, green: 0.451, blue: 0.451),          // #E57373
        navigationBarUnselected: Color(red: 0.533, green: 0.533, blue: 0.533), // #888888
        darkBackground: Color(red: 0.039, green: 0.039, blue: 0.039)     // #0A0A0A
    )
}

// Environment key for accessing the current theme
private struct PadawanColorsKey: EnvironmentKey {
    static let defaultValue = PadawanColors.tatooineDesert
}

extension EnvironmentValues {
    var padawanColors: PadawanColors {
        get { self[PadawanColorsKey.self] }
        set { self[PadawanColorsKey.self] = newValue }
    }
}