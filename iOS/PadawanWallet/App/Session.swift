//
//  Session.swift
//  PadawanWallet
//
//  Created by Rubens Machion on 29/07/25.
//

import SwiftUI

private extension String {
    static let onboardingKey = "isOnboarding"
    static let fullScanRequiredKey = "isfullScanRequiredKey"
    static let lastBalanceUpdateKey = "lastBalanceUpdate"
    static let languageChoiceKey = "languageChoice"
    static let themeChoiceKey = "themeChoice"
}

final class Session: ObservableObject {
    @Published var isOnboarding: Bool
    
    private let keyClient: KeyClient
    
    static let shared = Session()
    
    private let defaults = UserDefaults.standard
    
    var isFullScanRequired: Bool {
        get {
            return defaults.bool(forKey: .fullScanRequiredKey)
        }
        set {
            defaults.setValue(newValue, forKey: .fullScanRequiredKey)
        }
    }
    
    var lastBalanceUpdate: UInt64 {
        get {
            return (defaults.object(forKey: .lastBalanceUpdateKey) as? UInt64) ?? 0
        }
        
        set {
            defaults.setValue(newValue, forKey: .lastBalanceUpdateKey)
        }
    }
    
    var languageChoice: PadawanLanguage {
        get {
            guard let language = defaults.string(forKey: .languageChoiceKey) else {
                return .english
            }
            return .init(rawValue: language) ?? .english
        }
        set {
            defaults.set([newValue.code], forKey: "AppleLanguages")
            defaults.set(newValue.rawValue, forKey: .languageChoiceKey)
        }
    }
    
    var themeChoice: PadawanColorTheme {
        get {
            guard let theme = defaults.string(forKey: .themeChoiceKey) else {
                return .tatooine
            }
            return .init(rawValue: theme) ?? .tatooine
        }
        set {
            defaults.set(newValue.rawValue, forKey: .languageChoiceKey)
        }
    }
    
    init(keyClient: KeyClient = .live) {
        isOnboarding = defaults.object(forKey: .onboardingKey) == nil ?
            true :
        defaults.bool(forKey: .onboardingKey)
        self.keyClient = keyClient
    }
    
    func setNeedOnboarding(_ value: Bool) {
        isOnboarding = value
        defaults.setValue(isOnboarding, forKey: .onboardingKey)
    }
    
    func walletExists() -> Bool {
        guard (try? keyClient.getBackupInfo()) != nil else {
            return false
        }
        return true
    }
    
    func resetWallet() {
        try? keyClient.deleteBackupInfo()
        isFullScanRequired = true
        lastBalanceUpdate = .zero
        languageChoice = .english
        themeChoice = .tatooine
        setNeedOnboarding(true)
        resetLessons()
    }
    
    func resetLessons() {
        for i in 1...100 {
            let key = "l\(i)_title"
            UserDefaultsStorage.shared.remove(key)
        }
    }
}
