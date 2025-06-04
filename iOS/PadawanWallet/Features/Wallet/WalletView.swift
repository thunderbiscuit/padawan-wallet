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
                    Label("Wallet", systemImage: "flame.fill")
                }

            LessonsRootView()
                .tabItem {
                    Label("Learn", systemImage: "leaf.fill")
                }

            MoreRootView()
                .tabItem {
                    Label("More", systemImage: "drop.fill")
                }
        }
    }
}



struct WalletRootView: View {
    var body: some View {
        NavigationStack {
            ScrollView {
                VStack(spacing: 24) {
                    BalanceCard()
                    ActionButtons()
                    TransactionsCard()
                }
                .padding()
                .background(Color("Background"))
            }
        }
    }
}
