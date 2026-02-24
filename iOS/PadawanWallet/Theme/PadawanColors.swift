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
    let cardShadowColor: Color
}

extension PadawanColors: Equatable {
    static let tatooineDesert = PadawanColors(
        background: Asset.Colors.tatooineDesertBackground.toColor,
        background2: Asset.Colors.tatooineDesertBackground2.toColor,
        accent1: Asset.Colors.tatooineDesertAccent1.toColor,
        accent2: Asset.Colors.tatooineDesertAccent2.toColor,
        accent3: Asset.Colors.tatooineDesertAccent3.toColor,
        accent1Light: Asset.Colors.tatooineDesertAccent1Light.toColor,
        text: Asset.Colors.tatooineDesertText.toColor,
        textLight: Asset.Colors.tatooineDesertTextLight.toColor,
        textFaded: Asset.Colors.tatooineDesertTextFaded.toColor,
        goGreen: Asset.Colors.tatooineDesertGoGreen.toColor,
        errorRed: Asset.Colors.tatooineDesertErrorRed.toColor,
        navigationBarUnselected: Asset.Colors.tatooineDesertNavigationBarUnselected.toColor,
        darkBackground: Asset.Colors.tatooineDesertDarkBackground.toColor,
        cardShadowColor: Asset.Colors.tatooineDesertShadowColor.toColor
    )
    
    static let vaderDark = PadawanColors(
        background: Asset.Colors.vaderDarkBackground.toColor,
        background2: Asset.Colors.vaderDarkBackground2.toColor,
        accent1: Asset.Colors.vaderDarkAccent1.toColor,
        accent2: Asset.Colors.vaderDarkAccent2.toColor,
        accent3: Asset.Colors.vaderDarkAccent3.toColor,
        accent1Light: Asset.Colors.vaderDarkAccent1Light.toColor,
        text: Asset.Colors.vaderDarkText.toColor,
        textLight: Asset.Colors.vaderDarkTextLight.toColor,
        textFaded: Asset.Colors.vaderDarkTextFaded.toColor,
        goGreen: Asset.Colors.vaderDarkGoGreen.toColor,
        errorRed: Asset.Colors.vaderDarkErrorRed.toColor,
        navigationBarUnselected: Asset.Colors.vaderDarkNavigationBarUnselected.toColor,
        darkBackground: Asset.Colors.vaderDarkDarkBackground.toColor,
        cardShadowColor: Asset.Colors.vaderDarkShadowColor.toColor
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
