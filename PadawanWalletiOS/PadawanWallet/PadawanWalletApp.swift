//
//  PadawanWalletApp.swift
//  PadawanWallet
//
//  Copyright 2024 thunderbiscuit, geigerzaehler, and contributors.
//  Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE.txt file.
//

import SwiftUI

@main
struct PadawanWalletApp: App {
    @AppStorage("isOnboarding") var isOnboarding: Bool = true

    var body: some Scene {
        WindowGroup {
            if isOnboarding {
                WelcomeView().environmentObject(WalletViewModel())
            } else {
                ContentView().environmentObject(WalletViewModel())
            }
        }
    }
}
