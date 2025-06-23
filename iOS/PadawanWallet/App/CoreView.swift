//
//  CoreView.swift
//  PadawanWallet
//
//  Created by thunderbiscuit on 2025-06-13.
//

import SwiftUI

struct CoreView: View {
    var body: some View {
        TabView {
            WalletRootView()
                .tabItem {
                    Label(String(localized: "bottom_nav_wallet"), systemImage: "bitcoinsign.square.fill")
                }

            LessonsRootView()
                .tabItem {
                    Label(String(localized: "bottom_nav_chapters"), systemImage: "graduationcap.fill")
                }

            MoreRootView()
                .tabItem {
                    Label(String(localized: "bottom_nav_settings"), systemImage: "plus.square.fill")
                }
        }
    }
}
