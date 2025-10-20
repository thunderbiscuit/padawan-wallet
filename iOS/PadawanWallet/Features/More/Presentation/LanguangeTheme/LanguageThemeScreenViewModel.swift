//
//  LanguageThemeScreenViewModel.swift
//  PadawanWallet
//
//  Created by Rubens Machion on 14/09/25.
//

// LanguageThemeScreenViewModel.swift

import SwiftUI
import Foundation

@MainActor
final class LanguageThemeScreenViewModel: ObservableObject {
    
    @Published var fullScreenCover: MoreScreenNavigation?
    @Published var selectedLanguage: PadawanLanguage
    init() {
        self.selectedLanguage = LanguageManager.shared.currentLanguage
    }
    
    @Published var selectedTheme: PadawanColorTheme = Session.shared.themeChoice
    
    var disabledLanguages: [PadawanLanguage] = []
    var disabledThemes: [PadawanColorTheme] = [.vader]
    
    func selectItem<T: LanguageThemeItemProtocol>(_ item: T) {
        
        switch item {
        case is PadawanLanguage:
            guard let newLanguage = item as? PadawanLanguage,
                  !disabledLanguages.contains(newLanguage),
                  newLanguage != selectedLanguage
            else { return }
            
            let previousLanguage = selectedLanguage
            
            selectedLanguage = newLanguage
            
            fullScreenCover = .alert(
                data: .init(
                    titleKey: "attention",
                    subtitleKey: "alert_change_language",
                    primaryButtonTitleKey: "button_yes",
                    secondaryButtonTitleKey: "button_no",
                    overrideLanguage: newLanguage,
                    onPrimaryButtonTap: {
                        Session.shared.languageChoice = newLanguage
                        LanguageManager.shared.setLanguage(newLanguage)
                        self.fullScreenCover = nil
                    },
                    onSecondaryButtonTap: {
                        self.selectedLanguage = previousLanguage
                        self.fullScreenCover = nil
                    }
                )
            )
            
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
