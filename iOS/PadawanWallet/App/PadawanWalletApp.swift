//
//  PadawanWalletApp.swift
//  PadawanWallet
//
//  Created by thunderbiscuit on 2025-04-16.
//

import SwiftUI

@main
struct PadawanWalletApp: App {
    @AppStorage("isOnboarding") var isOnboarding: Bool = true
    @State private var navigationPath = NavigationPath()

    var body: some Scene {
        WindowGroup {
            if isOnboarding {
                OnboardingView()
            } else {
                CoreView()
            }
        }
    }
}
