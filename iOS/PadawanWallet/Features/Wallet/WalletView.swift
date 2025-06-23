//
//  WalletView.swift
//  PadawanWallet
//
//  Created by thunderbiscuit on 2025-06-02.
//

import SwiftUI

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
