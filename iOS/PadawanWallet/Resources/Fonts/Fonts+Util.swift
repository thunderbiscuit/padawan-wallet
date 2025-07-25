//
//  Fonts+Util.swift
//  PadawanWallet
//
//  Created by Rubens Machion on 18/07/25.
//

import SwiftUI

extension Fonts {
    static func font(
        _ weight: SwiftUI.Font.Weight = .regular,
        _ size: CGFloat = 14.0
    ) -> SwiftUI.Font {
        switch weight {
        case .bold, .semibold:
            return Fonts.Outfit.semiBold.swiftUIFont(size: size)

        case .medium:
            return Fonts.Outfit.medium.swiftUIFont(size: size)

        default:
            return Fonts.Outfit.regular.swiftUIFont(size: size)
        }
    }
    
    static var title: SwiftUI.Font {
        Fonts.font(.bold, 32)
    }
    
    static var subtitle: SwiftUI.Font {
        Fonts.font(.regular, 18)
    }
    
    static var caption: SwiftUI.Font {
        Fonts.font(.medium, 18)
    }
}
