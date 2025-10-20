//
//  LanguageManager.swift
//  PadawanWallet
//
//  Created by Vinicius Silva Moreira on 14/10/25.
//

import Foundation
import Combine

class LanguageManager: ObservableObject {
    
    static let shared = LanguageManager()
    
    @Published var currentLanguage: PadawanLanguage {
        didSet {
            storage.set(currentLanguage.code, key: "selectedLanguage")
            updateBundle()
        }
    }
    
    private var bundle: Bundle?
    private let storage: StorageProtocol

    private init(storage: StorageProtocol = UserDefaultsStorage.shared) {
        self.storage = storage

        if let savedLangCode: String = storage.get("selectedLanguage"),
           let savedLang = PadawanLanguage.allCases.first(where: { $0.code == savedLangCode }) {
            self.currentLanguage = savedLang
        } else {
            let deviceLanguage = Bundle.main.preferredLocalizations.first ?? "en"
            self.currentLanguage = PadawanLanguage.allCases.first(where: { $0.code == deviceLanguage }) ?? .english
        }
        updateBundle()
    }
    
    func setLanguage(_ language: PadawanLanguage) {
        currentLanguage = language
    }

    func localizedString(forKey key: String) -> String {
        return bundle?.localizedString(forKey: key, value: nil, table: nil) ?? key
    }
    
    private func updateBundle() {
        guard let path = Bundle.main.path(forResource: currentLanguage.code, ofType: "lproj"),
              let newBundle = Bundle(path: path) else {
            self.bundle = Bundle.main
            return
        }
        self.bundle = newBundle
    }
    static func translate(key: String, to language: PadawanLanguage) -> String {
        guard let path = Bundle.main.path(forResource: language.code, ofType: "lproj"),
              let bundle = Bundle(path: path) else {
            return key
        }
        return bundle.localizedString(forKey: key, value: nil, table: nil)
    }
}
