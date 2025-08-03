//
//  Session.swift
//  PadawanWallet
//
//  Created by Rubens Machion on 29/07/25.
//

import SwiftUI

private extension String {
    static let onboardingKey = "isOnboarding"
}

final class Session: ObservableObject {
    @Published var isOnboarding: Bool
    
    private let keyClient: KeyClient
    
    static let shared = Session()
    
    init(keyClient: KeyClient = .live) {
        isOnboarding = UserDefaults.standard.object(forKey: .onboardingKey) == nil ?
            true :
            UserDefaults.standard.bool(forKey: .onboardingKey)
        self.keyClient = keyClient
    }
    
    func setNeedOnboarding(_ value: Bool) {
        isOnboarding = value
        UserDefaults.standard.setValue(isOnboarding, forKey: .onboardingKey)
    }
    
    func walletExists() -> Bool {
        guard (try? keyClient.getBackupInfo()) != nil else {
            return false
        }
        return true
    }
    
    func resetWallet() {
        try? keyClient.deleteBackupInfo()
        setNeedOnboarding(true)
    }
}
