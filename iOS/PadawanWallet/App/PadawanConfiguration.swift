//
//  Configuration.swift
//  PadawanWallet
//
//  Created by Rubens Machion on 21/09/25.
//

import Foundation

protocol LanguageThemeItemProtocol: Equatable, CaseIterable, Identifiable, RawRepresentable where RawValue == String { }

enum PadawanLanguage: String, LanguageThemeItemProtocol {
    case english = "English"
    case spanish = "Spanish"
    case portuguese = "Portuguese"
    case french = "French"

    var id: String { self.rawValue }
    
    var code: String {
        switch self {
        case .english:
            return "en"
        case .spanish:
            return "es"
        case .portuguese:
            return "pt"
        case .french:
            return "fr"
        }
    }
    
    var locale: Locale {
        Locale(identifier: self.code)
    }
}

enum PadawanColorTheme: String, LanguageThemeItemProtocol {
    case tatooine = "Tatooine Desert"
    case vader = "Vader Dark"

    var id: String { self.rawValue }
    
    var colors: PadawanColors {
        switch self {
        case .tatooine:
            return .tatooineDesert
        case .vader:
            return .vaderDark
        }
    }
}
