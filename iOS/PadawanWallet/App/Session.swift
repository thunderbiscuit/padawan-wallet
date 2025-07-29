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
    
    static let shared = Session()
    
    private init() {
        isOnboarding = UserDefaults.standard.object(forKey: .onboardingKey) == nil ?
            true :
            UserDefaults.standard.bool(forKey: .onboardingKey)
    }
    
    func setNeedOnboarding(_ value: Bool) {
        isOnboarding = value
        UserDefaults.standard.setValue(isOnboarding, forKey: .onboardingKey)
    }
}
