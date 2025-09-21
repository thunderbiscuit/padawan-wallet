//
//  LanguageThemeScreenViewModel.swift
//  PadawanWallet
//
//  Created by Rubens Machion on 14/09/25.
//

import SwiftUI
import Foundation

@MainActor
final class LanguageThemeScreenViewModel: ObservableObject {
    
    @Published var selectedLanguage: PadawanLanguage = Session.shared.languageChoice
    @Published var selectedTheme: PadawanColorTheme = Session.shared.themeChoice
    
    var disabledLanguages: [PadawanLanguage] = []
    var disabledThemes: [PadawanColorTheme] = [.vader]
    
    init() {
        
    }
    
    func selectItem<T: LanguageThemeItemProtocol>(_ item: T) {
        
        switch item {
        case is PadawanLanguage:
            if let language = item as? PadawanLanguage, !disabledLanguages.contains(language) {
                selectedLanguage = language
                Session.shared.languageChoice = language
            }
        
        case is PadawanColorTheme:
            if let theme = item as? PadawanColorTheme, !disabledThemes.contains(theme) {
                selectedTheme = theme
                Session.shared.themeChoice = theme
            }
            
        default:
            break
        }
    }
}
