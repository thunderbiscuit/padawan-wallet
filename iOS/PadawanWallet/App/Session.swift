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
        setNeedOnboarding(true)
    }
}
