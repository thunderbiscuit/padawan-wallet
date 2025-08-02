//
//  SwiftGenAssets+Helpers.swift
//  PadawanWallet
//
//  Created by Rubens Machion on 24/07/25.
//

import SwiftUI

extension ImageAsset {
    var toImage: SwiftUI.Image {
        self.swiftUIImage
    }
}

extension ColorAsset {
    var toColor: SwiftUI.Color {
        self.swiftUIColor
    }
}
