//
//  WalletView.swift
//  PadawanWallet
//
//  Created by thunderbiscuit on 2025-06-02.
//

import SwiftUI

struct CoreView: View {
    var body: some View {
        TabView {
            WalletRootView()
                .tabItem {
                    Label("Red", systemImage: "flame.fill")
                }

            LessonsRootView()
                .tabItem {
                    Label("Green", systemImage: "leaf.fill")
                }

            MoreRootView()
                .tabItem {
                    Label("Blue", systemImage: "drop.fill")
                }
        }
    }
}

struct WalletRootView: View {
    var body: some View {
        ZStack {
            Color.red.ignoresSafeArea()
            Text("Wallet")
                .font(.largeTitle)
                .foregroundColor(.white)
        }
    }
}
