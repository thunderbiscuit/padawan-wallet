//
//  Configuration.swift
//  PadawanWallet
//
//  Created by Rubens Machion on 21/09/25.
//

protocol LanguageThemeItemProtocol: Equatable, CaseIterable, Identifiable, RawRepresentable where RawValue == String { }

enum PadawanLanguage: String, LanguageThemeItemProtocol {
    case english = "English"
    case spanish = "Spanish"
    case portuguese = "Portuguese"
    case french = "French"

    var id: String { self.rawValue }
}

enum PadawanColorTheme: String, LanguageThemeItemProtocol {
    case tatooine = "Tatooine Desert"
    case vader = "Vader Dark (Coming Soon!)"

    var id: String { self.rawValue }
}
